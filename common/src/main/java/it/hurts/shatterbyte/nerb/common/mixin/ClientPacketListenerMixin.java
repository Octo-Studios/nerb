package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.config.NERBClientConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = {"handleRecipeBookAdd", "handleRecipeBookRemove", "refreshRecipeBook"}, at = @At("HEAD"), cancellable = true)
    private void onRecipeBookUpdate(CallbackInfo ci) {
        if (ConfigRegistry.CLIENT.getButtonMode() != NERBClientConfig.ButtonMode.RECIPE_BOOK)
            ci.cancel();
    }
}
