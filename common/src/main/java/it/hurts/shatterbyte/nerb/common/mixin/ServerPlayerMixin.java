package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.config.NERBConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "awardRecipes", at = @At("HEAD"), cancellable = true)
    public void onAwardRecipes(Collection<RecipeHolder<?>> holders, CallbackInfoReturnable<Integer> cir) {
        if (ConfigRegistry.GENERAL.getButtonMode() != NERBConfig.ButtonMode.DISCOVERED)
            cir.setReturnValue(0);
    }

    @Inject(method = "awardRecipesByKey", at = @At("HEAD"), cancellable = true)
    public void onAwardRecipesByKey(List<Identifier> recipes, CallbackInfo ci) {
        ci.cancel();
    }
}