package com.witcher.fabric;

import com.witcher.fabric.compat.CompatFeatures;
import net.fabricmc.api.ModInitializer;
import net.witcher_rpg.WitcherClassMod;


public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CompatFeatures.init();
        WitcherClassMod.init();

        WitcherClassMod.registerEffects();
        WitcherClassMod.registerItems();
        WitcherClassMod.registerEntities();
        WitcherClassMod.registerSounds();
        WitcherClassMod.registerWorldGen();
        WitcherClassMod.registerBlocks();
    }
}
