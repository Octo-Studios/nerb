package it.hurts.shatterbyte.nerb.test;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** Opt-in integration test, never included in NERB's release artifacts. */
@Mod(value = "nerb_recipe_display_test", dist = Dist.DEDICATED_SERVER)
public final class RecipeDisplayTestMod {
    public RecipeDisplayTestMod() {
        NeoForge.EVENT_BUS.addListener(this::register);
    }

    private void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("nerb_recipe_display_test")
                .requires(source -> source.getEntity() == null)
                .executes(context -> {
                    var server = context.getSource().getServer();
                    int checks = RecipeDisplayRegression.verify(server);
                    server.getRecipeManager().finalizeRecipeLoading(server.overworld().enabledFeatures());
                    checks += RecipeDisplayRegression.verify(server);
                    int passed = checks;
                    context.getSource().sendSuccess(() -> Component.literal("Recipe display regression passed: " + passed + " checks"), false);
                    return checks;
                }));
    }
}
