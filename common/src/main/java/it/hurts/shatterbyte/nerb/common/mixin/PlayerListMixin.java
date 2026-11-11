package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.recipebook.ServerRecipeBookAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow
    public abstract List<ServerPlayer> getPlayers();

    @Inject(method = "reloadResources", at = @At(value = "NEW", target = "net/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket"))
    private void onReloadResources(CallbackInfo ci) {
        for (var player : getPlayers())
            ((ServerRecipeBookAccess) player.getRecipeBook()).nerb$removeStaleRecipes(player);
    }
}
