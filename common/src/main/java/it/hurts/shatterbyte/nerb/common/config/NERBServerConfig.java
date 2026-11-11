package it.hurts.shatterbyte.nerb.common.config;

import it.hurts.octostudios.octolib.modules.config.annotations.Prop;
import it.hurts.octostudios.octolib.modules.config.impl.ConfigSide;
import it.hurts.octostudios.octolib.modules.config.impl.OctoConfig;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NERBServerConfig implements OctoConfig {
    @Prop(comment = """
            Controls the vanilla recipe book on the server. The lists below override this choice.
            DISABLED: Stops recipe unlocking and sending recipes to the book, except for recipeWhitelist. Skips recipe book work for other recipes to reduce server load.
            DISCOVERED: Makes recipes immediately available in the book without tracking each automatic unlock as player progress. Whitelisted recipes unlock normally; blacklisted recipes stay hidden.
            With NERB on the client, buttonMode must be RECIPE_BOOK to open the book and see these recipes.
            """)
    private RecipeBookMode recipeBookMode = RecipeBookMode.DISABLED;

    @Prop(comment = """
            Recipes that bypass NERB: they unlock through normal gameplay, and their unlock progress is always saved.
            In both DISABLED and DISCOVERED, players must unlock these recipes normally before they appear in the book.
            Matches recipe IDs, which can differ from item IDs. Use minecraft:stick for one recipe or regex:minecraft:.*_planks for vanilla recipes whose IDs end in _planks.
            Regex patterns match the entire namespace:path ID. Whitelist takes priority over recipeBlacklist. An empty list means no exceptions.
            """)
    private List<String> recipeWhitelist = new ArrayList<>();

    @Prop(comment = """
            Recipes to keep hidden from the vanilla book, even in DISCOVERED. Normal unlocking of these recipes is also skipped.
            The recipes themselves remain in the game. This list does not filter JEI/REI interfaces.
            Uses the same recipe ID and regex syntax as recipeWhitelist. Whitelist takes priority. An empty list adds no restrictions beyond recipeBookMode.
            """)
    private List<String> recipeBlacklist = new ArrayList<>();

    @Prop(comment = """
            Controls saved recipe progress: the recipes a player has unlocked and those marked as new in the book.
            true: Removes progress outside recipeWhitelist from the player's data when the player is next saved.
            false: Keeps existing progress for later use without NERB. Recipes automatically unlocked by DISCOVERED are not added to it.
            Book settings mean whether the recipe panel is open and whether it shows only recipes the player currently has ingredients for.
            These settings are saved per recipe book type, including modded types that use Minecraft's recipe book settings.
            Settings are always kept in DISCOVERED. In DISABLED, true resets them if no loaded recipe matches recipeWhitelist, even if the list contains entries.
            """)
    private boolean clearRecipeBookData = true;

    @Override
    public ConfigSide getSide() {
        return ConfigSide.SERVER;
    }

    public enum RecipeBookMode {
        DISABLED,
        DISCOVERED
    }
}
