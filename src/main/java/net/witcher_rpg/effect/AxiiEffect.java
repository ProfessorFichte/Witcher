package net.witcher_rpg.effect;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.witcher_rpg.util.tags.WitcherEntityTags;

public class AxiiEffect extends StatusEffect {
    protected AxiiEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public void onApplied(LivingEntity livingEntity, int amplifier) {
        super.onApplied(livingEntity, amplifier);
        EntityType<?> type = livingEntity.getType();
        if(type.isIn(WitcherEntityTags.AXII_EFFECT_IMMUNE)) {
            livingEntity.removeStatusEffect(WitcherStatusEffects.AXII.entry);

        }
    }

}
