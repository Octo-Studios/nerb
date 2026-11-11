package it.hurts.shatterbyte.nerb.common.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeManagerAccess;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @WrapOperation(method = {"handlePlaceRecipe", "handleRecipeBookSeenRecipePacket"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRecipeFromDisplay(Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;)Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo;"))
    private RecipeManager.ServerDisplayInfo onGetRecipeFromDisplay(RecipeManager manager, RecipeDisplayId id, Operation<RecipeManager.ServerDisplayInfo> original) {
        var display = original.call(manager, id);

        if (display != null && !((RecipeManagerAccess) manager).nerb$getRecipeBookRules().isEnabled(display.parent().id()))
            return null;

        return display;
    }
}
