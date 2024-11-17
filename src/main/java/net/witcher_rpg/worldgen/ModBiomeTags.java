package net.witcher_rpg.worldgen;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class ModBiomeTags {
    public static final TagKey<Biome> HAS_METEORITE = of("has_meteorite");

    private static TagKey<Biome> of(String id) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(MOD_ID, id));
    }
}
