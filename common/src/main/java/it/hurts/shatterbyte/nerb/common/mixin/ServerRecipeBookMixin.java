package it.hurts.shatterbyte.nerb.common.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.hurts.shatterbyte.nerb.common.config.NERBServerConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundRecipeBookSettingsPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.function.Predicate;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin {
    @Unique
    private boolean nerb$discoveringRecipes;

    @Inject(method = "addRecipes", at = @At("HEAD"), cancellable = true)
    public void onAddRecipes(Collection<RecipeHolder<?>> recipes, ServerPlayer player, CallbackInfoReturnable<Integer> cir) {
        if (ConfigRegistry.SERVER.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISABLED)
            cir.setReturnValue(0);
    }

    @Inject(method = "sendInitialRecipeBook", at = @At("HEAD"), cancellable = true)
    public void onSendInitial(ServerPlayer player, CallbackInfo ci) {
        if (ConfigRegistry.SERVER.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISCOVERED) {
            nerb$discoveringRecipes = true;

            try {
                player.awardRecipes(player.level().getServer().getRecipeManager().getRecipes());
            } finally {
                nerb$discoveringRecipes = false;
            }

            return;
        }

        if (ConfigRegistry.SERVER.getRecipeBookMode() != NERBServerConfig.RecipeBookMode.DISABLED)
            return;

        var book = (ServerRecipeBook) (Object) this;

        player.connection.send(new ClientboundRecipeBookSettingsPacket(book.getBookSettings().copy()));

        ci.cancel();
    }

    @Inject(method = "addHighlight", at = @At("HEAD"), cancellable = true)
    private void onAddHighlight(ResourceKey<Recipe<?>> recipe, CallbackInfo ci) {
        if (nerb$discoveringRecipes)
            ci.cancel();
    }

    @WrapOperation(method = "addRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void onSendAddedRecipes(ServerGamePacketListenerImpl connection, Packet<?> packet, Operation<Void> original) {
        if (!nerb$discoveringRecipes)
            original.call(connection, packet);
    }

    @Inject(method = "loadUntrusted", at = @At("HEAD"), cancellable = true)
    public void onLoad(ServerRecipeBook.Packed packed, Predicate<ResourceKey<Recipe<?>>> predicate, CallbackInfo ci) {
        if (ConfigRegistry.SERVER.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISABLED && ConfigRegistry.SERVER.isClearRecipeBookData())
            ci.cancel();
    }
}
