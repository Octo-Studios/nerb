package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import it.hurts.shatterbyte.shatterlib.module.config.ConfigManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ConfigManager.class, remap = false)
public class ConfigManagerMixin {
    @Inject(method = "reload", at = @At("RETURN"))
    private static void onReload(String path, MinecraftServer server, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && server != null && path.equals(ConfigRegistry.SERVER.getPath()))
            ConfigRegistry.reloadRecipeBook(server);
    }
}
