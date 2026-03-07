package net.witcher_rpg.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.datagen.SmithingRecipeGenerator;
import net.witcher_rpg.item.WitcherArmorDiagrams;
import net.witcher_rpg.item.WitcherMaterials;
import net.witcher_rpg.item.armor.Armors;
import net.witcher_rpg.item.weapon.WeaponsRegister;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

/**
 * Generates all smithing recipes for the Witcher mod using the SmithingRecipeGenerator from More RPG Library
 */
public class WitcherSmithingRecipeGenerator extends SmithingRecipeGenerator {

    public WitcherSmithingRecipeGenerator(FabricDataOutput output) {
        super(output, MOD_ID);
    }

    @Override
    public void generate() {
        generateArmorUpgrades();
        generateSwordUpgrades();
        generateModdedSwordUpgrades();
    }

    private void generateArmorUpgrades() {
        // Get diagram items
        var enhancedDiagram = WitcherArmorDiagrams.ENTRIES.stream()
                .filter(e -> e.id().getPath().equals("enhanced_diagram"))
                .findFirst().get().item();
        var superiorDiagram = WitcherArmorDiagrams.ENTRIES.stream()
                .filter(e -> e.id().getPath().equals("superior_diagram"))
                .findFirst().get().item();
        var mastercraftedDiagram = WitcherArmorDiagrams.ENTRIES.stream()
                .filter(e -> e.id().getPath().equals("mastercrafted_diagram"))
                .findFirst().get().item();
        var grandmasterDiagram = WitcherArmorDiagrams.ENTRIES.stream()
                .filter(e -> e.id().getPath().equals("grandmaster_diagram"))
                .findFirst().get().item();

        // FELINE SCHOOL - Steel based (melee/agility armor)
        // Base -> Enhanced
        createSimpleArmorSetUpgrade(
                "enhanced_feline",
                Armors.felineSchoolArmorSet,
                enhancedDiagram,
                WitcherMaterials.STEEL_INGOT.item(),
                Armors.enhancedFelineSchoolArmorSet
        );
        // Enhanced -> Superior
        createSimpleArmorSetUpgrade(
                "superior_feline",
                Armors.enhancedFelineSchoolArmorSet,
                superiorDiagram,
                WitcherMaterials.DARK_IRON_INGOT.item(),
                Armors.superiorFelineSchoolArmorSet
        );
        // Superior -> Mastercrafted
        createSimpleArmorSetUpgrade(
                "mastercrafted_feline",
                Armors.superiorFelineSchoolArmorSet,
                mastercraftedDiagram,
                WitcherMaterials.DARK_STEEL_INGOT.item(),
                Armors.mastercraftedFelineSchoolArmorSet
        );
        // Mastercrafted -> Grandmaster
        createSimpleArmorSetUpgrade(
                "grandmaster_feline",
                Armors.mastercraftedFelineSchoolArmorSet,
                grandmasterDiagram,
                WitcherMaterials.DIMERITIUM_INGOT.item(),
                Armors.grandmasterFelineSchoolArmorSet
        );

        // GRIFFIN SCHOOL - Silver based (magic armor)
        // Base -> Enhanced
        createSimpleArmorSetUpgrade(
                "enhanced_griffin",
                Armors.griffinArmorSet,
                enhancedDiagram,
                WitcherMaterials.SILVER_INGOT.item(),
                Armors.enhancedGriffinArmorSet
        );
        // Enhanced -> Superior
        createSimpleArmorSetUpgrade(
                "superior_griffin",
                Armors.enhancedGriffinArmorSet,
                superiorDiagram,
                WitcherMaterials.METEORITE_INGOT.item(),
                Armors.superiorGriffinArmorSet
        );
        // Superior -> Mastercrafted
        createSimpleArmorSetUpgrade(
                "mastercrafted_griffin",
                Armors.superiorGriffinArmorSet,
                mastercraftedDiagram,
                WitcherMaterials.METEORITE_SILVER_INGOT.item(),
                Armors.mastercraftedGriffinArmorSet
        );
        // Mastercrafted -> Grandmaster
        createSimpleArmorSetUpgrade(
                "grandmaster_griffin",
                Armors.mastercraftedGriffinArmorSet,
                grandmasterDiagram,
                WitcherMaterials.DIMERITIUM_INGOT.item(),
                Armors.grandmasterGriffinArmorSet
        );

        // URSINE SCHOOL - Steel based (heavy armor)
        // Base -> Enhanced
        createSimpleArmorSetUpgrade(
                "enhanced_ursine",
                Armors.ursineArmorSet,
                enhancedDiagram,
                WitcherMaterials.STEEL_INGOT.item(),
                Armors.enhancedUrsineArmorSet
        );
        // Enhanced -> Superior
        createSimpleArmorSetUpgrade(
                "superior_ursine",
                Armors.enhancedUrsineArmorSet,
                superiorDiagram,
                WitcherMaterials.DARK_IRON_INGOT.item(),
                Armors.superiorUrsineArmorSet
        );
        // Superior -> Mastercrafted
        createSimpleArmorSetUpgrade(
                "mastercrafted_ursine",
                Armors.superiorUrsineArmorSet,
                mastercraftedDiagram,
                WitcherMaterials.DARK_STEEL_INGOT.item(),
                Armors.mastercraftedUrsineArmorSet
        );
        // Mastercrafted -> Grandmaster
        createSimpleArmorSetUpgrade(
                "grandmaster_ursine",
                Armors.mastercraftedUrsineArmorSet,
                grandmasterDiagram,
                WitcherMaterials.DIMERITIUM_INGOT.item(),
                Armors.grandmasterUrsineArmorSet
        );

        // WOLVEN SCHOOL - Silver based (hybrid armor)
        // Base -> Enhanced
        createSimpleArmorSetUpgrade(
                "enhanced_wolven",
                Armors.wolvenArmorSet,
                enhancedDiagram,
                WitcherMaterials.SILVER_INGOT.item(),
                Armors.enhancedWolvenArmorSet
        );
        // Enhanced -> Superior
        createSimpleArmorSetUpgrade(
                "superior_wolven",
                Armors.enhancedWolvenArmorSet,
                superiorDiagram,
                WitcherMaterials.METEORITE_INGOT.item(),
                Armors.superiorWolvenArmorSet
        );
        // Superior -> Mastercrafted
        createSimpleArmorSetUpgrade(
                "mastercrafted_wolven",
                Armors.superiorWolvenArmorSet,
                mastercraftedDiagram,
                WitcherMaterials.METEORITE_SILVER_INGOT.item(),
                Armors.mastercraftedWolvenArmorSet
        );
        // Mastercrafted -> Grandmaster
        createSimpleArmorSetUpgrade(
                "grandmaster_wolven",
                Armors.mastercraftedWolvenArmorSet,
                grandmasterDiagram,
                WitcherMaterials.DIMERITIUM_INGOT.item(),
                Armors.grandmasterWolvenArmorSet
        );
    }

