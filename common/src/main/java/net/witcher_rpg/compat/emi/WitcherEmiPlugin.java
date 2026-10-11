package net.witcher_rpg.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import net.jewelry.gems.GemCut;
import net.jewelry.gems.GemCutRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.compat.jewelry.JewelrySocketFeature;
import net.witcher_rpg.compat.jewelry.WitcherGlyphRunestones;
import net.witcher_rpg.recipe.GemCutCraftingRecipe;
import net.witcher_rpg.recipe.GemCutUpgradeRecipe;

public class WitcherEmiPlugin implements EmiPlugin {
    public static final Identifier CATEGORY_ID = Identifier.of(WitcherClassMod.MOD_ID, "glyph_and_runestone_infusion");
    public static EmiStack ICON;
    public static EmiRecipeCategory CATEGORY;

    private static Identifier syntheticId(String path) {
        return Identifier.of(WitcherClassMod.MOD_ID, "/" + path);
    }

    @Override
    public void register(EmiRegistry registry) {
        if (!JewelrySocketFeature.isJewelryPresent()) return;

        var world = MinecraftClient.getInstance().world;
        if (world == null || !GemCutRegistry.isEnabled(world)) return;

        ICON = EmiStack.of(WitcherGlyphRunestones.LESSER_RUNESTONE.item().get());
        CATEGORY = new EmiRecipeCategory(CATEGORY_ID, ICON, ICON, EmiRecipeSorting.compareInputThenOutput());

        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, ICON);

        registry.addEmiStack(EmiStack.of(WitcherGlyphRunestones.GLYPH.item().get()));
        registry.addEmiStack(EmiStack.of(WitcherGlyphRunestones.GREATER_GLYPH.item().get()));
        registry.addEmiStack(EmiStack.of(WitcherGlyphRunestones.RUNESTONE.item().get()));
        registry.addEmiStack(EmiStack.of(WitcherGlyphRunestones.GREATER_RUNESTONE.item().get()));

        var cutRegistry = GemCutRegistry.from(world);

        int infusionAttempted = 0;
        int infusionRegistered = 0;
        for (var entry : GemCutCraftingRecipe.entries().entrySet()) {
            infusionAttempted++;
            try {
                var key = entry.getKey();
                var cutId = entry.getValue();
                var cutEntry = cutRegistry.getEntry(RegistryKey.of(GemCutRegistry.KEY, cutId)).map(e -> (RegistryEntry<GemCut>) e).orElse(null);
                if (cutEntry == null) {
                    WitcherClassMod.LOGGER.warn("EMI: skipping glyph/runestone infusion recipe, cut not found in registry: {}", cutId);
                    continue;
                }

                var recipeId = syntheticId("infusion/" + cutId.getPath());
                registry.addRecipe(new GemCutCraftingEmiRecipe(recipeId, key.baseItem(), key.catalyst(), cutEntry));
                infusionRegistered++;
            } catch (Exception e) {
                WitcherClassMod.LOGGER.error("Failed to register EMI glyph/runestone infusion recipe for {}", entry.getValue(), e);
            }
        }

        int upgradeAttempted = 0;
        int upgradeRegistered = 0;
        for (var entry : GemCutUpgradeRecipe.tierUpgrades().entrySet()) {
            upgradeAttempted++;
            try {
                var fromCutId = entry.getKey();
                var toCutId = entry.getValue();
                var fromCut = cutRegistry.getEntry(RegistryKey.of(GemCutRegistry.KEY, fromCutId)).map(e -> (RegistryEntry<GemCut>) e).orElse(null);
                var toCut = cutRegistry.getEntry(RegistryKey.of(GemCutRegistry.KEY, toCutId)).map(e -> (RegistryEntry<GemCut>) e).orElse(null);
                var fromItem = GemCutUpgradeRecipe.itemForCut(fromCutId);
                var toItem = GemCutUpgradeRecipe.itemForCut(toCutId);
                if (fromCut == null || toCut == null || fromItem == null || toItem == null) {
                    WitcherClassMod.LOGGER.warn("EMI: skipping glyph/runestone upgrade recipe {} -> {}, fromCut={} toCut={} fromItem={} toItem={}",
                            fromCutId, toCutId, fromCut != null, toCut != null, fromItem != null, toItem != null);
                    continue;
                }

                var recipeId = syntheticId("upgrade/" + toCutId.getPath());
                registry.addRecipe(new GemCutUpgradeEmiRecipe(recipeId, fromItem, fromCut, toItem, toCut));
                upgradeRegistered++;
            } catch (Exception e) {
                WitcherClassMod.LOGGER.error("Failed to register EMI glyph/runestone upgrade recipe for {}", entry.getValue(), e);
            }
        }

        WitcherClassMod.LOGGER.info("EMI: registered {}/{} glyph/runestone infusion recipes, {}/{} glyph/runestone upgrade recipes",
                infusionRegistered, infusionAttempted, upgradeRegistered, upgradeAttempted);
    }
}
