package it.hurts.sskirillss.nerb.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleUpdateRecipes", at = @At("HEAD"), cancellable = true)
    public void onSync(ClientboundUpdateRecipesPacket pPacket, CallbackInfo ci) {
        ci.cancel();
    }
}