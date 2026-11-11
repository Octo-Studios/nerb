package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.octostudios.octolib.modules.config.ConfigManager;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ConfigManager.class, remap = false)
public class ConfigManagerMixin {
    @Inject(target = @Desc(value = "syncConfig", args = {String.class, MinecraftServer.class}), at = @At("RETURN"))
    private static void onSyncConfig(String path, MinecraftServer server, CallbackInfo ci) {
        if (path.equals(ConfigRegistry.SERVER_PATH))
            ConfigRegistry.reloadRecipeBook(server);
    }
}
