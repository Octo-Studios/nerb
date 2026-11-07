package it.hurts.shatterbyte.nerb.common.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeBookRules;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeManagerAccess;
import it.hurts.shatterbyte.nerb.common.recipebook.ServerRecipeBookAccess;
import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;
import net.minecraft.network.protocol.game.ClientboundRecipeBookSettingsPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin implements ServerRecipeBookAccess {
    @Shadow
    @Final
    protected Set<ResourceKey<Recipe<?>>> known;

    @Shadow
    @Final
    protected Set<ResourceKey<Recipe<?>>> highlight;

    @Unique
    private MinecraftServer nerb$server;

    @Unique
    private final Set<ResourceKey<Recipe<?>>> nerb$removedRecipes = new HashSet<>();

    @Override
    public void nerb$setServer(MinecraftServer server) {
        nerb$server = server;
    }

    @Override
    public Set<ResourceKey<Recipe<?>>> nerb$getRemovedRecipes() {
        return nerb$removedRecipes;
    }

    @Inject(method = "add", at = @At("HEAD"))
    private void onAdd(ResourceKey<Recipe<?>> recipe, CallbackInfo ci) {
        nerb$removedRecipes.remove(recipe);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void onRemove(ResourceKey<Recipe<?>> recipe, CallbackInfo ci) {
        if (known.contains(recipe))
            nerb$removedRecipes.add(recipe);
    }

    @Inject(method = "copyOverData", at = @At("TAIL"))
    private void onCopyOverData(ServerRecipeBook book, CallbackInfo ci) {
        nerb$removedRecipes.clear();
        nerb$removedRecipes.addAll(((ServerRecipeBookAccess) book).nerb$getRemovedRecipes());
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

    @Inject(method = "contains", at = @At("HEAD"), cancellable = true)
    private void onContains(ResourceKey<Recipe<?>> recipe, CallbackInfoReturnable<Boolean> cir) {
        var rules = nerb$getRecipeBookRules();

        if (rules.isDiscoveredMode() && !rules.isVanilla(recipe))
            cir.setReturnValue(rules.isDiscovered(recipe));
    }

    @WrapMethod(method = "sendInitialRecipeBook")
    private void onSendInitial(ServerPlayer player, Operation<Void> original) {
        var rules = nerb$getRecipeBookRules();

        if (ConfigRegistry.SERVER.isClearRecipeBookData()) {
            known.removeIf(recipe -> !rules.isVanilla(recipe));
            highlight.removeIf(recipe -> !rules.isVanilla(recipe));

            if (!rules.isDiscoveredMode() && !rules.hasVanillaRecipes())
                ((ServerRecipeBook) (Object) this).setBookSettings(new RecipeBookSettings());
        }

        if (rules.hasVanillaRecipes()) {
            for (var advancement : nerb$server.getAdvancements().getAllAdvancements()) {
                var recipes = advancement.value().rewards().recipes();

                if (recipes.stream().anyMatch(rules::isVanilla) && player.getAdvancements().getOrStartProgress(advancement).isDone())
                    player.awardRecipesByKey(recipes.stream().filter(recipe -> !nerb$removedRecipes.contains(recipe)).toList());
            }

            original.call(player);
        } else
            player.connection.send(new ClientboundRecipeBookSettingsPacket(((ServerRecipeBook) (Object) this).getBookSettings().copy()));

        if (rules.isDiscoveredMode())
            player.connection.send(((RecipeManagerAccess) nerb$server.getRecipeManager()).nerb$getDiscoveredRecipes());
        else if (!rules.hasVanillaRecipes())
            player.connection.send(new ClientboundRecipeBookAddPacket(List.of(), true));
    }

    @WrapOperation(method = "sendInitialRecipeBook", at = @At(value = "INVOKE", target = "Lnet/minecraft/stats/ServerRecipeBook$DisplayResolver;displaysForRecipe(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Consumer;)V"))
    private void onSendKnownRecipe(ServerRecipeBook.DisplayResolver resolver, ResourceKey<Recipe<?>> recipe, Consumer<RecipeDisplayEntry> output, Operation<Void> original) {
        if (nerb$getRecipeBookRules().isVanilla(recipe))
            original.call(resolver, recipe, output);
    }

    @Inject(method = "pack", at = @At("HEAD"), cancellable = true)
    private void onPack(CallbackInfoReturnable<ServerRecipeBook.Packed> cir) {
        var rules = nerb$getRecipeBookRules();

        if (!rules.isDiscoveredMode() || !ConfigRegistry.SERVER.isClearRecipeBookData() || rules.hasVanillaRecipes())
            return;

        var book = (ServerRecipeBook) (Object) this;

        cir.setReturnValue(new ServerRecipeBook.Packed(book.getBookSettings().copy(), List.of(), List.of()));
    }

    @WrapMethod(method = "loadUntrusted")
    private void onLoad(ServerRecipeBook.Packed packed, Predicate<ResourceKey<Recipe<?>>> predicate, Operation<Void> original) {
        var rules = nerb$getRecipeBookRules();

        if (!ConfigRegistry.SERVER.isClearRecipeBookData()) {
            original.call(packed, predicate);

            return;
        }

        if (!rules.hasVanillaRecipes()) {
            if (rules.isDiscoveredMode())
                ((ServerRecipeBook) (Object) this).setBookSettings(packed.settings());

            return;
        }

        original.call(new ServerRecipeBook.Packed(packed.settings(),
                packed.known().stream().filter(rules::isVanilla).toList(),
                packed.highlight().stream().filter(rules::isVanilla).toList()), predicate);
    }

    @Unique
    private RecipeBookRules nerb$getRecipeBookRules() {
        return ((RecipeManagerAccess) nerb$server.getRecipeManager()).nerb$getRecipeBookRules();
    }
}
