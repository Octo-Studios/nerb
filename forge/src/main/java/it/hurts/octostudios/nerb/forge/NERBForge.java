package it.hurts.octostudios.nerb.forge;

import it.hurts.octostudios.nerb.common.NERB;
import net.minecraftforge.fml.common.Mod;

@Mod(NERB.MODID)
public final class NERBForge {
    public NERBForge() {
        NERB.init();
    }
}
