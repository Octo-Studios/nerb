package it.hurts.shatterbyte.nerb.common.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeBookRules;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeManagerAccess;
import it.hurts.shatterbyte.nerb.common.recipebook.ServerRecipeBookAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundRecipePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin extends RecipeBook implements ServerRecipeBookAccess {
    @Unique
    private MinecraftServer nerb$server;

    @Unique
    private final Set<ResourceLocation> nerb$removedRecipes = new HashSet<>();

    @Unique
    private RecipeBookRules nerb$sentRules;

    @Override
    public void nerb$setServer(MinecraftServer server) {
        nerb$server = server;
    }

    @Override
    public Set<ResourceLocation> nerb$getRemovedRecipes() {
        return nerb$removedRecipes;
    }

    @Override
    public RecipeBookRules nerb$getSentRules() {
        return nerb$sentRules;
    }

    @Override
    protected void add(ResourceLocation recipe) {
        nerb$removedRecipes.remove(recipe);
        super.add(recipe);
    }

    @Override
    protected void remove(ResourceLocation recipe) {
        if (known.contains(recipe))
            nerb$removedRecipes.add(recipe);

        super.remove(recipe);
    }

    @Override
    public void copyOverData(RecipeBook book) {
        super.copyOverData(book);

        nerb$removedRecipes.clear();
        nerb$sentRules = null;
        if (book instanceof ServerRecipeBookAccess other) {
            nerb$removedRecipes.addAll(other.nerb$getRemovedRecipes());
            nerb$sentRules = other.nerb$getSentRules();
        }
    }

    @WrapMethod(method = "addRecipes")
    private int onAddRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer player, Operation<Integer> original) {
        var rules = nerb$getRecipeBookRules();

        if (!rules.hasVanillaRecipes())
            return 0;

        return original.call(recipes.stream().filter(recipe -> rules.isVanilla(recipe.id())).toList(), player);
    }

    @WrapMethod(method = "removeRecipes")
    private int onRemoveRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer player, Operation<Integer> original) {
        var rules = nerb$getRecipeBookRules();

        if (!rules.isDiscoveredMode())
            return original.call(recipes, player);

        if (!rules.hasVanillaRecipes())
            return 0;

        return original.call(recipes.stream().filter(recipe -> rules.isVanilla(recipe.id())).toList(), player);
    }

    @Override
    public boolean contains(RecipeHolder<?> recipe) {
        return recipe != null && contains(recipe.id());
    }

    @Override
    public boolean contains(ResourceLocation recipe) {
        var rules = nerb$getRecipeBookRules();

        if (rules.isDiscoveredMode() && !rules.isVanilla(recipe))
            return rules.isDiscovered(recipe);

        return super.contains(recipe);
    }

    @Inject(method = "sendInitialRecipeBook", at = @At("HEAD"), cancellable = true)
    private void onSendInitial(ServerPlayer player, CallbackInfo ci) {
        var rules = nerb$getRecipeBookRules();
        nerb$removeStaleRecipes(player);

        if (ConfigRegistry.SERVER.isClearRecipeBookData()) {
            known.removeIf(recipe -> !rules.isVanilla(recipe));
            highlight.removeIf(recipe -> !rules.isVanilla(recipe));

            if (!rules.isDiscoveredMode() && !rules.hasVanillaRecipes())
                setBookSettings(new RecipeBookSettings());
        }

        var recipes = rules.discoveredRecipes();
        var highlights = List.<ResourceLocation>of();
        if (rules.hasVanillaRecipes()) {
            for (var advancement : nerb$server.getAdvancements().getAllAdvancements()) {
                var rewards = advancement.value().rewards().recipes();

                if (rewards.stream().anyMatch(rules::isVanilla) && player.getAdvancements().getOrStartProgress(advancement).isDone())
                    player.awardRecipesByKey(rewards.stream().filter(recipe -> !nerb$removedRecipes.contains(recipe)).toList());
            }

            recipes = Stream.concat(recipes.stream(), known.stream().filter(rules::isVanilla)).collect(ImmutableList.toImmutableList());
            highlights = highlight.stream().filter(rules::isVanilla).toList();
        }

        player.connection.send(new ClientboundRecipePacket(ClientboundRecipePacket.State.INIT, recipes, highlights, getBookSettings().copy()));
        ci.cancel();
    }

    @Override
    public void nerb$removeStaleRecipes(ServerPlayer player) {
        var rules = nerb$getRecipeBookRules();
        var previous = nerb$sentRules;
        if (previous == rules)
            return;

        nerb$sentRules = rules;
        if (previous == null)
            return;

        var removed = Stream.concat(
                previous.discoveredRecipes().stream().filter(recipe -> !rules.isDiscovered(recipe)),
                known.stream().filter(recipe -> previous.isVanilla(recipe) && !rules.isVanilla(recipe))
        ).toList();
        if (!removed.isEmpty())
            player.connection.send(new ClientboundRecipePacket(ClientboundRecipePacket.State.REMOVE, removed, List.of(), getBookSettings().copy()));
    }

    @Inject(method = "fromNbt", at = @At("HEAD"), cancellable = true)
    private void onLoad(CompoundTag tag, RecipeManager recipeManager, CallbackInfo ci) {
        if (!ConfigRegistry.SERVER.isClearRecipeBookData())
            return;

        var rules = ((RecipeManagerAccess) recipeManager).nerb$getRecipeBookRules();
        if (rules.hasVanillaRecipes())
            return;

        if (rules.isDiscoveredMode())
            setBookSettings(RecipeBookSettings.read(tag));

        ci.cancel();
    }

    @ModifyVariable(method = "loadRecipes", at = @At("HEAD"), argsOnly = true)
    private Consumer<RecipeHolder<?>> onLoadRecipes(Consumer<RecipeHolder<?>> output) {
        if (!ConfigRegistry.SERVER.isClearRecipeBookData())
            return output;

        var rules = nerb$getRecipeBookRules();
        return recipe -> {
            if (rules.isVanilla(recipe.id()))
                output.accept(recipe);
        };
    }

    @Unique
    private RecipeBookRules nerb$getRecipeBookRules() {
        return ((RecipeManagerAccess) nerb$server.getRecipeManager()).nerb$getRecipeBookRules();
    }
}
