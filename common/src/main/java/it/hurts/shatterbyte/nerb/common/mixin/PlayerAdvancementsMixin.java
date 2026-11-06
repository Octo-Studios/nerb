package it.hurts.shatterbyte.nerb.common.mixin;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {
    @Inject(method = "registerListeners(Lnet/minecraft/advancements/AdvancementHolder;)V", at = @At("HEAD"), cancellable = true)
    private void onRegisterListeners(AdvancementHolder holder, CallbackInfo ci) {
        var advancement = holder.value();
        var rewards = advancement.rewards();

        if (advancement.display().isEmpty() && !rewards.recipes().isEmpty() && rewards.experience() == 0 && rewards.loot().size() == 0 && rewards.function().isEmpty())
            ci.cancel();
    }
}
