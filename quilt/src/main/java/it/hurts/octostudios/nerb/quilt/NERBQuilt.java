package it.hurts.octostudios.nerb.quilt;

import it.hurts.octostudios.nerb.common.NERB;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public final class NERBQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        NERB.init();
    }
}