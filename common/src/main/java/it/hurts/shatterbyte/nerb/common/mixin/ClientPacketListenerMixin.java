package it.hurts.shatterbyte.nerb.common.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.hurts.shatterbyte.nerb.common.config.NERBClientConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.SessionSearchTrees;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleAddOrRemoveRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/ClientRecipeBook;setBookSettings(Lnet/minecraft/stats/RecipeBookSettings;)V", shift = At.Shift.AFTER), cancellable = true)
    private void onRecipeBookUpdate(CallbackInfo ci) {
        if (ConfigRegistry.CLIENT.getButtonMode() != NERBClientConfig.ButtonMode.RECIPE_BOOK)
            ci.cancel();
    }

    @WrapOperation(method = "handleUpdateRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/SessionSearchTrees;updateRecipes(Lnet/minecraft/client/ClientRecipeBook;Lnet/minecraft/core/RegistryAccess$Frozen;)V"))
    private void onUpdateRecipeSearch(SessionSearchTrees searchTrees, ClientRecipeBook book, RegistryAccess.Frozen registries, Operation<Void> original) {
        if (ConfigRegistry.CLIENT.getButtonMode() == NERBClientConfig.ButtonMode.RECIPE_BOOK)
            original.call(searchTrees, book, registries);
    }
}
