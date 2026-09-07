package com.witcher.fabric.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import net.witcher_rpg.util.tags.ModBiomeTags;
import net.witcher_rpg.worldgen.OreGen;

public class OreGeneration {
    public static void register() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                OreGen.SILVER_ORE_SMALL_PLACED_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                OreGen.SILVER_ORE_LARGE_PLACED_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                OreGen.SILVER_ORE_BURIED_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                OreGen.METEORITE_ORE_SMALL_PLACED_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                OreGen.METEORITE_ORE_LARGE_PLACED_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                OreGen.METEORITE_ORE_BURIED_PLACED_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModBiomeTags.HAS_METEORITE),
                GenerationStep.Feature.UNDERGROUND_DECORATION,
                OreGen.METEORITE_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                OreGen.DARK_IRON_ORE_SMALL_PLACED_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                OreGen.DARK_IRON_ORE_LARGE_PLACED_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                OreGen.DARK_IRON_ORE_BURIED_PLACED_KEY
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                OreGen.NETHER_DARK_IRON_ORE_PLACED_KEY
        );
    }
}
