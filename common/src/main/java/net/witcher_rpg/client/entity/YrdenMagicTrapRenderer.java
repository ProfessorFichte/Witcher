package net.witcher_rpg.client.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.api.render.CustomLayers;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.fx.Easing;
import net.spell_engine.api.spell.fx.ModelEffect;
import net.spell_engine.api.spell.fx.ModelEffectBuilder;
import net.spell_engine.client.render.ModelEffectOperations;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.entity.YrdenMagicTrapEntity;

import java.util.HashMap;
import java.util.Map;

public class YrdenMagicTrapRenderer<T extends YrdenMagicTrapEntity> extends EntityRenderer<T> {
    private final ItemRenderer itemRenderer;
    public YrdenMagicTrapRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public Identifier getTexture(T entity) {
        return null;
    }
    public static final Identifier modelId = Identifier.of(WitcherClassMod.MOD_ID, "spell_effect/magic_trap_yrden");
    private static final RenderLayer layer =  CustomLayers.spellEffect(LightEmission.RADIATE, false);

    private static final int spawnTicks = 5;
    private static final int despawnTicks = 5;
    private static final Map<Integer, ModelEffect> fxCache = new HashMap<>();

    private static ModelEffect fx(int totalTicks) {
        return fxCache.computeIfAbsent(totalTicks, ticks -> ModelEffectBuilder.create(modelId.toString())
                .duration(ticks)
                .scaleIn(0, Math.min(spawnTicks, ticks), Easing.EASE_OUT_BACK)
                .scaleOut(Math.max(ticks - despawnTicks, 0), ticks, Easing.EASE_IN_BACK)
                .build());
    }

    public void render(T entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider
            vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrixStack, vertexConsumers, light);
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-1F * entity.getYaw() + 180F));
        matrixStack.translate(0, 0.5F, 0);
        ModelEffectOperations.applyTransforms(matrixStack, fx(entity.getTimeToLive()), entity.age + tickDelta);
        CustomModels.render(layer, itemRenderer, modelId, matrixStack, vertexConsumers, light, entity.getId());
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.pop();
    }
}
