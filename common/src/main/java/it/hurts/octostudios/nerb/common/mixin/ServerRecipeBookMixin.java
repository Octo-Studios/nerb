package it.hurts.octostudios.nerb.common.mixin;

import it.hurts.octostudios.nerb.common.config.misc.ButtonMode;
import it.hurts.octostudios.nerb.common.init.ConfigRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundRecipePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin {
    @Inject(method = "toNbt", at = @At("HEAD"), cancellable = true)
    public void onSave(CallbackInfoReturnable<CompoundTag> cir) {
        if (ConfigRegistry.CONFIG.getButtonMode() != ButtonMode.DISCOVERED)
            cir.setReturnValue(new CompoundTag());
    }

    @Inject(method = "fromNbt", at = @At("HEAD"), cancellable = true)
    public void onLoad(CompoundTag pTag, RecipeManager pRecipeManager, CallbackInfo ci) {
        if (ConfigRegistry.CONFIG.getButtonMode() != ButtonMode.DISCOVERED)
            ci.cancel();
    }

    @Inject(method = "sendRecipes", at = @At("HEAD"), cancellable = true)
    public void onLoad(ClientboundRecipePacket.State pState, ServerPlayer pPlayer, List<ResourceLocation> pRecipes, CallbackInfo ci) {
        if (ConfigRegistry.CONFIG.getButtonMode() != ButtonMode.DISCOVERED)
            ci.cancel();
    }
}