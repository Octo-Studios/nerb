package it.hurts.octostudios.nerb.common.mixin;

import dev.emi.emi.screen.EmiScreenManager;
import it.hurts.octostudios.nerb.common.compat.CraftingManagerCompat;
import it.hurts.octostudios.nerb.common.config.misc.ButtonMode;
import it.hurts.octostudios.nerb.common.init.ConfigRegistry;
import me.shedaniel.rei.api.client.REIRuntime;
import mezz.jei.common.Internal;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Button.class)
public class ButtonMixin {
    @Inject(method = "onPress", at = @At("HEAD"), cancellable = true)
    public void onPress(CallbackInfo ci) {
        if (ConfigRegistry.CONFIG.getButtonMode() != ButtonMode.TOGGLE || !CraftingManagerCompat.isAnyLoaded())
            return;

        Button button = (Button) (Object) this;

        if (button instanceof ImageButton image && image.sprites != null && image.sprites.equals(RecipeBookComponent.RECIPE_BUTTON_SPRITES)) {
            if (CraftingManagerCompat.isJEILoaded())
                Internal.getClientToggleState().toggleOverlayEnabled();

            if (CraftingManagerCompat.isREILoaded())
                REIRuntime.getInstance().toggleOverlayVisible();

            if (CraftingManagerCompat.isEMILoaded())
                EmiScreenManager.toggleVisibility(false);

            ci.cancel();
        }
    }
}