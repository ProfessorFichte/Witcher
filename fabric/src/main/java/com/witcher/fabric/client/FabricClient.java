package com.witcher.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.witcher_rpg.client.WitcherClient;
import net.witcher_rpg.client.WitcherExposedClient;
import net.witcher_rpg.client.render.GlyphTooltipRenderer;
import net.witcher_rpg.client.render.RunestoneTooltipRenderer;
import net.witcher_rpg.client.entity.YrdenMagicTrapRenderer;
import net.witcher_rpg.client.entity.YrdenRenderer;
import net.witcher_rpg.entity.YrdenEntity;
import net.witcher_rpg.entity.YrdenMagicTrapEntity;
import net.witcher_rpg.item.component.GlyphTooltipComponent;
import net.witcher_rpg.item.component.RunestoneTooltipComponent;
import net.witcher_rpg.network.ExposedGlowPayload;

public final class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WitcherClient.registerParticleAppearances(new WitcherClient.ParticleAppearanceRegistrar() {
            @Override
            public <T extends net.minecraft.particle.ParticleEffect> void register(net.minecraft.particle.ParticleType<T> type, WitcherClient.SpriteFactory<T> factory) {
                ParticleFactoryRegistry.getInstance().register(type, factory::create);
            }
        });
        WitcherClient.init();

        ClientPlayNetworking.registerGlobalReceiver(ExposedGlowPayload.ID, (payload, context) ->
                context.client().execute(() -> WitcherExposedClient.setActive(payload.entityId(), payload.active())));

        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof GlyphTooltipComponent component) {
                return new GlyphTooltipRenderer(component);
            }
            if (data instanceof RunestoneTooltipComponent component) {
                return new RunestoneTooltipRenderer(component);
            }
            return null;
        });

        EntityRendererRegistry.register(YrdenEntity.ENTITY_TYPE, YrdenRenderer::new);
        EntityRendererRegistry.register(YrdenMagicTrapEntity.ENTITY_TYPE, YrdenMagicTrapRenderer::new);
    }
}
