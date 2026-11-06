package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.config.NERBServerConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeManagerAccess;
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
import java.util.List;
import java.util.function.Predicate;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin {
    @Inject(method = "addRecipes", at = @At("HEAD"), cancellable = true)
    public void onAddRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer player, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }

    @Inject(method = "removeRecipes", at = @At("HEAD"), cancellable = true)
    private void onRemoveRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer player, CallbackInfoReturnable<Integer> cir) {
        if (ConfigRegistry.SERVER.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISCOVERED)
            cir.setReturnValue(0);
    }

    @Inject(method = "contains", at = @At("HEAD"), cancellable = true)
    private void onContains(ResourceKey<Recipe<?>> recipe, CallbackInfoReturnable<Boolean> cir) {
        if (ConfigRegistry.SERVER.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISCOVERED)
            cir.setReturnValue(true);
    }

    @Inject(method = "sendInitialRecipeBook", at = @At("HEAD"), cancellable = true)
    public void onSendInitial(ServerPlayer player, CallbackInfo ci) {
        var book = (ServerRecipeBook) (Object) this;

        player.connection.send(new ClientboundRecipeBookSettingsPacket(book.getBookSettings().copy()));

        if (ConfigRegistry.SERVER.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISCOVERED)
            player.connection.send(((RecipeManagerAccess) player.level().getServer().getRecipeManager()).nerb$getDiscoveredRecipes());

        ci.cancel();
    }

    @Inject(method = "pack", at = @At("HEAD"), cancellable = true)
    private void onPack(CallbackInfoReturnable<ServerRecipeBook.Packed> cir) {
        if (ConfigRegistry.SERVER.getRecipeBookMode() != NERBServerConfig.RecipeBookMode.DISCOVERED)
            return;

        var book = (ServerRecipeBook) (Object) this;

        cir.setReturnValue(new ServerRecipeBook.Packed(book.getBookSettings().copy(), List.of(), List.of()));
    }

    @Inject(method = "loadUntrusted", at = @At("HEAD"), cancellable = true)
    public void onLoad(ServerRecipeBook.Packed packed, Predicate<ResourceKey<Recipe<?>>> predicate, CallbackInfo ci) {
        if (ConfigRegistry.SERVER.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISCOVERED) {
            ((ServerRecipeBook) (Object) this).setBookSettings(packed.settings());

            ci.cancel();

            return;
        }

        if (ConfigRegistry.SERVER.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISABLED && ConfigRegistry.SERVER.isClearRecipeBookData())
            ci.cancel();
    }
}
