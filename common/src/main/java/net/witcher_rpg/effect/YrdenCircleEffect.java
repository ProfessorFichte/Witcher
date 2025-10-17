package net.witcher_rpg.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.fx.ParticleHelper;
import net.witcher_rpg.util.tags.WitcherEntityTags;

public class YrdenCircleEffect extends StatusEffect {
    public YrdenCircleEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public static final ParticleBatch yrden_damage_spehre = new ParticleBatch(
            "witcher_rpg:yrden_cloud",
            ParticleBatch.Shape.SPHERE,
            ParticleBatch.Origin.CENTER,
            null,
            15,
            0.001F,
            0.02F,
            0);

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int pAmplifier) {
        EntityType<?> type = ((Entity) entity).getType();
        if(type.isIn(WitcherEntityTags.YRDEN_VULNERABLE)){
            if (!entity.getWorld().isClient()) {
                ParticleHelper.sendBatches(entity, new ParticleBatch[]{yrden_damage_spehre});
            }
            entity.setVelocity(Vec3d.ZERO);
        }
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}