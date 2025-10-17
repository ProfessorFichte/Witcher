package com.witcher.fabric.compat.trinkets;

import net.fabricmc.loader.api.FabricLoader;

public class TrinketCompat {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            TrinketsHelper.register();
        }
    }
}
