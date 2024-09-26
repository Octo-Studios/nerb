package it.hurts.octostudios.nerb.common.mixin;

import it.hurts.octostudios.nerb.common.compat.craftingmanager.CraftingManagerCompat;
import it.hurts.octostudios.nerb.common.compat.craftingmanager.impl.base.ICMEntry;
import it.hurts.octostudios.nerb.common.config.misc.ButtonMode;
import it.hurts.octostudios.nerb.common.init.ConfigRegistry;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Button.class)
public class ButtonMixin {
    @Unique
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");

    @Inject(method = "onPress", at = @At("HEAD"), cancellable = true)
    public void onPress(CallbackInfo ci) {
        if (ConfigRegistry.CONFIG.getButtonMode() != ButtonMode.TOGGLE || !CraftingManagerCompat.isAnyLoaded())
            return;

        Button button = (Button) (Object) this;

        if (button instanceof ImageButton image && image.resourceLocation != null && image.resourceLocation.equals(TEXTURE)) {
            for (ICMEntry entry : CraftingManagerCompat.ENTRIES.values())
                entry.toggleVisibility();

            ci.cancel();
        }
    }
}