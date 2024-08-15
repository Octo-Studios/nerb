package it.hurts.octostudios.nerb.common.mixin;

import it.hurts.octostudios.nerb.common.config.misc.ButtonMode;
import it.hurts.octostudios.nerb.common.init.ConfigRegistry;
import net.minecraft.stats.RecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBook.class)
public class RecipeBookMixin {
    @Inject(method = "copyOverData", at = @At("HEAD"), cancellable = true)
    public void onCopy(RecipeBook pOther, CallbackInfo ci) {
        if (ConfigRegistry.CONFIG.getButtonMode() != ButtonMode.DISCOVERED)
            ci.cancel();
    }
}