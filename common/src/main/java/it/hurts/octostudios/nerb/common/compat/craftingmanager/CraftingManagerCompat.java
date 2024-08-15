package it.hurts.octostudios.nerb.common.compat.craftingmanager;

import dev.architectury.platform.Platform;
import it.hurts.octostudios.nerb.common.compat.craftingmanager.impl.EMIEntry;
import it.hurts.octostudios.nerb.common.compat.craftingmanager.impl.JEIEntry;
import it.hurts.octostudios.nerb.common.compat.craftingmanager.impl.REIEntry;
import it.hurts.octostudios.nerb.common.compat.craftingmanager.impl.base.ICMEntry;

import java.util.HashMap;

public class CraftingManagerCompat {
    public static HashMap<String, ICMEntry> ENTRIES = new HashMap<>();

    public static boolean isJEILoaded() {
        return ENTRIES.containsKey("jei");
    }

    public static boolean isREILoaded() {
        return ENTRIES.containsKey("roughlyenoughitems");
    }

    public static boolean isEMILoaded() {
        return ENTRIES.containsKey("emi");
    }

    public static boolean isAnyLoaded() {
        return isJEILoaded() || isREILoaded() || isEMILoaded();
    }

    public static void setupCommon() {
        if (Platform.isModLoaded("jei"))
            ENTRIES.put("jei", new JEIEntry());

        if (Platform.isModLoaded("roughlyenoughitems"))
            ENTRIES.put("roughlyenoughitems", new REIEntry());

        if (Platform.isModLoaded("emi"))
            ENTRIES.put("emi", new EMIEntry());
    }
}