package net.witcher_rpg.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;

import static net.more_rpg_classes.util.CustomMethods.removeEffects;
import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class QuenEffect extends StatusEffect {
    public static final Identifier QUEN_BREAK_ID = new Identifier(MOD_ID, "quen_sign_break");
    public static final SoundEvent QUEN_BREAK = SoundEvent.of(QUEN_BREAK_ID );
    private final int healthPerStack;

    public static final ParticleBatch quen_break = new ParticleBatch(
            "spell_engine:electric_arc_a",
            ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
            null, 4, 0.01F, 0.1F, 0);
    public QuenEffect(StatusEffectCategory statusEffectCategory, int i) {
        this(statusEffectCategory, i, 4);
    }

    public QuenEffect(StatusEffectCategory statusEffectCategory, int i, int healthPerStack) {
        super(statusEffectCategory, i);
        this.healthPerStack = healthPerStack;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        float currentAbsorption = entity.getAbsorptionAmount();
        if(currentAbsorption == 0){
            entity.removeStatusEffect(Effects.QUEN_SHIELD);
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (float)(healthPerStack * (amplifier + 1)));
        if (!entity.getWorld().isClient()) {
                    entity.getWorld().playSoundFromEntity(null, entity, QUEN_BREAK, SoundCategory.PLAYERS, 1F, 1F);
            ParticleHelper.sendBatches(entity, new ParticleBatch[]{quen_break});
        }
        super.onRemoved(entity, attributes, amplifier);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        removeEffects(entity,true,false);
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float)(healthPerStack * (amplifier + 1)));
        super.onApplied(entity, attributes, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}