    private void generateSwordUpgrades() {
        // Diamond -> Netherite upgrade (vanilla)
        createSimpleSmithingRecipe(
                "netherite_witcher_sword",
                WeaponsRegister.diamond_witcher_sword.item(),
                Identifier.ofVanilla("netherite_upgrade_smithing_template"),
                Identifier.ofVanilla("netherite_ingot"),
                WeaponsRegister.netherite_witcher_sword.item()
        );
    }

    private void generateModdedSwordUpgrades() {
        // Netherite -> Ruby (requires BetterNether)
        // Find ruby sword from entries (conditionally registered)
        var rubySword = WeaponsRegister.entries.stream()
                .filter(e -> e.id().getPath().equals("ruby_witcher_sword"))
                .findFirst()
                .map(e -> e.item())
                .orElse(null);

        if (rubySword != null) {
            createSmithingTransformRecipe(
                    "ruby_witcher_sword",
                    WeaponsRegister.netherite_witcher_sword.item(),
                    Identifier.ofVanilla("netherite_upgrade_smithing_template"),
                    Identifier.of("betternether", "nether_ruby"),
                    rubySword,
                    "betternether"
            );
        }

        // Netherite -> Aeternium (requires BetterEnd)
        var aeterniumSword = WeaponsRegister.entries.stream()
                .filter(e -> e.id().getPath().equals("aeternium_witcher_sword"))
                .findFirst()
                .map(e -> e.item())
                .orElse(null);

        if (aeterniumSword != null) {
            createSmithingTransformRecipe(
                    "aeternium_witcher_sword",
                    WeaponsRegister.netherite_witcher_sword.item(),
                    Identifier.ofVanilla("netherite_upgrade_smithing_template"),
                    Identifier.of("betterend", "aeternium_ingot"),
                    aeterniumSword,
                    "betterend"
            );
        }
    }
}
