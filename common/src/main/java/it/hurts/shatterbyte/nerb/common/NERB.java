package it.hurts.shatterbyte.nerb.common;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.CraftingManagerCompat;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;

public class NERB {
    public static final String MODID = "nerb";

    public static void init() {
        LifecycleEvent.SETUP.register(() -> {
            ConfigRegistry.registerCommon();
            CraftingManagerCompat.setupCommon(Platform::isModLoaded);
        });
    }
}
