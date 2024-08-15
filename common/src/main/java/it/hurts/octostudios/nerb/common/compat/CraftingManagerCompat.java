package it.hurts.octostudios.nerb.common.compat;

import dev.architectury.platform.Platform;

public class CraftingManagerCompat {
    private static boolean JEI_LOADED = false;
    private static boolean REI_LOADED = false;
    private static boolean EMI_LOADED = false;

    public static boolean isJEILoaded() {
        return JEI_LOADED;
    }

    public static boolean isREILoaded() {
        return REI_LOADED;
    }

    public static boolean isEMILoaded() {
        return EMI_LOADED;
    }

    public static boolean isAnyLoaded() {
        return isJEILoaded() || isREILoaded() || isEMILoaded();
    }

    public static void setupCommon() {
        JEI_LOADED = Platform.isModLoaded("jei");
        REI_LOADED = Platform.isModLoaded("roughlyenoughitems");
        EMI_LOADED = Platform.isModLoaded("emi");
    }
}