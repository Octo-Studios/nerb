package it.hurts.shatterbyte.nerb.common.recipebook;

public interface RecipeManagerAccess {
    RecipeBookRules nerb$getRecipeBookRules();

    void nerb$invalidateRecipeBookRules();
}
