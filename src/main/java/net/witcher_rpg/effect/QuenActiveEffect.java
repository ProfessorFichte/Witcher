package net.witcher_rpg.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.fx.ParticleHelper;

import static net.more_rpg_classes.util.CustomMethods.clearNegativeEffects;
import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class QuenActiveEffect extends StatusEffect {
    public static final Identifier QUEN_BREAK_ID = Identifier.of(MOD_ID, "quen_sign_break");
    public static final SoundEvent QUEN_BREAK = SoundEvent.of(QUEN_BREAK_ID );
    private final int healthPerStack;
    public static final ParticleBatch quen_break = new ParticleBatch(
            "spell_engine:electric_arc_a",
            ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
            null, 8, 0.03F, 0.7F,360 ,3);



    public QuenActiveEffect(StatusEffectCategory category, int color) {
        super(category, color);
        this.healthPerStack = 6;
    }

    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.getWorld().isClient) {
            float currentAbsorption = entity.getAbsorptionAmount();
            if(currentAbsorption == 0 ){
                entity.removeStatusEffect(WitcherStatusEffects.QUEN_ACTIVE.entry);
            }
        }
        return entity.getAbsorptionAmount() > 0.0F || entity.getWorld().isClient;
    }

    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        clearNegativeEffects(entity,true);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(healthPerStack * (1 + amplifier))));
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public static void onRemove(LivingEntity entity) {
        float currentAbsorption = entity.getAbsorptionAmount();
        if(currentAbsorption == 0 ){
            entity.addStatusEffect(new StatusEffectInstance(MRPGCEffects.STUNNED.registryEntry,20,0,false,false,false));
        }
        if (!entity.getWorld().isClient()) {
            entity.getWorld().playSoundFromEntity(null, entity, QUEN_BREAK, SoundCategory.PLAYERS, 1F, 1F);
            ParticleHelper.sendBatches(entity, new ParticleBatch[]{quen_break});
        }
    }
}
