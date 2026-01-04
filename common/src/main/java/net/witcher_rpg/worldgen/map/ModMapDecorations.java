package net.witcher_rpg.worldgen.map;

import net.minecraft.item.map.MapDecorationType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class ModMapDecorations {
    public static final RegistryKey<MapDecorationType> WITCHER_HIDEOUT_KEY = RegistryKey.of(
            RegistryKeys.MAP_DECORATION_TYPE,
            Identifier.of(MOD_ID, "witcher_hideout")
    );

    public static RegistryEntry<MapDecorationType> WITCHER_HIDEOUT;

    public static void register() {
        MapDecorationType decorationType = new MapDecorationType(
                Identifier.of(MOD_ID, "witcher_hideout"),
                true,
                -1,
                false,
                true
        );

        WITCHER_HIDEOUT = Registry.registerReference(
                Registries.MAP_DECORATION_TYPE,
                WITCHER_HIDEOUT_KEY,
                decorationType
        );
    }
}
