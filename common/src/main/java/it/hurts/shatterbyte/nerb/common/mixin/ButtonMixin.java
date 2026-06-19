package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.CraftingManagerCompat;
import it.hurts.shatterbyte.nerb.common.compat.craftingmanager.impl.base.ICMEntry;
import it.hurts.shatterbyte.nerb.common.config.NERBConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
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
        if (ConfigRegistry.GENERAL.getButtonMode() != NERBConfig.ButtonMode.TOGGLE || !CraftingManagerCompat.isAnyLoaded())
            return;

        Button button = (Button) (Object) this;

        if (button instanceof ImageButton image && image.sprites != null && image.sprites.equals(RecipeBookComponent.RECIPE_BUTTON_SPRITES)) {
            for (ICMEntry entry : CraftingManagerCompat.ENTRIES.values())
                entry.toggleVisibility();

            ci.cancel();
        }
    }
}