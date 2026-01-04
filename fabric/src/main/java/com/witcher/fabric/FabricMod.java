package com.witcher.fabric;

import com.witcher.fabric.compat.CompatFeatures;
import net.fabricmc.api.ModInitializer;
import net.witcher_rpg.WitcherClassMod;


public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CompatFeatures.init();
        net.witcher_rpg.item.component.WitcherDataComponents.register();
        WitcherClassMod.init();

        WitcherClassMod.registerMapDecorations();
        WitcherClassMod.registerEffects();
        WitcherClassMod.registerItems();
        WitcherClassMod.registerEntities();
        WitcherClassMod.registerSounds();
        WitcherClassMod.registerWorldGen();
        WitcherClassMod.registerBlocks();
    }
}
