package it.hurts.sskirillss.nerb.mixin;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "addButton", at = @At("HEAD"), cancellable = true)
    public <T extends AbstractWidget> void onWidgetAdded(T widget, CallbackInfoReturnable<T> cir) {
        if (!(widget instanceof ImageButton))
            return;

        ImageButton image = (ImageButton) widget;

        if (image.resourceLocation.equals(new ResourceLocation("textures/gui/recipe_button.png")))
            cir.setReturnValue(null);
    }
}