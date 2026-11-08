# Shared recipe display regression

This opt-in NeoForge test mod checks that disabling the recipe book still leaves
cake, bread and pumpkin-pie displays available, that display IDs resolve to the
original recipes, and that `ServerRecipeBook.addRecipes` still returns zero in
DISABLED mode. It repeats these checks after rebuilding recipe metadata.

1. Build with `../../gradlew.bat -p . jar` from this directory (Java 25).
2. Put the test JAR in a disposable Minecraft 26.1.2 / NeoForge 26.1.2.126 server
   with NERB and ShatterLib. Keep `recipeBookMode: "DISABLED"` in
   `config/nerb-server.json5`. Do not use a production world.
3. Run `nerb_recipe_display_test` in the server console after startup.

Expected: 18 checks pass with vanilla recipes. On unpatched NERB 0.6 the test
fails on the missing cake display. This command rebuilds recipe metadata, so use
only the disposable test server. This test mod is not part of the release build.
