package it.hurts.sskirillss.nerb.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundRecipePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(ServerRecipeBook.class)
public abstract class ServerRecipeBookMixin {
    @Inject(method = "addRecipes", at = @At("HEAD"), cancellable = true)
    public void denyAllRecipes(Collection<Recipe<?>> collection, ServerPlayer serverPlayer, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }

    @Inject(method = "toNbt", at = @At("HEAD"), cancellable = true)
    public void onSave(CallbackInfoReturnable<CompoundTag> cir) {
        cir.setReturnValue(new CompoundTag());
    }

    @Inject(method = "fromNbt", at = @At("HEAD"), cancellable = true)
    public void onLoad(CompoundTag pTag, RecipeManager pRecipeManager, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "sendRecipes", at = @At("HEAD"), cancellable = true)
    public void onLoad(ClientboundRecipePacket.State pState, ServerPlayer pPlayer, List<ResourceLocation> pRecipes, CallbackInfo ci) {
        ci.cancel();
    }
}