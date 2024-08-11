package it.hurts.octostudios.nerb.common;

import dev.architectury.event.events.common.LifecycleEvent;
import it.hurts.octostudios.nerb.common.init.ConfigRegistry;

public class NERB {
    public static final String MODID = "nerb";

    public static void init() {
        LifecycleEvent.SETUP.register(ConfigRegistry::registerCommon);
    }
}