package it.hurts.octostudios.nerb.common.mixin;

import it.hurts.octostudios.nerb.common.compat.craftingmanager.CraftingManagerCompat;
import it.hurts.octostudios.nerb.common.config.NERBConfig;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static it.hurts.octostudios.nerb.common.init.ConfigRegistry.GENERAL;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "addRenderableWidget", at = @At("HEAD"), cancellable = true)
    public <T extends GuiEventListener & Renderable & NarratableEntry> void onWidgetAdded(T widget, CallbackInfoReturnable<T> cir) {
        if ((GENERAL.getButtonMode() == NERBConfig.ButtonMode.DISABLED || (GENERAL.getButtonMode() == NERBConfig.ButtonMode.TOGGLE && !CraftingManagerCompat.isAnyLoaded()))
                && widget instanceof ImageButton image && image.sprites != null && image.sprites.equals(RecipeBookComponent.RECIPE_BUTTON_SPRITES)) {
            cir.setReturnValue(null);
        }
    }
}