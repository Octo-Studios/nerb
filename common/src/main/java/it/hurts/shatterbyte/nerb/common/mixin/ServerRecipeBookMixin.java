package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.config.NERBServerConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundRecipePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin extends RecipeBook {
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

        player.connection.send(new ClientboundRecipePacket(ClientboundRecipePacket.State.INIT, List.of(), List.of(), getBookSettings()));

        ci.cancel();
    }

    @Override
    protected void addHighlight(ResourceLocation recipe) {
        if (!nerb$discoveringRecipes)
            super.addHighlight(recipe);
    }

    @Inject(method = "sendRecipes", at = @At("HEAD"), cancellable = true)
    private void onSendRecipes(CallbackInfo ci) {
        if (nerb$discoveringRecipes || ConfigRegistry.SERVER.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISABLED)
            ci.cancel();
    }

    @Inject(method = "fromNbt", at = @At("HEAD"), cancellable = true)
    public void onLoad(CompoundTag tag, RecipeManager recipeManager, CallbackInfo ci) {
        if (ConfigRegistry.SERVER.getRecipeBookMode() == NERBServerConfig.RecipeBookMode.DISABLED && ConfigRegistry.SERVER.isClearRecipeBookData())
            ci.cancel();
    }
}
