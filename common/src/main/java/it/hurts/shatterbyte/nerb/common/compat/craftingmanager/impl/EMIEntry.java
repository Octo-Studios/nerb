package it.hurts.shatterbyte.nerb.common.compat.craftingmanager.impl;

import dev.emi.emi.config.SidebarType;
import dev.emi.emi.screen.EmiScreenManager;
import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.impl.base.ICMEntry;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;

import java.util.EnumSet;

public class EMIEntry implements ICMEntry {
    private static final EnumSet<SidebarType> HIDDEN_SIDEBARS = EnumSet.noneOf(SidebarType.class);

    public static boolean isSidebarHidden(SidebarType type) {
        return HIDDEN_SIDEBARS.contains(type);
    }

    @Override
    public void toggleVisibility() {
        var sidebars = ConfigRegistry.EMI_COMPAT.getToggleableSidebars();

        if (!sidebars.isEmpty()) {
            if (HIDDEN_SIDEBARS.isEmpty()) {
                for (var entry : sidebars)
                    HIDDEN_SIDEBARS.add(SidebarType.fromName(entry));
            } else
                HIDDEN_SIDEBARS.clear();

            EmiScreenManager.forceRecalculate();
        } else
            EmiScreenManager.toggleVisibility(false);
    }
}
