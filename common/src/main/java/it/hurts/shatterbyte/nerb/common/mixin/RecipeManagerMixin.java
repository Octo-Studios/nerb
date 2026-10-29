package it.hurts.shatterbyte.nerb.common.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.hurts.shatterbyte.nerb.common.config.NERBConfig;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @WrapOperation(method = "finalizeRecipeLoading", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;unpackRecipeInfo(Ljava/lang/Iterable;Lnet/minecraft/world/flag/FeatureFlagSet;)Ljava/util/List;"))
    private List<RecipeManager.ServerDisplayInfo> onUnpackRecipeInfo(Iterable<RecipeHolder<?>> recipes, FeatureFlagSet enabledFeatures, Operation<List<RecipeManager.ServerDisplayInfo>> original) {
        if (ConfigRegistry.GENERAL.getButtonMode() != NERBConfig.ButtonMode.DISCOVERED)
            return List.of();

        return original.call(recipes, enabledFeatures);
    }
}
