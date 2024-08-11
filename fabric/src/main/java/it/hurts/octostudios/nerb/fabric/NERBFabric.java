package it.hurts.octostudios.nerb.fabric;

import it.hurts.octostudios.nerb.common.NERB;
import net.fabricmc.api.ModInitializer;

public final class NERBFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NERB.init();
    }
}
