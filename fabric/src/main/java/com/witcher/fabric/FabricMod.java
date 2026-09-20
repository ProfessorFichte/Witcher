package com.witcher.fabric;

import com.witcher.fabric.compat.CompatFeatures;
import com.witcher.fabric.worldgen.OreGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.network.WitcherNetworking;


public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CompatFeatures.init();
        WitcherNetworking.install((player, payload) -> ServerPlayNetworking.send(player, payload.id(), payload.toBuffer()));
        net.witcher_rpg.item.component.WitcherDataComponents.register();
        WitcherClassMod.init();
        WitcherClassMod.registerSpellSchools();

        net.witcher_rpg.client.particle.WitcherParticles.register();
        WitcherClassMod.registerEffects();
        WitcherClassMod.registerItems();
        WitcherClassMod.registerEntities();
        WitcherClassMod.registerSounds();
        OreGeneration.register();
        WitcherClassMod.registerBlocks();
        WitcherClassMod.registerBlockItems();
        WitcherClassMod.registerEnchantments();
    }
}
