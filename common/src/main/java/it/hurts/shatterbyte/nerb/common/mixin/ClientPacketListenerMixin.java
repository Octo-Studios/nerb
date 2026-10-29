package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.config.NERBConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "refreshRecipeBook", at = @At("HEAD"), cancellable = true)
    private void onRefreshRecipeBook(ClientRecipeBook recipeBook, CallbackInfo ci) {
        if (ConfigRegistry.GENERAL.getButtonMode() != NERBConfig.ButtonMode.DISCOVERED)
            ci.cancel();
    }
}
