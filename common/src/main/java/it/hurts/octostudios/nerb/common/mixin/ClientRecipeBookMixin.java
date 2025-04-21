package it.hurts.octostudios.nerb.common.mixin;

import it.hurts.octostudios.nerb.common.config.NERBConfig;
import it.hurts.octostudios.nerb.common.init.ConfigRegistry;
import net.minecraft.client.ClientRecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(method = "rebuildCollections", at = @At("HEAD"), cancellable = true)
    public void onSetup(CallbackInfo ci) {
        if (ConfigRegistry.GENERAL.getButtonMode() != NERBConfig.ButtonMode.DISCOVERED)
            ci.cancel();
    }
}