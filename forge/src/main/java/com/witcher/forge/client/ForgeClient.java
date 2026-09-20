package com.witcher.forge.client;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.witcher_rpg.client.WitcherClient;
import net.witcher_rpg.client.entity.YrdenMagicTrapRenderer;
import net.witcher_rpg.client.entity.YrdenRenderer;
import net.witcher_rpg.client.render.GlyphTooltipRenderer;
import net.witcher_rpg.client.render.RunestoneTooltipRenderer;
import net.witcher_rpg.entity.YrdenEntity;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.witcher_rpg.entity.YrdenMagicTrapEntity;
import net.witcher_rpg.item.component.GlyphTooltipComponent;
import net.witcher_rpg.item.component.RunestoneTooltipComponent;

public class ForgeClient {
    public static void register(IEventBus modBus) {
        modBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, ForgeClient::onClientSetup);
        modBus.addListener(EventPriority.NORMAL, false, RegisterParticleProvidersEvent.class, ForgeClient::registerParticleProviders);
        modBus.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterRenderers.class, ForgeClient::registerEntityRenderers);
        modBus.addListener(EventPriority.NORMAL, false, RegisterClientTooltipComponentFactoriesEvent.class, ForgeClient::registerTooltipComponents);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        WitcherClient.init(ModelPredicateProviderRegistry::register);
    }

    private static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        WitcherClient.registerParticleAppearances(new WitcherClient.ParticleAppearanceRegistrar() {
            @Override
            public <T extends net.minecraft.particle.ParticleEffect> void register(net.minecraft.particle.ParticleType<T> type, WitcherClient.SpriteFactory<T> factory) {
                event.registerSpriteSet(type, factory::create);
            }
        });
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(YrdenEntity.ENTITY_TYPE, YrdenRenderer::new);
        event.registerEntityRenderer(YrdenMagicTrapEntity.ENTITY_TYPE, YrdenMagicTrapRenderer::new);
    }

    private static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(GlyphTooltipComponent.class, GlyphTooltipRenderer::new);
        event.register(RunestoneTooltipComponent.class, RunestoneTooltipRenderer::new);
    }
}
