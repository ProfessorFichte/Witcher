package net.witcher_rpg.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;

import java.util.List;

import static net.more_rpg_classes.util.CustomMethods.clearNegativeEffects;
import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class QuenShieldEffect extends StatusEffect {
    private final int healthPerStack;
    public static final Identifier QUEN_BREAK_ID = new Identifier(MOD_ID, "quen_sign_break");
    public static final SoundEvent QUEN_BREAK = SoundEvent.of(QUEN_BREAK_ID );
    public static final ParticleGroup quen_break = ParticleGroupBuilder.electricArc(SpellEngineParticles.lightning_arc_A)
            .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                    .count(4)
                    .speed(0.01F, 0.1F)
                    .extent(3F));


    public QuenShieldEffect(StatusEffectCategory category, int color) {
        super(category, color);
        this.healthPerStack = 4;
    }

    // 1.20.1: `applyUpdateEffect` returns void - the 1.21 `boolean` whose `false` ended the effect as
    // soon as the absorption was chewed through does not exist. The shield now runs its full duration;
    // only the HUD icon lingers, since the absorption itself is set in `onApplied`.
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        clearNegativeEffects(entity,false);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(healthPerStack * (1 + amplifier))));
    }

    public static void onRemove(LivingEntity entity) {
        if (!entity.getWorld().isClient()) {
            entity.getWorld().playSoundFromEntity(null, entity, QUEN_BREAK, SoundCategory.PLAYERS, 1F, 1F);
            ParticleHelper.sendBatches(entity, List.of(quen_break));
        }
    }
}
