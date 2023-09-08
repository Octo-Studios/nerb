package it.hurts.sskirillss.nerb.fabric;

import it.hurts.sskirillss.nerb.fabriclike.NERBFabricLike;
import net.fabricmc.api.ModInitializer;

public class NERBFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NERBFabricLike.init();
    }
}