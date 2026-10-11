package net.witcher_rpg.compat.jewelry;

import net.spell_engine.Platform;
import net.witcher_rpg.WitcherClassMod;

public class JewelrySocketFeature {
    private static final String JEWELRY_MOD_ID = "jewelry";

    public static boolean isJewelryPresent() {
        return Platform.util().isModLoaded(JEWELRY_MOD_ID) || Platform.util().isDevelopmentEnvironment();
    }

    public static boolean glyphsActive() {
        return isJewelryPresent() && WitcherClassMod.featuresConfig.value.glyphs;
    }

    public static boolean runestonesActive() {
        return isJewelryPresent() && WitcherClassMod.featuresConfig.value.runestones;
    }
}
