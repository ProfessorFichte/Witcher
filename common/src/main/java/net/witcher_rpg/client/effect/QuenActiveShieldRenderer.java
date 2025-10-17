package net.witcher_rpg.client.effect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class QuenActiveShieldRenderer implements CustomModelStatusEffect.Renderer{
    public static final Identifier modelId = Identifier.of(MOD_ID, "effect/quen_active_shield");
    private static final RenderLayer SHIELD_RENDER = RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);


    @Override
    public void renderEffect(int amplifier, LivingEntity livingEntity, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        float yOffset = 0.51F;
        matrixStack.push();
        matrixStack.translate(0, yOffset, 0);
        CustomModels.render(SHIELD_RENDER, MinecraftClient.getInstance().getItemRenderer(), modelId,
                matrixStack, vertexConsumers, light, livingEntity.getId());
        matrixStack.pop();


    }
}
