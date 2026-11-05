package it.hurts.shatterbyte.nerb.common;

import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;

public class NERB {
    public static final String MODID = "nerb";

    public static void init() {
        ConfigRegistry.registerCommon();
    }
}
