package it.hurts.shatterbyte.nerb.common.mixin;

import com.mojang.serialization.Codec;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeBookRules;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeManagerAccess;
import it.hurts.shatterbyte.nerb.common.recipebook.ServerRecipeBookAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    @Final
    private ServerRecipeBook recipeBook;

    @Unique
    private static final Codec<List<ResourceLocation>> nerb$removedRecipesCodec = ResourceLocation.CODEC.listOf();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onCreateRecipeBook(CallbackInfo ci) {
        ((ServerRecipeBookAccess) recipeBook).nerb$setServer(server);
    }

    @Inject(method = "awardRecipes", at = @At("HEAD"), cancellable = true)
    public void onAwardRecipes(Collection<RecipeHolder<?>> holders, CallbackInfoReturnable<Integer> cir) {
        if (!nerb$getRecipeBookRules().hasVanillaRecipes())
            cir.setReturnValue(0);
    }

    @Inject(method = "awardRecipesByKey", at = @At("HEAD"), cancellable = true)
    public void onAwardRecipesByKey(List<ResourceLocation> recipes, CallbackInfo ci) {
        if (!nerb$getRecipeBookRules().hasVanillaRecipes())
            ci.cancel();
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void onLoadRemovedRecipes(CompoundTag tag, CallbackInfo ci) {
        var removedRecipes = ((ServerRecipeBookAccess) recipeBook).nerb$getRemovedRecipes();

        removedRecipes.clear();
        if (tag.contains("nerb:removed_recipes"))
            nerb$removedRecipesCodec.parse(NbtOps.INSTANCE, tag.get("nerb:removed_recipes")).result().ifPresent(removedRecipes::addAll);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void onSaveRemovedRecipes(CompoundTag tag, CallbackInfo ci) {
        var removedRecipes = ((ServerRecipeBookAccess) recipeBook).nerb$getRemovedRecipes();

        if (!removedRecipes.isEmpty())
            tag.put("nerb:removed_recipes", nerb$removedRecipesCodec.encodeStart(NbtOps.INSTANCE, List.copyOf(removedRecipes)).getOrThrow());
    }

    @Unique
    private RecipeBookRules nerb$getRecipeBookRules() {
        return ((RecipeManagerAccess) server.getRecipeManager()).nerb$getRecipeBookRules();
    }
}
