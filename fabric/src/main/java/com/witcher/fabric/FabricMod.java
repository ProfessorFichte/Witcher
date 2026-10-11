package com.witcher.fabric;

import com.witcher.fabric.compat.CompatFeatures;
import com.witcher.fabric.compat.jewelry.JewelryItemGroupCompat;
import com.witcher.fabric.worldgen.OreGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.compat.jewelry.JewelrySocketFeature;
import net.witcher_rpg.network.ExposedGlowPayload;


public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CompatFeatures.init();
        PayloadTypeRegistry.playS2C().register(ExposedGlowPayload.ID, ExposedGlowPayload.CODEC);
        WitcherClassMod.init();

        WitcherClassMod.registerMapDecorations();
        net.witcher_rpg.client.particle.WitcherParticles.register();
        WitcherClassMod.registerEffects();
        WitcherClassMod.registerItems();
        WitcherClassMod.registerEntities();
        WitcherClassMod.registerSounds();
        WitcherClassMod.registerRecipes();
        OreGeneration.register();
        WitcherClassMod.registerBlocks();

        if (JewelrySocketFeature.isJewelryPresent()) {
            JewelryItemGroupCompat.init();
        }
    }
}
