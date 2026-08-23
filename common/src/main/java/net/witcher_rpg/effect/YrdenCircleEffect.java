package net.witcher_rpg.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.fx.ParticleHelper;
import net.witcher_rpg.util.tags.WitcherEntityTags;

import java.util.List;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class YrdenCircleEffect extends StatusEffect {
    public YrdenCircleEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public static final ParticleGroup yrden_damage_spehre = ParticleGroupBuilder.of(Identifier.of(MOD_ID, "yrden_cloud"))
            .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                    .count(15)
                    .speed(0.001F, 0.02F));

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int pAmplifier) {
        EntityType<?> type = ((Entity) entity).getType();
        if(type.isIn(WitcherEntityTags.YRDEN_VULNERABLE)){
            if (!entity.getWorld().isClient()) {
                ParticleHelper.sendBatches(entity, List.of(yrden_damage_spehre));
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