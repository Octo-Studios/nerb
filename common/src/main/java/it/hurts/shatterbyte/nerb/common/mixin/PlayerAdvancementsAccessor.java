package it.hurts.shatterbyte.nerb.common.mixin;

import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerAdvancements.class)
public interface PlayerAdvancementsAccessor {
    @Invoker("registerListeners")
    void nerb$registerListeners(ServerAdvancementManager manager);
}
