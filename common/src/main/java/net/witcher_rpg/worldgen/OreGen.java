package net.witcher_rpg.worldgen;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.witcher_rpg.WitcherClassMod;

public class OreGen {
    public static final RegistryKey<PlacedFeature> SILVER_ORE_SMALL_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(WitcherClassMod.MOD_ID, "silver_ore_small_placed"));
    public static final RegistryKey<PlacedFeature> SILVER_ORE_LARGE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(WitcherClassMod.MOD_ID, "silver_ore_large_placed"));
    public static final RegistryKey<PlacedFeature> SILVER_ORE_BURIED_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(WitcherClassMod.MOD_ID, "silver_ore_buried_placed"));

    public static final RegistryKey<PlacedFeature> METEORITE_ORE_SMALL_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(WitcherClassMod.MOD_ID, "meteorite_ore_small_placed"));
    public static final RegistryKey<PlacedFeature> METEORITE_ORE_LARGE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(WitcherClassMod.MOD_ID, "meteorite_ore_large_placed"));
    public static final RegistryKey<PlacedFeature> METEORITE_ORE_BURIED_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(WitcherClassMod.MOD_ID, "meteorite_ore_buried_placed"));
    public static final RegistryKey<PlacedFeature> METEORITE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(WitcherClassMod.MOD_ID, "meteorite"));

    public static final RegistryKey<PlacedFeature> DARK_IRON_ORE_SMALL_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(WitcherClassMod.MOD_ID, "dark_iron_ore_small_placed"));
    public static final RegistryKey<PlacedFeature> DARK_IRON_ORE_LARGE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(WitcherClassMod.MOD_ID, "dark_iron_ore_large_placed"));
    public static final RegistryKey<PlacedFeature> DARK_IRON_ORE_BURIED_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(WitcherClassMod.MOD_ID, "dark_iron_ore_buried_placed"));
    public static final RegistryKey<PlacedFeature> NETHER_DARK_IRON_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(WitcherClassMod.MOD_ID, "dark_iron_ore_nether_placed"));
}
