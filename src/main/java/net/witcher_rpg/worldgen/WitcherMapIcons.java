package net.witcher_rpg.worldgen;

import net.minecraft.item.map.MapDecorationType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherMapIcons {
    public static final Identifier FELINE_ICON_ID = Identifier.of(MOD_ID, "feline_icon");

    public static final MapDecorationType FELINE_ICON = new MapDecorationType(
            FELINE_ICON_ID,
            true,
            -1,
            true,
            false
    );

    public static void register() {
        Registry.register(Registries.MAP_DECORATION_TYPE, FELINE_ICON_ID, FELINE_ICON);



    }
}
