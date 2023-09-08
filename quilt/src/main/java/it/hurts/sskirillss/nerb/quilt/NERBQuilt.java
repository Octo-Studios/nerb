package it.hurts.sskirillss.nerb.quilt;

import it.hurts.sskirillss.nerb.fabriclike.NERBFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class NERBQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        NERBFabricLike.init();
    }
}