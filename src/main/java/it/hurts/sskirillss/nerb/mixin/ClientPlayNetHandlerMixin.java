package it.hurts.sskirillss.nerb.mixin;

import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.play.server.SUpdateRecipesPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetHandler.class)
public class ClientPlayNetHandlerMixin {
    @Inject(method = "handleUpdateRecipes", at = @At("HEAD"), cancellable = true)
    public void onSync(SUpdateRecipesPacket p_199525_1_, CallbackInfo ci) {
        ci.cancel();
    }
}