package com.witcher.neoforge;

import com.witcher.neoforge.compat.CompatFeatures;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.witcher_rpg.client.WitcherExposedClient;
import net.witcher_rpg.network.ExposedGlowPayload;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.client.particle.WitcherParticles;

@Mod(WitcherClassMod.MOD_ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        CompatFeatures.init();
        WitcherClassMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        modBus.addListener(RegisterPayloadHandlersEvent.class, NeoForgeMod::registerPayloads);
    }

    private static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToClient(ExposedGlowPayload.ID, ExposedGlowPayload.CODEC, (payload, context) ->
                WitcherExposedClient.setActive(payload.entityId(), payload.active()));
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.DATA_COMPONENT_TYPE, reg -> {
            net.witcher_rpg.item.component.WitcherDataComponents.register();
        });
        event.register(RegistryKeys.MAP_DECORATION_TYPE, reg -> {
            WitcherClassMod.registerMapDecorations();
        });
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
}
