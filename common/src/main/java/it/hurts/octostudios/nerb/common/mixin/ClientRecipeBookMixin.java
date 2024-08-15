package it.hurts.octostudios.nerb.common.mixin;

import it.hurts.octostudios.nerb.common.config.misc.ButtonMode;
import it.hurts.octostudios.nerb.common.init.ConfigRegistry;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(method = "setupCollections", at = @At("HEAD"), cancellable = true)
    public void onSetup(Iterable<Recipe<?>> iterable, RegistryAccess registryAccess, CallbackInfo ci) {
        if (ConfigRegistry.CONFIG.getButtonMode() != ButtonMode.DISCOVERED)
            ci.cancel();
    }
}