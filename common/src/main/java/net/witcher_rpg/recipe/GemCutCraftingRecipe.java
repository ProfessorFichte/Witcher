package net.witcher_rpg.recipe;

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
import net.witcher_rpg.compat.jewelry.WitcherGlyphRunestones;

import java.util.HashMap;
import java.util.Map;

/**
 * Crafts a cut directly onto a raw item using a base item + a themed catalyst item,
 * entirely bypassing Jewelry's own Gem Cutting Table. Entries are a simple lookup table
 * keyed by (base item, catalyst item); add more via {@link #register} as the full
 * glyph/runestone catalyst list gets decided.
 */
public class GemCutCraftingRecipe extends SpecialCraftingRecipe {
    public record Key(Item baseItem, Item catalyst) {
    }

    private static final Map<Key, Identifier> RECIPES = new HashMap<>();

    public static void register(Item baseItem, Item catalyst, Identifier cutId) {
        RECIPES.put(new Key(baseItem, catalyst), cutId);
    }

    public static Map<Key, Identifier> entries() {
        return Map.copyOf(RECIPES);
    }

    static {
        var glyph = WitcherGlyphRunestones.LESSER_GLYPH_OF_INFUSION.item().get();
        var runestone = WitcherGlyphRunestones.LESSER_RUNESTONE.item().get();
        var modId = WitcherClassMod.MOD_ID;

        register(glyph, net.minecraft.item.Items.LAPIS_LAZULI, Identifier.of(modId, "lesser_aard_glyph"));
        register(glyph, net.minecraft.item.Items.GLOW_INK_SAC, Identifier.of(modId, "lesser_axii_glyph"));
        register(glyph, net.minecraft.item.Items.BLAZE_POWDER, Identifier.of(modId, "lesser_igni_glyph"));
        register(glyph, net.minecraft.item.Items.TURTLE_SCUTE, Identifier.of(modId, "lesser_quen_glyph"));
        register(glyph, net.minecraft.item.Items.AMETHYST_SHARD, Identifier.of(modId, "lesser_yrden_glyph"));

        register(runestone, net.minecraft.item.Items.NETHERITE_SCRAP, Identifier.of(modId, "lesser_chernobog_runestone"));
        register(runestone, net.minecraft.item.Items.PUFFERFISH, Identifier.of(modId, "lesser_morana_runestone"));
        register(runestone, net.minecraft.item.Items.MAGMA_CREAM, Identifier.of(modId, "lesser_dazhbog_runestone"));
        register(runestone, net.minecraft.item.Items.SPIDER_EYE, Identifier.of(modId, "lesser_devana_runestone"));
        register(runestone, net.minecraft.item.Items.RABBIT_FOOT, Identifier.of(modId, "lesser_perun_runestone"));
        register(runestone, net.minecraft.item.Items.GUNPOWDER, Identifier.of(modId, "lesser_stribog_runestone"));
        register(runestone, net.minecraft.item.Items.ENDER_PEARL, Identifier.of(modId, "lesser_svarog_runestone"));
        register(runestone, net.minecraft.item.Items.ECHO_SHARD, Identifier.of(modId, "lesser_triglav_runestone"));
        register(runestone, net.minecraft.item.Items.AMETHYST_SHARD, Identifier.of(modId, "lesser_veles_runestone"));
        register(runestone, net.minecraft.item.Items.PRISMARINE_CRYSTALS, Identifier.of(modId, "lesser_zoria_runestone"));
    }

    public GemCutCraftingRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    private record Resolution(Item resultItem, Identifier cutId) {
    }

    private static boolean isKnownBaseItem(Item item) {
        return item == WitcherGlyphRunestones.LESSER_GLYPH_OF_INFUSION.item().get() || item == WitcherGlyphRunestones.LESSER_RUNESTONE.item().get();
    }

    private static Resolution resolve(CraftingRecipeInput input) {
        Item baseItem = null;
        Item catalystItem = null;
        int filled = 0;

        for (var stack : input.getStacks()) {
            if (stack.isEmpty()) continue;
            filled++;
            if (filled > 2) return null;

            var item = stack.getItem();
            if (isKnownBaseItem(item)) {
                if (baseItem != null) return null;
                baseItem = item;
            } else {
                if (catalystItem != null) return null;
                catalystItem = item;
            }
        }

        if (filled != 2 || baseItem == null || catalystItem == null) return null;

        var cutId = RECIPES.get(new Key(baseItem, catalystItem));
        if (cutId == null) return null;

        return new Resolution(baseItem, cutId);
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
        var cutEntry = resolveCut(resolution.cutId(), lookup);
        if (cutEntry != null) {
            output.set(GemComponents.CUT, cutEntry);
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
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WitcherRecipes.GEM_CUT_CRAFTING_SERIALIZER;
    }
}
