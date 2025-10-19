package net.witcher_rpg.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.sound.SoundCategory;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.fx.ParticleHelper;

public class WitcherReflexesEffect extends StatusEffect {
    protected WitcherReflexesEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public static void onRemove(LivingEntity entity) {
    }
}
