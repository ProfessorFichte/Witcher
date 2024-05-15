package net.witcher_rpg.client.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.api.render.CustomModels;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.entity.YrdenEntity;

public class YrdenRenderer<T extends YrdenEntity> extends EntityRenderer<T> {
    private final ItemRenderer itemRenderer;
    public YrdenRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public Identifier getTexture(T entity) {
        return null;
    }

    public static final Identifier modelId = new Identifier(WitcherClassMod.MOD_ID, "effect/yrden_circle");

    private static final RenderLayer layer =
            RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);



    public void render(T entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider
    vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrixStack, vertexConsumers, light);
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-1F * entity.getYaw() + 180F));
        matrixStack.translate(0, 0.5F, 0);
        CustomModels.render(layer, itemRenderer, modelId, matrixStack, vertexConsumers, light, entity.getId());
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.pop();
    }
}
