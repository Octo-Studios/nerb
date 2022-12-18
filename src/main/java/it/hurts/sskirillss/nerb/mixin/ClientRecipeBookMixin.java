package it.hurts.sskirillss.nerb.mixin;

import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.item.crafting.IRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(method = "setupCollections", at = @At("HEAD"), cancellable = true)
    public void onSetup(Iterable<IRecipe<?>> recipe, CallbackInfo ci) {
        ci.cancel();
    }
}