package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.CraftingManagerCompat;
import it.hurts.shatterbyte.nerb.common.config.NERBClientConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RecipeBookComponent.class, priority = 900)
public abstract class RecipeBookComponentMixin {
    @Shadow
    public abstract boolean isVisible();

    @Shadow
    protected abstract void setVisible(boolean visible);

    @Inject(method = "isVisible", at = @At("HEAD"), cancellable = true)
    private void onIsVisible(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigRegistry.CLIENT.getButtonMode() != NERBClientConfig.ButtonMode.RECIPE_BOOK)
            cir.setReturnValue(false);
    }

    @Inject(method = "toggleVisibility", at = @At("HEAD"), cancellable = true)
    private void onToggleVisibility(CallbackInfo ci) {
        if (ConfigRegistry.CLIENT.getButtonMode() != NERBClientConfig.ButtonMode.RECIPE_BOOK)
            ci.cancel();
        else if (CraftingManagerCompat.isEMILoaded()) {
            setVisible(!isVisible());
            ci.cancel();
        }
    }

    @Inject(method = "setVisible", at = @At("HEAD"), cancellable = true)
    private void onSetVisible(boolean visible, CallbackInfo ci) {
        if (ConfigRegistry.CLIENT.getButtonMode() != NERBClientConfig.ButtonMode.RECIPE_BOOK)
            ci.cancel();
    }

    @Inject(method = "initVisuals", at = @At("HEAD"), cancellable = true)
    private void onInitVisuals(CallbackInfo ci) {
        if (ConfigRegistry.CLIENT.getButtonMode() != NERBClientConfig.ButtonMode.RECIPE_BOOK)
            ci.cancel();
    }
}
