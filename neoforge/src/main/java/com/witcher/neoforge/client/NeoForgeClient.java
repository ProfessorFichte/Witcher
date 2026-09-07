package com.witcher.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.client.WitcherClient;
import net.witcher_rpg.client.entity.YrdenMagicTrapRenderer;
import net.witcher_rpg.client.entity.YrdenRenderer;
import net.witcher_rpg.client.render.GlyphTooltipRenderer;
import net.witcher_rpg.client.render.RunestoneTooltipRenderer;
import net.witcher_rpg.entity.YrdenEntity;
import net.witcher_rpg.entity.YrdenMagicTrapEntity;
import net.witcher_rpg.item.component.GlyphTooltipComponent;
import net.witcher_rpg.item.component.RunestoneTooltipComponent;

@EventBusSubscriber(modid = WitcherClassMod.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        WitcherClient.init();
    }
    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        WitcherClient.registerParticleAppearances(new WitcherClient.ParticleAppearanceRegistrar() {
            @Override
            public <T extends net.minecraft.particle.ParticleEffect> void register(net.minecraft.particle.ParticleType<T> type, WitcherClient.SpriteFactory<T> factory) {
                event.registerSpriteSet(type, factory::create);
            }
        });
    }
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(YrdenEntity.ENTITY_TYPE, YrdenRenderer::new);
        event.registerEntityRenderer(YrdenMagicTrapEntity.ENTITY_TYPE, YrdenMagicTrapRenderer::new);
    }
    @SubscribeEvent
    public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(GlyphTooltipComponent.class, GlyphTooltipRenderer::new);
        event.register(RunestoneTooltipComponent.class, RunestoneTooltipRenderer::new);
    }
}
