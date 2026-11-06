package it.hurts.shatterbyte.nerb.common.mixin.compat.emi;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.emi.emi.config.SidebarSubpanels;
import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.impl.EMIEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Pseudo
@Mixin(targets = "dev.emi.emi.screen.EmiScreenManager", remap = false)
public class EMIScreenManagerMixin {
    @ModifyExpressionValue(method = "createScreenSpace", at = @At(value = "FIELD", target = "Ldev/emi/emi/config/SidebarSubpanels;subpanels:Ljava/util/List;"))
    private static List<SidebarSubpanels.Subpanel> onGetSubpanels(List<SidebarSubpanels.Subpanel> original) {
        return original.stream().filter(subpanel -> !EMIEntry.isSidebarHidden(subpanel.type)).toList();
    }
}
