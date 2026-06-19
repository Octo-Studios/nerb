//package it.hurts.shatterbyte.nerb.common.compat.craftingmanager.impl;
//
//import dev.emi.emi.config.SidebarType;
//import dev.emi.emi.screen.EmiScreenManager;
//import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.impl.base.ICMEntry;
//import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
//
//public class EMIEntry implements ICMEntry {
//    @Override
//    public void toggleVisibility() {
//        var sidebars = ConfigRegistry.EMI_COMPAT.getToggleableSidebars();
//
//        if (!sidebars.isEmpty()) {
//            for (var entry : sidebars)
//                EmiScreenManager.toggleSidebarType(SidebarType.fromName(entry));
//        } else
//            EmiScreenManager.toggleVisibility(false);
//    }
//}