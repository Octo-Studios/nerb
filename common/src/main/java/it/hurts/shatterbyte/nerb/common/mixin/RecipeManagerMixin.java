package it.hurts.shatterbyte.nerb.common.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeBookRules;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeManagerAccess;
import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.StreamSupport;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin implements RecipeManagerAccess {
    @Unique
    private RecipeBookRules nerb$recipeBookRules;

    @Unique
    private ClientboundRecipeBookAddPacket nerb$discoveredRecipes;

    @WrapOperation(method = "finalizeRecipeLoading", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;unpackRecipeInfo(Ljava/lang/Iterable;Lnet/minecraft/world/flag/FeatureFlagSet;)Ljava/util/List;"))
    private List<RecipeManager.ServerDisplayInfo> onUnpackRecipeInfo(Iterable<RecipeHolder<?>> recipes, FeatureFlagSet enabledFeatures, Operation<List<RecipeManager.ServerDisplayInfo>> original) {
        nerb$recipeBookRules = new RecipeBookRules(recipes, ConfigRegistry.SERVER);

        if (!nerb$recipeBookRules.isDiscoveredMode() && !nerb$recipeBookRules.hasVanillaRecipes())
            return List.of();

        var enabledRecipes = StreamSupport.stream(recipes.spliterator(), false)
                .filter(recipe -> nerb$recipeBookRules.isEnabled(recipe.id()))
                .toList();
        var displays = original.call(enabledRecipes, enabledFeatures);

        nerb$discoveredRecipes = new ClientboundRecipeBookAddPacket(displays.stream()
                .filter(display -> !display.parent().value().isSpecial() && nerb$recipeBookRules.isDiscovered(display.parent().id()))
                .map(display -> new ClientboundRecipeBookAddPacket.Entry(display.display(), false, false))
                .toList(), !nerb$recipeBookRules.hasVanillaRecipes());

        return displays;
    }

    @Override
    public RecipeBookRules nerb$getRecipeBookRules() {
        return nerb$recipeBookRules;
    }

    @Override
    public ClientboundRecipeBookAddPacket nerb$getDiscoveredRecipes() {
        return nerb$discoveredRecipes;
    }
}
