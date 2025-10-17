package com.witcher.fabric.compat;

import com.witcher.fabric.compat.trinkets.TrinketCompat;
import net.spell_engine.fabric.compat.FabricCompatFeatures;
import net.witcher_rpg.compat.AccessoriesCompat;

public class CompatFeatures {
    public static void init() {
        var id = FabricCompatFeatures.initSlotCompat();
        if ("trinkets".equals(id)) {
            TrinketCompat.init();
        } else if ("accessories".equals(id)) {
            AccessoriesCompat.init();
        }
    }
}
