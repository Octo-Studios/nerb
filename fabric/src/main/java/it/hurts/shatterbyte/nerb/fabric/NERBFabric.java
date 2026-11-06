package it.hurts.shatterbyte.nerb.fabric;

import it.hurts.shatterbyte.nerb.common.NERB;
import net.fabricmc.api.ModInitializer;

public final class NERBFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NERB.init();
    }
}
