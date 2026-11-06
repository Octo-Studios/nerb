package it.hurts.shatterbyte.nerb.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeBookRules;
import it.hurts.shatterbyte.nerb.common.recipebook.RecipeManagerAccess;
import it.hurts.shatterbyte.nerb.common.recipebook.ServerRecipeBookAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @Unique
    private static final Codec<ServerRecipeBook.Packed> nerb$discoveredRecipeBookCodec = RecipeBookSettings.MAP_CODEC.codec()
            .xmap(settings -> new ServerRecipeBook.Packed(settings, List.of(), List.of()), ServerRecipeBook.Packed::settings);

    @WrapOperation(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/stats/ServerRecipeBook"))
    private ServerRecipeBook onCreateRecipeBook(ServerRecipeBook.DisplayResolver displayResolver, Operation<ServerRecipeBook> original) {
        var book = original.call(displayResolver);

        ((ServerRecipeBookAccess) book).nerb$setServer(server);

        return book;
    }

    @Inject(method = "awardRecipes", at = @At("HEAD"), cancellable = true)
    public void onAwardRecipes(Collection<RecipeHolder<?>> holders, CallbackInfoReturnable<Integer> cir) {
        if (!nerb$getRecipeBookRules().hasVanillaRecipes())
            cir.setReturnValue(0);
    }

    @Inject(method = "awardRecipesByKey", at = @At("HEAD"), cancellable = true)
    public void onAwardRecipesByKey(List<ResourceKey<Recipe<?>>> recipes, CallbackInfo ci) {
        if (!nerb$getRecipeBookRules().hasVanillaRecipes())
            ci.cancel();
    }

    @ModifyExpressionValue(method = "readAdditionalSaveData", at = @At(value = "FIELD", target = "Lnet/minecraft/stats/ServerRecipeBook$Packed;CODEC:Lcom/mojang/serialization/Codec;"))
    private Codec<ServerRecipeBook.Packed> onRecipeBookCodec(Codec<ServerRecipeBook.Packed> original) {
        var rules = nerb$getRecipeBookRules();

        return rules.isDiscoveredMode() && !rules.hasVanillaRecipes() ? nerb$discoveredRecipeBookCodec : original;
    }

    @Unique
    private RecipeBookRules nerb$getRecipeBookRules() {
        return ((RecipeManagerAccess) server.getRecipeManager()).nerb$getRecipeBookRules();
    }
}
