package it.hurts.octostudios.nerb.common.mixin;

import it.hurts.octostudios.nerb.common.compat.craftingmanager.CraftingManagerCompat;
import it.hurts.octostudios.nerb.common.config.misc.ButtonMode;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static it.hurts.octostudios.nerb.common.init.ConfigRegistry.CONFIG;

@Mixin(Screen.class)
public class ScreenMixin {
    @Unique
    private static final ResourceLocation nerb$TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");

    @Inject(method = "addRenderableWidget", at = @At("HEAD"), cancellable = true)
    public <T extends GuiEventListener & Renderable & NarratableEntry> void onWidgetAdded(T widget, CallbackInfoReturnable<T> cir) {
        if ((CONFIG.getButtonMode() == ButtonMode.DISABLED || (CONFIG.getButtonMode() == ButtonMode.TOGGLE && !CraftingManagerCompat.isAnyLoaded()))
                && widget instanceof ImageButton image && image.resourceLocation != null && image.resourceLocation.equals(nerb$TEXTURE)) {
            cir.setReturnValue(null);
        }
    }
}