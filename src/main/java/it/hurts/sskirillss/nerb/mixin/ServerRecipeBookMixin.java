package it.hurts.sskirillss.nerb.mixin;

import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.item.crafting.ServerRecipeBook;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin {
    @Inject(method = "toNbt", at = @At("HEAD"), cancellable = true)
    public void onSave(CallbackInfoReturnable<CompoundNBT> cir) {
        cir.setReturnValue(new CompoundNBT());
    }

    @Inject(method = "fromNbt", at = @At("HEAD"), cancellable = true)
    public void onLoad(CompoundNBT tag, RecipeManager manager, CallbackInfo ci) {
        ci.cancel();
    }
}