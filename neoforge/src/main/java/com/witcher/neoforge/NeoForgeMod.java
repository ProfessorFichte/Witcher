package com.witcher.neoforge;

import com.witcher.neoforge.compat.CompatFeatures;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.client.particle.WitcherParticles;

@Mod(WitcherClassMod.MOD_ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        CompatFeatures.init();
        WitcherClassMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        modBus.addListener(NeoForgeMod::registerSpawnPlacements);
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            WitcherClassMod.registerSounds();
        });
        event.register(RegistryKeys.PARTICLE_TYPE, reg -> {
            WitcherParticles.register();
        });
        event.register(RegistryKeys.ITEM, reg -> {
            WitcherClassMod.registerItems();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            WitcherClassMod.registerEffects();
        });
        event.register(RegistryKeys.BLOCK, reg -> {
            WitcherClassMod.registerBlocks();
        });
        event.register(RegistryKeys.ENTITY_TYPE, reg -> {
            WitcherClassMod.registerEntities();
        });
    }
    private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        WitcherClassMod.registerWorldGen();
    }
}
