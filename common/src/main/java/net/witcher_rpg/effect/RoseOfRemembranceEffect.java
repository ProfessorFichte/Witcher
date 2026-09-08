package net.witcher_rpg.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RoseOfRemembranceEffect extends StatusEffect {
    public RoseOfRemembranceEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    // 1.20.1: `applyUpdateEffect` returns void (the 1.21 `boolean` return that ends the effect early
    // does not exist).
    public void applyUpdateEffect(LivingEntity livingEntity, int pAmplifier) {
        if (livingEntity.getHealth() < livingEntity.getMaxHealth()) {
            livingEntity.heal(livingEntity.getMaxHealth()*0.05F);
        }
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        i = 40;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}
