package it.hurts.shatterbyte.nerb.neoforge;

import it.hurts.shatterbyte.nerb.common.NERB;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(NERB.MODID)
public final class NERBNeoForge {
    public NERBNeoForge(IEventBus modBus) {
        NERB.init();
    }
}
