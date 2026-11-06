package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.recipebook.RecipeManagerAccess;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {
    @Shadow
    private ServerPlayer player;

    @Inject(method = "registerListeners(Lnet/minecraft/advancements/AdvancementHolder;)V", at = @At("HEAD"), cancellable = true)
    private void onRegisterListeners(AdvancementHolder holder, CallbackInfo ci) {
        var advancement = holder.value();
        var rewards = advancement.rewards();

        var rules = ((RecipeManagerAccess) player.level().getServer().getRecipeManager()).nerb$getRecipeBookRules();

        if (advancement.display().isEmpty() && !rewards.recipes().isEmpty() && rewards.experience() == 0 && rewards.loot().size() == 0 && rewards.function().isEmpty()
                && rewards.recipes().stream().noneMatch(rules::isVanilla))
            ci.cancel();
    }
}
