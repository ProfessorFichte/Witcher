package net.witcher_rpg.recipe;

import com.google.common.base.Suppliers;
import net.jewelry.gems.GemComponents;
import net.jewelry.gems.GemCut;
import net.jewelry.gems.GemCutRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.compat.jewelry.JewelrySocketFeature;
import net.witcher_rpg.compat.jewelry.WitcherGlyphRunestoneCuts;
import net.witcher_rpg.compat.jewelry.WitcherGlyphRunestones;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class GemCutUpgradeRecipe extends SpecialCraftingRecipe {
    private static final int REQUIRED_INPUTS = 4;

    private static final Supplier<Map<Item, Item>> NEXT_TIER_ITEM = Suppliers.memoize(() -> Map.of(
            WitcherGlyphRunestones.LESSER_GLYPH_OF_INFUSION.item().get(), WitcherGlyphRunestones.GLYPH.item().get(),
            WitcherGlyphRunestones.GLYPH.item().get(), WitcherGlyphRunestones.GREATER_GLYPH.item().get(),
            WitcherGlyphRunestones.LESSER_RUNESTONE.item().get(), WitcherGlyphRunestones.RUNESTONE.item().get(),
            WitcherGlyphRunestones.RUNESTONE.item().get(), WitcherGlyphRunestones.GREATER_RUNESTONE.item().get()
    ));

    private static final Supplier<Map<Identifier, Identifier>> NEXT_TIER_CUT = Suppliers.memoize(GemCutUpgradeRecipe::buildTierMap);

    public GemCutUpgradeRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    private static final Supplier<Map<Identifier, Item>> CUT_ITEM = Suppliers.memoize(GemCutUpgradeRecipe::buildCutItemMap);

    public static Map<Identifier, Identifier> tierUpgrades() {
        return NEXT_TIER_CUT.get();
    }

    public static Item itemForCut(Identifier cutId) {
        return CUT_ITEM.get().get(cutId);
    }

    private static Map<Identifier, Item> buildCutItemMap() {
        var result = new HashMap<Identifier, Item>();
        for (var cut : WitcherGlyphRunestoneCuts.CUTS) {
            var id = cut.id();
            var path = id.getPath();
            Item item;
            if (path.startsWith("lesser_")) {
                item = cut.glyph() ? WitcherGlyphRunestones.LESSER_GLYPH_OF_INFUSION.item().get() : WitcherGlyphRunestones.LESSER_RUNESTONE.item().get();
            } else if (path.startsWith("greater_")) {
                item = cut.glyph() ? WitcherGlyphRunestones.GREATER_GLYPH.item().get() : WitcherGlyphRunestones.GREATER_RUNESTONE.item().get();
            } else {
                item = cut.glyph() ? WitcherGlyphRunestones.GLYPH.item().get() : WitcherGlyphRunestones.RUNESTONE.item().get();
            }
            result.put(id, item);
        }
        return result;
    }

    private static Map<Identifier, Identifier> buildTierMap() {
        var byBase = new LinkedHashMap<String, Identifier[]>();
        for (var cut : WitcherGlyphRunestoneCuts.CUTS) {
            var id = cut.id();
            var path = id.getPath();
            String base;
            int tier;
            if (path.startsWith("lesser_")) {
                base = path.substring("lesser_".length());
                tier = 0;
            } else if (path.startsWith("greater_")) {
                base = path.substring("greater_".length());
                tier = 2;
            } else {
                base = path;
                tier = 1;
            }
            byBase.computeIfAbsent(base, key -> new Identifier[3])[tier] = id;
        }

        var result = new HashMap<Identifier, Identifier>();
        for (var tiers : byBase.values()) {
            if (tiers[0] != null && tiers[1] != null) result.put(tiers[0], tiers[1]);
            if (tiers[1] != null && tiers[2] != null) result.put(tiers[1], tiers[2]);
        }

        var knownIds = new HashSet<Identifier>();
        for (var cut : WitcherGlyphRunestoneCuts.CUTS) knownIds.add(cut.id());
        for (var entry : result.entrySet()) {
            var expected = deriveByStringSurgery(entry.getKey(), knownIds);
            if (expected == null || !expected.equals(entry.getValue())) {
                WitcherClassMod.LOGGER.warn(
                        "Gem cut tier map mismatch for {}: grouped -> {}, string-derived -> {}",
                        entry.getKey(), entry.getValue(), expected);
            }
        }
        return result;
    }

    private static Identifier deriveByStringSurgery(Identifier cutId, Set<Identifier> knownIds) {
        var path = cutId.getPath();
        Identifier candidate;
        if (path.startsWith("lesser_")) {
            candidate = Identifier.of(cutId.getNamespace(), path.substring("lesser_".length()));
        } else if (!path.startsWith("greater_")) {
            candidate = Identifier.of(cutId.getNamespace(), "greater_" + path);
        } else {
            return null;
        }
        return knownIds.contains(candidate) ? candidate : null;
    }

    private record Resolution(Item resultItem, Identifier nextCutId) {
    }

    /**
     * A plain {@code Ingredient} match (vanilla's own {@link net.minecraft.recipe.Ingredient#test})
     * only ever compares {@code ItemStack.getItem()} and ignores data components, so any
     * data-driven "4 raw items -> next tier" recipe would match cut and uncut stacks identically.
     * This recipe therefore has to subsume that plain upgrade case too (falling back to an
     * uncut result when the four inputs don't share one cut) rather than coexist with a
     * separate JSON recipe for the same four-identical-item shape: two crafting-table recipes
     * that both match the same input resolve to whichever one {@code RecipeManager} happens to
     * iterate first, which is load-order dependent and silently drops the cut in practice.
     */
    private static Resolution resolve(CraftingRecipeInput input) {
        Item commonItem = null;
        RegistryEntry<GemCut> commonCut = null;
        boolean cutMismatch = false;
        int filled = 0;

        for (var stack : input.getStacks()) {
            if (stack.isEmpty()) continue;
            filled++;
            if (filled > REQUIRED_INPUTS) return null;

            if (commonItem == null) {
                commonItem = stack.getItem();
            } else if (commonItem != stack.getItem()) {
                return null;
            }

            var cut = GemCut.of(stack).orElse(null);
            if (cut == null) {
                cutMismatch = true;
            } else if (commonCut == null) {
                commonCut = cut;
            } else if (!commonCut.equals(cut)) {
                cutMismatch = true;
            }
        }

        if (filled != REQUIRED_INPUTS || commonItem == null) return null;

        var resultItem = NEXT_TIER_ITEM.get().get(commonItem);
        if (resultItem == null) return null;

        if (cutMismatch || commonCut == null) {
            return new Resolution(resultItem, null);
        }

        var cutId = GemCut.idOf(commonCut).orElse(null);
        if (cutId == null) {
            return new Resolution(resultItem, null);
        }

        var nextCutId = NEXT_TIER_CUT.get().get(cutId);
        return new Resolution(resultItem, nextCutId);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (!JewelrySocketFeature.isJewelryPresent()) return false;
        return resolve(input) != null;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        if (!JewelrySocketFeature.isJewelryPresent()) return ItemStack.EMPTY;
        var resolution = resolve(input);
        if (resolution == null) return ItemStack.EMPTY;

        var output = new ItemStack(resolution.resultItem());
        if (resolution.nextCutId() != null) {
            var cutEntry = resolveCut(resolution.nextCutId(), lookup);
            if (cutEntry != null) {
                output.set(GemComponents.CUT, cutEntry);
            }
        }
        return output;
    }

    private static RegistryEntry<GemCut> resolveCut(Identifier cutId, RegistryWrapper.WrapperLookup lookup) {
        return GemCutRegistry.from(lookup)
                .flatMap(registry -> registry.getOptional(RegistryKey.of(GemCutRegistry.KEY, cutId)))
                .map(reference -> (RegistryEntry<GemCut>) reference)
                .orElse(null);
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= REQUIRED_INPUTS;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WitcherRecipes.GEM_CUT_UPGRADE_SERIALIZER;
    }
}
