package it.hurts.shatterbyte.nerb.common.compat.craftingmanager;

import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.impl.JEIEntry;
import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.impl.base.ICMEntry;

import java.util.HashMap;
import java.util.function.Predicate;

public class CraftingManagerCompat {
    public static HashMap<String, ICMEntry> ENTRIES = new HashMap<>();

    public static boolean isJEILoaded() {
        return ENTRIES.containsKey("jei");
    }

//    public static boolean isREILoaded() {
//        return ENTRIES.containsKey("roughlyenoughitems");
//    }

//    public static boolean isEMILoaded() {
//        return ENTRIES.containsKey("emi");
//    }

    public static boolean isAnyLoaded() {
        return isJEILoaded();
    }

    public static void setupCommon(Predicate<String> isModLoaded) {
        if (isModLoaded.test("jei"))
            ENTRIES.put("jei", new JEIEntry());

//        if (Platform.isModLoaded("roughlyenoughitems"))
//            ENTRIES.put("roughlyenoughitems", new REIEntry());

//        if (Platform.isModLoaded("emi"))
//            ENTRIES.put("emi", new EMIEntry());
    }
}
