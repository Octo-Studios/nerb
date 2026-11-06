package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.config.NERBClientConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.client.ClientRecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(method = "setupCollections", at = @At("HEAD"), cancellable = true)
    public void onSetup(CallbackInfo ci) {
        if (ConfigRegistry.CLIENT.getButtonMode() != NERBClientConfig.ButtonMode.RECIPE_BOOK)
            ci.cancel();
    }
}
