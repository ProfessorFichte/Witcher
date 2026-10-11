package net.witcher_rpg.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.witcher_rpg.compat.jewelry.JewelrySocketFeature;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherRecipes {
    public static RecipeSerializer<GemCutUpgradeRecipe> GEM_CUT_UPGRADE_SERIALIZER;
    public static RecipeSerializer<GemCutCraftingRecipe> GEM_CUT_CRAFTING_SERIALIZER;

    public static void register() {
        if (!JewelrySocketFeature.isJewelryPresent()) return;

        var upgradeId = Identifier.of(MOD_ID, "gem_cut_upgrade");
        GEM_CUT_UPGRADE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, upgradeId,
                new SpecialRecipeSerializer<>(GemCutUpgradeRecipe::new));

        var craftingId = Identifier.of(MOD_ID, "gem_cut_crafting");
        GEM_CUT_CRAFTING_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, craftingId,
                new SpecialRecipeSerializer<>(GemCutCraftingRecipe::new));
    }
}
