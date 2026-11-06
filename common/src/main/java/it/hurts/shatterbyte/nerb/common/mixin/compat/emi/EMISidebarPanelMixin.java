package it.hurts.shatterbyte.nerb.common.mixin.compat.emi;

import dev.emi.emi.config.SidebarType;
import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.impl.EMIEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "dev.emi.emi.screen.EmiScreenManager$SidebarPanel", remap = false)
public abstract class EMISidebarPanelMixin {
    @Shadow
    public abstract SidebarType getType();

    @Inject(method = "isVisible", at = @At("HEAD"), cancellable = true)
    private void onIsVisible(CallbackInfoReturnable<Boolean> cir) {
        if (EMIEntry.isSidebarHidden(getType()))
            cir.setReturnValue(false);
    }
}
