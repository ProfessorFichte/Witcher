package com.witcher.neoforge.compat.curios;

import net.spell_engine.Platform;

public class CuriosCompat {
    public static void init() {
        if (Platform.util().isModLoaded("curios")) {
            // Outsource to avoid class loading issues
            CuriosHelper.register();
        }
    }
}
