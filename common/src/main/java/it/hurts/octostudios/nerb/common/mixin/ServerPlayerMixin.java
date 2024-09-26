package it.hurts.octostudios.nerb.common.mixin;

import it.hurts.octostudios.nerb.common.config.misc.ButtonMode;
import it.hurts.octostudios.nerb.common.init.ConfigRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "awardRecipes", at = @At("HEAD"), cancellable = true)
    public void onAwardRecipes(Collection<Recipe<?>> recipes, CallbackInfoReturnable<Integer> cir) {
        if (ConfigRegistry.CONFIG.getButtonMode() != ButtonMode.DISCOVERED)
            cir.setReturnValue(0);
    }

    @Inject(method = "awardRecipesByKey", at = @At("HEAD"), cancellable = true)
    public void onAwardRecipesByKey(ResourceLocation[] resourceLocations, CallbackInfo ci) {
        ci.cancel();
    }
}