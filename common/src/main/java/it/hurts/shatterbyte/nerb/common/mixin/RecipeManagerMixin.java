package it.hurts.shatterbyte.nerb.common.mixin;

import it.hurts.shatterbyte.nerb.common.init.ConfigRegistry;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeBookRules;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeManagerAccess;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin implements RecipeManagerAccess {
    @Shadow
    public abstract Collection<RecipeHolder<?>> getRecipes();

    @Unique
    private RecipeBookRules nerb$recipeBookRules;

    @Inject(method = {"apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", "replaceRecipes"}, at = @At("RETURN"))
    private void onRecipesChanged(CallbackInfo ci) {
        nerb$invalidateRecipeBookRules();
    }

    @Override
    public RecipeBookRules nerb$getRecipeBookRules() {
        if (nerb$recipeBookRules == null)
            nerb$recipeBookRules = new RecipeBookRules(getRecipes(), ConfigRegistry.SERVER);

        return nerb$recipeBookRules;
    }

    @Override
    public void nerb$invalidateRecipeBookRules() {
        nerb$recipeBookRules = null;
    }
}
