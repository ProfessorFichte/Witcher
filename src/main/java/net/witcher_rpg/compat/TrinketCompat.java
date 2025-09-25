package net.witcher_rpg.compat;

import net.fabricmc.loader.api.FabricLoader;

public class TrinketCompat {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            TrinketsHelper.register();
        }
    }
}
