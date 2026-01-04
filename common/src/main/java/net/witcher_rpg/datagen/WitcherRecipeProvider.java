package net.witcher_rpg.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.armor.Armor;
import net.witcher_rpg.blocks.WitcherBlocks;
import net.witcher_rpg.item.WitcherMaterials;
import net.witcher_rpg.item.WitcherTrinkets;
import net.witcher_rpg.item.armor.Armors;
import net.witcher_rpg.item.weapon.WeaponsRegister;
import net.witcher_rpg.util.tags.WitcherItemTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WitcherRecipeProvider extends FabricRecipeProvider {
    public WitcherRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    public static int UNSMELT_TIME = 300;

    @Override
    public void generate(RecipeExporter exporter) {
        // WITCHER SWORDS - Shaped Crafting
        generateSwordRecipes(exporter);

        // SPELL BOOKS - Shapeless Crafting
        generateSpellBookRecipes(exporter);

        // WITCHER ARMOR - Shaped Crafting (base tier only, upgrades handled by SmithingRecipeGenerator)
        generateArmorRecipes(exporter);

        // MATERIALS - Smelting, Blasting, and Crafting
        generateMaterialRecipes(exporter);

        // TRINKET RECIPES (Medallions and Glyphs)
        generateTrinketRecipes(exporter);

        // DISASSEMBLY RECIPES (Smelting armor/weapons back into materials)
        generateDisassemblyRecipes(exporter);

        // MODDED SWORD RECIPES - With mod load conditions
        generateModdedSwordRecipes(exporter);
    }

    private void generateSwordRecipes(RecipeExporter exporter) {
        // Iron Witcher Sword
        createSwordRecipe(exporter, WeaponsRegister.iron_witcher_sword.item(), Items.IRON_INGOT);

        // Golden Witcher Sword
        createSwordRecipe(exporter, WeaponsRegister.golden_witcher_sword.item(), Items.GOLD_INGOT);

        // Diamond Witcher Sword
        var hardenedLeather = getOrFallback(Identifier.of("more_rpg_classes", "hardened_leather"), Items.LEATHER);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WeaponsRegister.diamond_witcher_sword.item())
                .pattern(" W ")
                .pattern("WWW")
                .pattern(" R ")
                .input('W', Items.DIAMOND)
                .input('R', hardenedLeather)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);

        // Steel Witcher Sword - uses steel ingots
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WeaponsRegister.steel_witcher_sword.item())
                .pattern(" W ")
                .pattern("WWW")
                .pattern(" R ")
                .input('W', WitcherItemTags.STEEL_INGOTS)
                .input('R', hardenedLeather)
                .criterion(hasItem(WitcherMaterials.STEEL_INGOT.item()), conditionsFromItem(WitcherMaterials.STEEL_INGOT.item()))
                .offerTo(exporter);

        // Silver Witcher Sword
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WeaponsRegister.witcher_silver_sword.item())
                .pattern(" W ")
                .pattern("WWW")
                .pattern(" R ")
                .input('W', WitcherItemTags.SILVER_INGOTS)
                .input('R', hardenedLeather)
                .criterion(hasItem(WitcherMaterials.SILVER_INGOT.item()), conditionsFromItem(WitcherMaterials.SILVER_INGOT.item()))
                .offerTo(exporter);

        // Dark Iron Witcher Sword
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WeaponsRegister.dark_iron_witcher_sword.item())
                .pattern(" W ")
                .pattern("WWW")
                .pattern(" R ")
                .input('W', WitcherMaterials.DARK_IRON_INGOT.item())
                .input('R', hardenedLeather)
                .criterion(hasItem(WitcherMaterials.DARK_IRON_INGOT.item()), conditionsFromItem(WitcherMaterials.DARK_IRON_INGOT.item()))
                .offerTo(exporter);

        // Dark Steel Witcher Sword - crafted using netherite scrap
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WeaponsRegister.dark_steel_witcher_sword.item())
                .pattern("RRR")
                .pattern("RWW")
                .pattern("XX ")
                .input('W', Items.NETHERITE_SCRAP)
                .input('R', WitcherItemTags.STEEL_INGOTS)
                .input('X', WitcherMaterials.DARK_IRON_INGOT.item())
                .criterion(hasItem(Items.NETHERITE_SCRAP), conditionsFromItem(Items.NETHERITE_SCRAP))
                .offerTo(exporter);

        // Meteorite Witcher Sword
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WeaponsRegister.witcher_meteorite_sword.item())
                .pattern(" W ")
                .pattern("WWW")
                .pattern(" R ")
                .input('W', WitcherMaterials.METEORITE_INGOT.item())
                .input('R', hardenedLeather)
                .criterion(hasItem(WitcherMaterials.METEORITE_INGOT.item()), conditionsFromItem(WitcherMaterials.METEORITE_INGOT.item()))
                .offerTo(exporter);

        // Meteorite Silver Witcher Sword
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WeaponsRegister.witcher_meteorite_silver_sword.item())
                .pattern(" W ")
                .pattern("WWW")
                .pattern(" R ")
                .input('W', WitcherMaterials.METEORITE_SILVER_INGOT.item())
                .input('R', hardenedLeather)
                .criterion(hasItem(WitcherMaterials.METEORITE_SILVER_INGOT.item()), conditionsFromItem(WitcherMaterials.METEORITE_SILVER_INGOT.item()))
                .offerTo(exporter);
    }

    private void generateModdedSwordRecipes(RecipeExporter exporter) {
        // Aeternium Witcher Sword - requires BetterEnd (handled in smithing recipes)
        // Ruby Witcher Sword - requires BetterNether (handled in smithing recipes)
        // Note: Smithing recipes with mod conditions are generated in WitcherSmithingRecipeGenerator
    }

    private void createSwordRecipe(RecipeExporter exporter, Item result, Item material) {
        var hardenedLeather = getOrFallback(Identifier.of("more_rpg_classes", "hardened_leather"), Items.LEATHER);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .pattern(" W ")
                .pattern("WWW")
                .pattern(" R ")
                .input('W', material)
                .input('R', hardenedLeather)
                .criterion(hasItem(material), conditionsFromItem(material))
                .offerTo(exporter);
    }

    private static Item getOrFallback(Identifier id, Item fallback) {
        var item = Registries.ITEM.get(id);
        return item != null && item != Items.AIR ? item : fallback;
    }

    private void generateSpellBookRecipes(RecipeExporter exporter) {
        var hardenedLeather = getOrFallback(Identifier.of("more_rpg_classes", "hardened_leather"), Items.LEATHER);
        var baseSignsBook = getOrFallback(Identifier.of("witcher_rpg", "base_signs_spell_book"), Items.WRITTEN_BOOK);
        var fencingBook = getOrFallback(Identifier.of("witcher_rpg", "fencing_spell_book"), Items.WRITTEN_BOOK);

        // Base Signs Spell Book
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, baseSignsBook)
                .input(hardenedLeather)
                .input(WitcherItemTags.SILVER_INGOTS)
                .input(Items.BOOK)
                .input(Items.LAPIS_LAZULI)
                .criterion(hasItem(Items.BOOK), conditionsFromItem(Items.BOOK))
                .offerTo(exporter);

        // Fencing Spell Book
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, fencingBook)
                .input(hardenedLeather)
                .input(WitcherItemTags.STEEL_INGOTS)
                .input(Items.BOOK)
                .input(Items.REDSTONE)
                .criterion(hasItem(Items.BOOK), conditionsFromItem(Items.BOOK))
                .offerTo(exporter);

        // Master Spell Book - combines base signs and fencing
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherMaterials.MASTER_BOOK)
                .input(baseSignsBook)
                .input(fencingBook)
                .input(Items.DIAMOND)
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }

    private void generateArmorRecipes(RecipeExporter exporter) {
        // Base Witcher Armor - Tier 1
        createArmorSet(exporter, "witcher", Items.LEATHER, Items.IRON_INGOT,
                Armors.witcherArmorSet);

        // Feline School Armor - Tier 1 (Light/Agility)
        createArmorSet(exporter, "feline", WitcherMaterials.STEEL_INGOT.item(), Items.LEATHER,
                Armors.felineSchoolArmorSet);

        // Griffin School Armor - Tier 1 (Magic)
        createArmorSet(exporter, "griffin", WitcherMaterials.SILVER_INGOT.item(), Items.LEATHER,
                Armors.griffinArmorSet);

        // Ursine School Armor - Tier 1 (Heavy/Tank)
        createArmorSet(exporter, "ursine", WitcherMaterials.STEEL_INGOT.item(), Items.CHAIN,
                Armors.ursineArmorSet);

        // Wolven School Armor - Tier 1 (Hybrid)
        createArmorSet(exporter, "wolven", WitcherMaterials.SILVER_INGOT.item(), Items.LEATHER,
                Armors.wolvenArmorSet);
    }

    private void createArmorSet(RecipeExporter exporter, String name, Item primary, Item secondary, Armor.Set armorSet) {
        // Helmet
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) armorSet.head)
                .pattern("PPP")
                .pattern("P P")
                .input('P', primary)
                .criterion(hasItem(primary), conditionsFromItem(primary))
                .offerTo(exporter, name + "_head");

        // Chestplate
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) armorSet.chest)
                .pattern("P P")
                .pattern("PPP")
                .pattern("SSS")
                .input('P', primary)
                .input('S', secondary)
                .criterion(hasItem(primary), conditionsFromItem(primary))
                .offerTo(exporter, name + "_chest");

        // Leggings
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) armorSet.legs)
                .pattern("PPP")
                .pattern("S S")
                .pattern("P P")
                .input('P', primary)
                .input('S', secondary)
                .criterion(hasItem(primary), conditionsFromItem(primary))
                .offerTo(exporter, name + "_legs");

        // Boots
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, (Item) armorSet.feet)
                .pattern("S S")
                .pattern("P P")
                .input('P', primary)
                .input('S', secondary)
                .criterion(hasItem(primary), conditionsFromItem(primary))
                .offerTo(exporter, name + "_feet");
    }

    private void generateMaterialRecipes(RecipeExporter exporter) {
        // ===== SILVER =====
        // Raw Silver Block
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.RAW_SILVER_BLOCK)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', WitcherMaterials.RAW_SILVER.item())
                .criterion(hasItem(WitcherMaterials.RAW_SILVER.item()), conditionsFromItem(WitcherMaterials.RAW_SILVER.item()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.RAW_SILVER.item(), 9)
                .input(Blocks.RAW_SILVER_BLOCK)
                .criterion(hasItem(Blocks.RAW_SILVER_BLOCK), conditionsFromItem(Blocks.RAW_SILVER_BLOCK))
                .offerTo(exporter, "raw_silver_from_block");

        // Silver Ingot Smelting from Raw Silver
        offerSmelting(exporter, List.of(WitcherMaterials.RAW_SILVER.item()),
                RecipeCategory.MISC, WitcherMaterials.SILVER_INGOT.item(),
                1.0f, 200, "silver");
        offerBlasting(exporter, List.of(WitcherMaterials.RAW_SILVER.item()),
                RecipeCategory.MISC, WitcherMaterials.SILVER_INGOT.item(),
                1.0f, 100, "silver");

        // Silver Block
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.SILVER_BLOCK)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', WitcherItemTags.SILVER_INGOTS)
                .criterion(hasItem(WitcherMaterials.SILVER_INGOT.item()), conditionsFromItem(WitcherMaterials.SILVER_INGOT.item()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.SILVER_INGOT.item(), 9)
                .input(Blocks.SILVER_BLOCK)
                .criterion(hasItem(Blocks.SILVER_BLOCK), conditionsFromItem(Blocks.SILVER_BLOCK))
                .offerTo(exporter, "silver_ingot_from_block");

        // Silver Ingot <-> Nuggets
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.SILVER_NUGGET.item(), 9)
                .input(WitcherMaterials.SILVER_INGOT.item())
                .criterion(hasItem(WitcherMaterials.SILVER_INGOT.item()), conditionsFromItem(WitcherMaterials.SILVER_INGOT.item()))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.SILVER_INGOT.item())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', WitcherMaterials.SILVER_NUGGET.item())
                .criterion(hasItem(WitcherMaterials.SILVER_NUGGET.item()), conditionsFromItem(WitcherMaterials.SILVER_NUGGET.item()))
                .offerTo(exporter, "silver_ingot_from_nuggets");

        // ===== STEEL =====
        // Steel Ingot from Iron (blasting only)
        offerBlasting(exporter, List.of(Items.IRON_INGOT),
                RecipeCategory.MISC, WitcherMaterials.STEEL_INGOT.item(),
                0.5f, 100, "steel");

        // Steel Block
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.STEEL_BLOCK)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', WitcherItemTags.STEEL_INGOTS)
                .criterion(hasItem(WitcherMaterials.STEEL_INGOT.item()), conditionsFromItem(WitcherMaterials.STEEL_INGOT.item()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.STEEL_INGOT.item(), 9)
                .input(Blocks.STEEL_BLOCK)
                .criterion(hasItem(Blocks.STEEL_BLOCK), conditionsFromItem(Blocks.STEEL_BLOCK))
                .offerTo(exporter, "steel_ingot_from_block");

        // Steel Ingot <-> Nuggets
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.STEEL_NUGGET.item(), 9)
                .input(WitcherMaterials.STEEL_INGOT.item())
                .criterion(hasItem(WitcherMaterials.STEEL_INGOT.item()), conditionsFromItem(WitcherMaterials.STEEL_INGOT.item()))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.STEEL_INGOT.item())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', WitcherMaterials.STEEL_NUGGET.item())
                .criterion(hasItem(WitcherMaterials.STEEL_NUGGET.item()), conditionsFromItem(WitcherMaterials.STEEL_NUGGET.item()))
                .offerTo(exporter, "steel_ingot_from_nuggets");

        // ===== DARK IRON =====
        // Dark Iron Ingot from Raw Dark Iron
        offerSmelting(exporter, List.of(WitcherMaterials.RAW_DARK_IRON.item()),
                RecipeCategory.MISC, WitcherMaterials.DARK_IRON_INGOT.item(),
                1.0f, 200, "dark_iron");
        offerBlasting(exporter, List.of(WitcherMaterials.RAW_DARK_IRON.item()),
                RecipeCategory.MISC, WitcherMaterials.DARK_IRON_INGOT.item(),
                1.0f, 100, "dark_iron");

        // Dark Iron Block
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.DARK_IRON_BLOCK)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', WitcherMaterials.DARK_IRON_INGOT.item())
                .criterion(hasItem(WitcherMaterials.DARK_IRON_INGOT.item()), conditionsFromItem(WitcherMaterials.DARK_IRON_INGOT.item()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.DARK_IRON_INGOT.item(), 9)
                .input(Blocks.DARK_IRON_BLOCK)
                .criterion(hasItem(Blocks.DARK_IRON_BLOCK), conditionsFromItem(Blocks.DARK_IRON_BLOCK))
                .offerTo(exporter, "dark_iron_ingot_from_block");

        // ===== DARK STEEL =====
        // Dark Steel Block
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.DARK_STEEL_BLOCK)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', WitcherMaterials.DARK_STEEL_INGOT.item())
                .criterion(hasItem(WitcherMaterials.DARK_STEEL_INGOT.item()), conditionsFromItem(WitcherMaterials.DARK_STEEL_INGOT.item()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.DARK_STEEL_INGOT.item(), 9)
                .input(Blocks.DARK_STEEL_BLOCK)
                .criterion(hasItem(Blocks.DARK_STEEL_BLOCK), conditionsFromItem(Blocks.DARK_STEEL_BLOCK))
                .offerTo(exporter, "dark_steel_ingot_from_block");

        // ===== METEORITE =====
        // Meteorite Ingot from Meteorite (raw ore)
        offerSmelting(exporter, List.of(WitcherMaterials.METEORITE.item()),
                RecipeCategory.MISC, WitcherMaterials.METEORITE_INGOT.item(),
                1.0f, 200, "meteorite");
        offerBlasting(exporter, List.of(WitcherMaterials.METEORITE.item()),
                RecipeCategory.MISC, WitcherMaterials.METEORITE_INGOT.item(),
                1.0f, 100, "meteorite");

        // Meteorite Block
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.METEORITE_BLOCK)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', WitcherMaterials.METEORITE_INGOT.item())
                .criterion(hasItem(WitcherMaterials.METEORITE_INGOT.item()), conditionsFromItem(WitcherMaterials.METEORITE_INGOT.item()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.METEORITE_INGOT.item(), 9)
                .input(Blocks.METEORITE_BLOCK)
                .criterion(hasItem(Blocks.METEORITE_BLOCK), conditionsFromItem(Blocks.METEORITE_BLOCK))
                .offerTo(exporter, "meteorite_ingot_from_block");

        // ===== METEORITE SILVER =====
        // Meteorite Silver Ingot Crafting (Meteorite + Silver)
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.METEORITE_SILVER_INGOT.item())
                .pattern("MSM")
                .pattern("SMS")
                .pattern("MSM")
                .input('M', WitcherMaterials.METEORITE_INGOT.item())
                .input('S', WitcherItemTags.SILVER_INGOTS)
                .criterion(hasItem(WitcherMaterials.METEORITE_INGOT.item()),
                        conditionsFromItem(WitcherMaterials.METEORITE_INGOT.item()))
                .offerTo(exporter);

        // Meteorite Silver Block
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.METEORITE_SILVER_BLOCK)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', WitcherMaterials.METEORITE_SILVER_INGOT.item())
                .criterion(hasItem(WitcherMaterials.METEORITE_SILVER_INGOT.item()), conditionsFromItem(WitcherMaterials.METEORITE_SILVER_INGOT.item()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, WitcherMaterials.METEORITE_SILVER_INGOT.item(), 9)
                .input(Blocks.METEORITE_SILVER_BLOCK)
                .criterion(hasItem(Blocks.METEORITE_SILVER_BLOCK), conditionsFromItem(Blocks.METEORITE_SILVER_BLOCK))
                .offerTo(exporter, "meteorite_silver_ingot_from_block");
    }

    private void generateTrinketRecipes(RecipeExporter exporter) {
        // School Medallions
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.BEAR_SCHOOL_MEDALLION.item().get())
                .pattern("BEB")
                .pattern("CAC")
                .pattern("DBD")
                .input('A', WitcherTrinkets.PURE_SILVER.item().get())
                .input('B', WitcherItemTags.SILVER_INGOTS)
                .input('C', WitcherMaterials.DARK_STEEL_INGOT.item())
                .input('D', WitcherMaterials.DARK_IRON_INGOT.item())
                .input('E', Items.CHAIN)
                .criterion(hasItem(WitcherTrinkets.PURE_SILVER.item().get()),
                        conditionsFromItem(WitcherTrinkets.PURE_SILVER.item().get()))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.CAT_SCHOOL_MEDALLION.item().get())
                .pattern("BEB")
                .pattern("DAD")
                .pattern("CBC")
                .input('A', WitcherTrinkets.PURE_SILVER.item().get())
                .input('B', WitcherItemTags.SILVER_INGOTS)
                .input('C', WitcherMaterials.DARK_STEEL_INGOT.item())
                .input('D', WitcherMaterials.DARK_IRON_INGOT.item())
                .input('E', Items.CHAIN)
                .criterion(hasItem(WitcherTrinkets.PURE_SILVER.item().get()),
                        conditionsFromItem(WitcherTrinkets.PURE_SILVER.item().get()))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.GRIFFIN_SCHOOL_MEDALLION.item().get())
                .pattern("BEB")
                .pattern("DAD")
                .pattern("CBC")
                .input('A', WitcherTrinkets.PURE_SILVER.item().get())
                .input('B', WitcherItemTags.SILVER_INGOTS)
                .input('C', WitcherMaterials.METEORITE_SILVER_INGOT.item())
                .input('D', WitcherMaterials.METEORITE_INGOT.item())
                .input('E', Items.CHAIN)
                .criterion(hasItem(WitcherTrinkets.PURE_SILVER.item().get()),
                        conditionsFromItem(WitcherTrinkets.PURE_SILVER.item().get()))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.WOLF_SCHOOL_MEDALLION.item().get())
                .pattern("BEB")
                .pattern("DAD")
                .pattern("CBC")
                .input('A', WitcherTrinkets.PURE_SILVER.item().get())
                .input('B', WitcherItemTags.SILVER_INGOTS)
                .input('C', WitcherMaterials.METEORITE_SILVER_INGOT.item())
                .input('D', WitcherItemTags.STEEL_INGOTS)
                .input('E', Items.CHAIN)
                .criterion(hasItem(WitcherTrinkets.PURE_SILVER.item().get()),
                        conditionsFromItem(WitcherTrinkets.PURE_SILVER.item().get()))
                .offerTo(exporter);

        // Glyph Combinations
        createGlyphRecipe(exporter, WitcherTrinkets.AARD_GLYPH.item().get(),
                WitcherTrinkets.LESSER_AARD_GLYPH.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.AXII_GLYPH.item().get(),
                WitcherTrinkets.LESSER_AXII_GLYPH.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.IGNI_GLYPH.item().get(),
                WitcherTrinkets.LESSER_IGNI_GLYPH.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.QUEN_GLYPH.item().get(),
                WitcherTrinkets.LESSER_QUEN_GLYPH.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.YRDEN_GLYPH.item().get(),
                WitcherTrinkets.LESSER_YRDEN_GLYPH.item().get());

        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_AARD_GLYPH.item().get(),
                WitcherTrinkets.AARD_GLYPH.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_AXII_GLYPH.item().get(),
                WitcherTrinkets.AXII_GLYPH.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_IGNI_GLYPH.item().get(),
                WitcherTrinkets.IGNI_GLYPH.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_QUEN_GLYPH.item().get(),
                WitcherTrinkets.QUEN_GLYPH.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_YRDEN_GLYPH.item().get(),
                WitcherTrinkets.YRDEN_GLYPH.item().get());
        // Rune Combinations
        createGlyphRecipe(exporter, WitcherTrinkets.DAZHBOG_RUNESTONE.item().get(),
                WitcherTrinkets.LESSER_DAZHBOG_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.CHERNOBOG_RUNESTONE.item().get(),
                WitcherTrinkets.LESSER_CHERNOBOG_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.STRIBOG_RUNESTONE.item().get(),
                WitcherTrinkets.LESSER_STRIBOG_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.SVAROG_RUNESTONE.item().get(),
                WitcherTrinkets.LESSER_SVAROG_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.TRIGLAV_RUNESTONE.item().get(),
                WitcherTrinkets.LESSER_TRIGLAV_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.PERUN_RUNESTONE.item().get(),
                WitcherTrinkets.LESSER_PERUN_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.VELES_RUNESTONE.item().get(),
                WitcherTrinkets.LESSER_VELES_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.MORANA_RUNESTONE.item().get(),
                WitcherTrinkets.LESSER_MORANA_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.ZORIA_RUNESTONE.item().get(),
                WitcherTrinkets.LESSER_ZORIA_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.DEVANA_RUNESTONE.item().get(),
                WitcherTrinkets.LESSER_DEVANA_RUNESTONE.item().get());

        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_DAZHBOG_RUNESTONE.item().get(),
                WitcherTrinkets.DAZHBOG_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_CHERNOBOG_RUNESTONE.item().get(),
                WitcherTrinkets.CHERNOBOG_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_STRIBOG_RUNESTONE.item().get(),
                WitcherTrinkets.STRIBOG_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_SVAROG_RUNESTONE.item().get(),
                WitcherTrinkets.SVAROG_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_TRIGLAV_RUNESTONE.item().get(),
                WitcherTrinkets.TRIGLAV_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_PERUN_RUNESTONE.item().get(),
                WitcherTrinkets.PERUN_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_VELES_RUNESTONE.item().get(),
                WitcherTrinkets.VELES_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_MORANA_RUNESTONE.item().get(),
                WitcherTrinkets.MORANA_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_ZORIA_RUNESTONE.item().get(),
                WitcherTrinkets.ZORIA_RUNESTONE.item().get());
        createGlyphRecipe(exporter, WitcherTrinkets.GREATER_DEVANA_RUNESTONE.item().get(),
                WitcherTrinkets.DEVANA_RUNESTONE.item().get());
    }

    private void createGlyphRecipe(RecipeExporter exporter, Item result, Item ingredient) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .input(ingredient)
                .input(ingredient)
                .input(ingredient)
                .input(ingredient)
                .criterion(hasItem(ingredient), conditionsFromItem(ingredient))
                .offerTo(exporter);
    }

    private void generateDisassemblyRecipes(RecipeExporter exporter) {
        // Armor Disassembly
        disassembleArmor(exporter, Armors.witcherArmorSet, Items.LEATHER);
        disassembleArmor(exporter, Armors.felineSchoolArmorSet, WitcherMaterials.STEEL_NUGGET.item());
        disassembleArmor(exporter, Armors.enhancedFelineSchoolArmorSet, WitcherMaterials.STEEL_NUGGET.item());
        disassembleArmor(exporter, Armors.superiorFelineSchoolArmorSet, WitcherMaterials.STEEL_NUGGET.item());
        disassembleArmor(exporter, Armors.ursineArmorSet, WitcherMaterials.STEEL_NUGGET.item());
        disassembleArmor(exporter, Armors.enhancedUrsineArmorSet, WitcherMaterials.STEEL_NUGGET.item());
        disassembleArmor(exporter, Armors.superiorUrsineArmorSet, WitcherMaterials.STEEL_NUGGET.item());
        disassembleArmor(exporter, Armors.griffinArmorSet, WitcherMaterials.SILVER_NUGGET.item());
        disassembleArmor(exporter, Armors.enhancedGriffinArmorSet, WitcherMaterials.SILVER_NUGGET.item());
        disassembleArmor(exporter, Armors.superiorGriffinArmorSet, WitcherMaterials.SILVER_NUGGET.item());
        disassembleArmor(exporter, Armors.wolvenArmorSet, WitcherMaterials.SILVER_NUGGET.item());
        disassembleArmor(exporter, Armors.enhancedWolvenArmorSet, WitcherMaterials.SILVER_NUGGET.item());
        disassembleArmor(exporter, Armors.superiorWolvenArmorSet, WitcherMaterials.SILVER_NUGGET.item());

        // Weapon Disassembly
        disassemble(exporter,
                WeaponsRegister.entries.stream()
                        .filter(entry -> entry.id().getPath().contains("gold"))
                        .map(entry -> (ItemConvertible) entry.item()).toList(),
                Items.GOLD_NUGGET);
        disassemble(exporter,
                WeaponsRegister.entries.stream()
                        .filter(entry -> entry.id().getPath().contains("iron"))
                        .map(entry -> (ItemConvertible) entry.item()).toList(),
                Items.IRON_NUGGET);
        disassemble(exporter,
                WeaponsRegister.entries.stream()
                        .filter(entry -> entry.id().getPath().contains("netherite"))
                        .map(entry -> (ItemConvertible) entry.item()).toList(),
                Items.NETHERITE_SCRAP);
        disassemble(exporter,
                WeaponsRegister.entries.stream()
                        .filter(entry -> entry.id().getPath().contains("steel"))
                        .map(entry -> (ItemConvertible) entry.item()).toList(),
                WitcherMaterials.STEEL_NUGGET.item());
        disassemble(exporter,
                WeaponsRegister.entries.stream()
                        .filter(entry -> entry.id().getPath().contains("silver"))
                        .map(entry -> (ItemConvertible) entry.item()).toList(),
                WitcherMaterials.SILVER_NUGGET.item());
    }

    private static void disassembleArmor(RecipeExporter exporter, Armor.Set armorSet, Item output) {
        offerSmelting(exporter,
                armorSet.pieces(),
                RecipeCategory.MISC,
                output,
                0.1f,
                UNSMELT_TIME,
                "disassemble"
        );
        offerBlasting(exporter,
                armorSet.pieces(),
                RecipeCategory.MISC,
                output,
                0.1f,
                UNSMELT_TIME / 2,
                "disassemble"
        );
    }

    private static void disassemble(RecipeExporter exporter, List<ItemConvertible> items, Item output) {
        offerSmelting(exporter,
                items,
                RecipeCategory.MISC,
                output,
                0.1f,
                UNSMELT_TIME,
                "disassemble"
        );
        offerBlasting(exporter,
                items,
                RecipeCategory.MISC,
                output,
                0.1f,
                UNSMELT_TIME / 2,
                "disassemble"
        );
    }

    // Helper class for block item references
    private static class Blocks {
        public static final Item RAW_SILVER_BLOCK = WitcherBlocks.RAW_SILVER_BLOCK.item();
        public static final Item SILVER_BLOCK = WitcherBlocks.SILVER_BLOCK.item();
        public static final Item STEEL_BLOCK = WitcherBlocks.STEEL_BLOCK.item();
        public static final Item DARK_IRON_BLOCK = WitcherBlocks.DARK_IRON_BLOCK.item();
        public static final Item DARK_STEEL_BLOCK = WitcherBlocks.DARK_STEEL_BLOCK.item();
        public static final Item METEORITE_BLOCK = WitcherBlocks.METEORITE_BLOCK.item();
        public static final Item METEORITE_SILVER_BLOCK = WitcherBlocks.METEORITE_SILVER_BLOCK.item();
    }
}
