package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.config.NERBConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.network.protocol.game.ClientboundRecipeBookSettingsPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.function.Predicate;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin {
    @Inject(method = "addRecipes", at = @At("HEAD"), cancellable = true)
    public void onAddRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer player, CallbackInfoReturnable<Integer> cir) {
        if (ConfigRegistry.GENERAL.getButtonMode() != NERBConfig.ButtonMode.DISCOVERED)
            cir.setReturnValue(0);
    }

    @Inject(method = "sendInitialRecipeBook", at = @At("HEAD"), cancellable = true)
    public void onSendInitial(ServerPlayer player, CallbackInfo ci) {
        if (ConfigRegistry.GENERAL.getButtonMode() == NERBConfig.ButtonMode.DISCOVERED)
            return;

        var book = (ServerRecipeBook) (Object) this;

        player.connection.send(new ClientboundRecipeBookSettingsPacket(book.getBookSettings().copy()));

        ci.cancel();
    }

    @Inject(method = "loadUntrusted", at = @At("HEAD"), cancellable = true)
    public void onLoad(ServerRecipeBook.Packed packed, Predicate<ResourceKey<Recipe<?>>> predicate, CallbackInfo ci) {
        if (ConfigRegistry.GENERAL.getButtonMode() != NERBConfig.ButtonMode.DISCOVERED)
            ci.cancel();
    }
}