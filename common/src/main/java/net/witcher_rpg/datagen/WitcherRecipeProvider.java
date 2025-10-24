package net.witcher_rpg.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.spell_engine.api.item.armor.Armor;
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
        /// SHAPED RECIPES
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.BEAR_SCHOOL_MEDALLION.item().get())
                .pattern("BEB")
                .pattern("CAC")
                .pattern("DBD")
                .input('A', WitcherTrinkets.PURE_SILVER.item().get())
                .input('B', WitcherItemTags.SILVER_INGOTS)
                .input('C', WitcherMaterials.DARK_STEEL_INGOT.item())
                .input('D', WitcherMaterials.DARK_IRON_INGOT.item())
                .input('E', Items.CHAIN)
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.PURE_SILVER.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.PURE_SILVER.item().get()))
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
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.PURE_SILVER.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.PURE_SILVER.item().get()))
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
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.PURE_SILVER.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.PURE_SILVER.item().get()))
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
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.PURE_SILVER.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.PURE_SILVER.item().get()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.AARD_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_AARD_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_AARD_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_AARD_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_AARD_GLYPH.item().get())
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.LESSER_AARD_GLYPH.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.LESSER_AARD_GLYPH.item().get()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.AXII_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_AXII_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_AXII_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_AXII_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_AXII_GLYPH.item().get())
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.LESSER_AXII_GLYPH.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.LESSER_AXII_GLYPH.item().get()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.IGNI_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_IGNI_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_IGNI_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_IGNI_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_IGNI_GLYPH.item().get())
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.LESSER_IGNI_GLYPH.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.LESSER_IGNI_GLYPH.item().get()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.QUEN_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_QUEN_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_QUEN_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_QUEN_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_QUEN_GLYPH.item().get())
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.LESSER_QUEN_GLYPH.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.LESSER_QUEN_GLYPH.item().get()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.YRDEN_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_YRDEN_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_YRDEN_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_YRDEN_GLYPH.item().get())
                .input(WitcherTrinkets.LESSER_YRDEN_GLYPH.item().get())
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.LESSER_YRDEN_GLYPH.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.LESSER_YRDEN_GLYPH.item().get()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.GREATER_AARD_GLYPH.item().get())
                .input(WitcherTrinkets.AARD_GLYPH.item().get())
                .input(WitcherTrinkets.AARD_GLYPH.item().get())
                .input(WitcherTrinkets.AARD_GLYPH.item().get())
                .input(WitcherTrinkets.AARD_GLYPH.item().get())
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.AARD_GLYPH.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.AARD_GLYPH.item().get()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.GREATER_AXII_GLYPH.item().get())
                .input(WitcherTrinkets.AXII_GLYPH.item().get())
                .input(WitcherTrinkets.AXII_GLYPH.item().get())
                .input(WitcherTrinkets.AXII_GLYPH.item().get())
                .input(WitcherTrinkets.AXII_GLYPH.item().get())
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.AXII_GLYPH.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.AXII_GLYPH.item().get()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.GREATER_IGNI_GLYPH.item().get())
                .input(WitcherTrinkets.IGNI_GLYPH.item().get())
                .input(WitcherTrinkets.IGNI_GLYPH.item().get())
                .input(WitcherTrinkets.IGNI_GLYPH.item().get())
                .input(WitcherTrinkets.IGNI_GLYPH.item().get())
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.IGNI_GLYPH.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.IGNI_GLYPH.item().get()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.GREATER_QUEN_GLYPH.item().get())
                .input(WitcherTrinkets.QUEN_GLYPH.item().get())
                .input(WitcherTrinkets.QUEN_GLYPH.item().get())
                .input(WitcherTrinkets.QUEN_GLYPH.item().get())
                .input(WitcherTrinkets.QUEN_GLYPH.item().get())
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.QUEN_GLYPH.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.QUEN_GLYPH.item().get()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, WitcherTrinkets.GREATER_YRDEN_GLYPH.item().get())
                .input(WitcherTrinkets.YRDEN_GLYPH.item().get())
                .input(WitcherTrinkets.YRDEN_GLYPH.item().get())
                .input(WitcherTrinkets.YRDEN_GLYPH.item().get())
                .input(WitcherTrinkets.YRDEN_GLYPH.item().get())
                .criterion(FabricRecipeProvider.hasItem(WitcherTrinkets.YRDEN_GLYPH.item().get()), FabricRecipeProvider.conditionsFromItem(WitcherTrinkets.YRDEN_GLYPH.item().get()))
                .offerTo(exporter);


        //// SMELTING
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
        FabricRecipeProvider.offerSmelting(exporter,
                armorSet.pieces(),
                RecipeCategory.MISC,
                output,
                0.1f,
                UNSMELT_TIME,
                "disassemble"
        );
        FabricRecipeProvider.offerBlasting(exporter,
                armorSet.pieces(),
                RecipeCategory.MISC,
                output,
                0.1f,
                UNSMELT_TIME / 2,
                "disassemble"
        );
    }

    private static void disassemble(RecipeExporter exporter, List<ItemConvertible> items, Item output) {
        FabricRecipeProvider.offerSmelting(exporter,
                items,
                RecipeCategory.MISC,
                output,
                0.1f,
                UNSMELT_TIME,
                "disassemble"
        );
        FabricRecipeProvider.offerBlasting(exporter,
                items,
                RecipeCategory.MISC,
                output,
                0.1f,
                UNSMELT_TIME / 2,
                "disassemble"
        );
    }
}