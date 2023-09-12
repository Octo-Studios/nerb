package it.hurts.sskirillss.nerb.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "addButton(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", at = @At("HEAD"), cancellable = true)
    public <T extends Widget> void onWidgetAdded(T widget, CallbackInfoReturnable<T> cir) {
        if (!(widget instanceof ImageButton))
            return;

        ImageButton image = (ImageButton) widget;

        if (image.resourceLocation.equals(new ResourceLocation("textures/gui/recipe_button.png")))
            cir.setReturnValue(null);
    }
}