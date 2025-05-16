package net.witcher_rpg.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.entity.attribute.MRPGCEntityAttributes;
import net.spell_engine.api.effect.*;
import net.spell_engine.api.entity.SpellEngineAttributes;
import net.spell_power.api.SpellPowerMechanics;
import net.witcher_rpg.entity.attribute.WitcherAttributes;

import java.util.ArrayList;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;
import static net.witcher_rpg.WitcherClassMod.effectsConfig;

public class Effects {
    private static final ArrayList<Entry> entries = new ArrayList<>();
    public static class Entry {
        public final Identifier id;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public Entry(String name, StatusEffect effect) {
            this.id = Identifier.of(MOD_ID, name);
            this.effect = effect;
            entries.add(this);
        }

        public void register() {
            registryEntry = Registry.registerReference(Registries.STATUS_EFFECT, id, effect);
        }

        public Identifier modifierId() {
            return Identifier.of(MOD_ID, "effect." + id.getPath());
        }
    }

    public static final Entry AERONDIGHT_CHARGE =  new Entry("aerondight_charge",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0xbce5fe));
    public static final Entry AARD_INTENSITY =  new Entry("aard_intensity",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0x3beeff));
    public static final Entry AXII = new  Entry("axii",
            new AxiiEffect(StatusEffectCategory.HARMFUL, 0x008000));
    public static final Entry AXII_INTENSITY = new Entry("axii_intensity",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0x008000));
    public static final Entry AXII_PUPPET = new Entry("axii_puppet",
            new AxiiPuppetEffect(StatusEffectCategory.HARMFUL, 0x008000));
    public static final Entry IGNI_INTENSITY = new Entry("igni_intensity",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0xdd4e00));
    public static final Entry QUEN_EXPLODE = new Entry("quen_explode",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0xfffeca));
    public static final Entry QUEN_SHIELD = new Entry("quen_shield",
            new QuenShieldEffect(StatusEffectCategory.BENEFICIAL, 0xfffeca));
    public static final Entry QUEN_ACTIVE = new Entry("quen_active",
            new QuenActiveEffect(StatusEffectCategory.BENEFICIAL, 0xfffeca));
    public static final Entry QUEN_INTENSITY = new Entry("quen_intensity",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0xfffeca));
    public static final Entry YRDEN_INTENSITY = new Entry("yrden_intensity",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0xe717fe));
    public static final Entry YRDEN_CIRCLE = new Entry("yrden_circle",
            new YrdenCircleEffect(StatusEffectCategory.HARMFUL, 0xe717fe));
    public static final Entry YRDEN_GLYPH = new Entry("yrden_glyph",
            new CustomEffect(StatusEffectCategory.HARMFUL, 0xe717fe));
    public static final Entry ADRENALINE_GAIN = new Entry("adrenaline_gain",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0xdd4e00));
    public static final Entry BATTLE_TRANCE = new Entry("battle_trance",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0xb3b3b3));
    public static final Entry STAGGER = new Entry("stagger",
            new CustomEffect(StatusEffectCategory.HARMFUL, 0xb3b3b3));
    public static final Entry SIGN_INTENSITY = new Entry("sign_intensity",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0xfffeca));
    public static final Entry ADRENALINE_BURST = new Entry("adrenaline_burst",
            new CustomEffect(StatusEffectCategory.BENEFICIAL, 0xdd4e00));

    public static void register(){
        AERONDIGHT_CHARGE.effect.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, AERONDIGHT_CHARGE.modifierId(),
                        effectsConfig.value.aerondight_attack_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry, AERONDIGHT_CHARGE.modifierId(),
                        effectsConfig.value.aerondight_spell_crit_chance_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry, AERONDIGHT_CHARGE.modifierId(),
                        effectsConfig.value.aerondight_spell_crit_damage_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        YRDEN_CIRCLE.effect.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, YRDEN_CIRCLE.modifierId(),
                effectsConfig.value.yrden_magical_trap_speed_reduction, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        YRDEN_GLYPH.effect.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, YRDEN_GLYPH.modifierId(),
                effectsConfig.value.yrden_magical_trap_speed_reduction, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        ADRENALINE_GAIN.effect.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, ADRENALINE_GAIN.modifierId(),
                effectsConfig.value.adrenaline_attack_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(WitcherAttributes.SIGN_INTENSITY, ADRENALINE_GAIN.modifierId(),
                        effectsConfig.value.adrenaline_sign_intensity_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER,ADRENALINE_GAIN.modifierId(),
                effectsConfig.value.adrenaline_attribute_increase_per_stack,EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        QUEN_EXPLODE.effect.addAttributeModifier(MRPGCEntityAttributes.DAMAGE_REFLECT_MODIFIER, QUEN_EXPLODE.modifierId(),
                effectsConfig.value.quen_reflect_damage, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        AARD_INTENSITY.effect.addAttributeModifier(WitcherAttributes.AARD_INTENSITY, AARD_INTENSITY.modifierId(),
                effectsConfig.value.sign_school_intensity_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        AXII_INTENSITY.effect.addAttributeModifier(WitcherAttributes.AXII_INTENSITY, AXII_INTENSITY.modifierId(),
                effectsConfig.value.sign_school_intensity_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        IGNI_INTENSITY.effect.addAttributeModifier(WitcherAttributes.IGNI_INTENSITY, IGNI_INTENSITY.modifierId(),
                effectsConfig.value.sign_school_intensity_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        QUEN_INTENSITY.effect.addAttributeModifier(WitcherAttributes.QUEN_INTENSITY, QUEN_INTENSITY.modifierId(),
                effectsConfig.value.sign_school_intensity_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        YRDEN_INTENSITY.effect.addAttributeModifier(WitcherAttributes.YRDEN_INTENSITY, YRDEN_INTENSITY.modifierId(),
                effectsConfig.value.sign_school_intensity_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        AXII_PUPPET.effect.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, AXII_PUPPET.modifierId(),
                effectsConfig.value.axii_puppet_attack_damage_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        BATTLE_TRANCE.effect.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, BATTLE_TRANCE.modifierId(),
                effectsConfig.value.battle_trance_speed_increase, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        QUEN_SHIELD.effect.addAttributeModifier(EntityAttributes.GENERIC_MAX_ABSORPTION,
                QUEN_SHIELD.modifierId(), 4, EntityAttributeModifier.Operation.ADD_VALUE);
        QUEN_ACTIVE.effect.addAttributeModifier(EntityAttributes.GENERIC_MAX_ABSORPTION,
                QUEN_ACTIVE.modifierId(), 8, EntityAttributeModifier.Operation.ADD_VALUE);
        STAGGER.effect.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, STAGGER.modifierId(),
                effectsConfig.value.stagger_attack_armor_speed_decrease, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(EntityAttributes.GENERIC_ARMOR, STAGGER.modifierId(),
                        effectsConfig.value.stagger_attack_armor_speed_decrease, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, STAGGER.modifierId(),
                effectsConfig.value.stagger_attack_armor_speed_decrease, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        SIGN_INTENSITY.effect.addAttributeModifier(WitcherAttributes.SIGN_INTENSITY, SIGN_INTENSITY.modifierId(),
                effectsConfig.value.sign_school_intensity_increase_per_stack, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        ADRENALINE_BURST.effect.addAttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER, ADRENALINE_BURST.modifierId(),
                effectsConfig.value.adrenaline_burst_increase, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        AXII.effect.addAttributeModifier(
                SpellEngineAttributes.DAMAGE_TAKEN.entry, AXII.modifierId(),
                effectsConfig.value.axii_damage_increase, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );



        Synchronized.configure(AERONDIGHT_CHARGE.effect,true);
        Synchronized.configure(QUEN_SHIELD.effect,true);
        Synchronized.configure(YRDEN_CIRCLE.effect,true);
        Synchronized.configure(YRDEN_GLYPH.effect,true);
        Synchronized.configure(AXII.effect,true);
        Synchronized.configure(ADRENALINE_GAIN.effect,true);
        Synchronized.configure(QUEN_EXPLODE.effect,true);
        Synchronized.configure(AARD_INTENSITY.effect,true);
        Synchronized.configure(AXII_INTENSITY.effect,true);
        Synchronized.configure(IGNI_INTENSITY.effect,true);
        Synchronized.configure(QUEN_INTENSITY.effect,true);
        Synchronized.configure(YRDEN_INTENSITY.effect,true);
        Synchronized.configure(QUEN_ACTIVE.effect,true);
        Synchronized.configure(BATTLE_TRANCE.effect,true);
        Synchronized.configure(STAGGER.effect,true);
        Synchronized.configure(SIGN_INTENSITY.effect,true);
        Synchronized.configure(ADRENALINE_BURST.effect,true);

        OnRemoval.configure(QUEN_SHIELD.effect, (context) -> {
            QuenShieldEffect.onRemove(context.entity());
        });
        OnRemoval.configure(QUEN_ACTIVE.effect, (context) -> {
            QuenActiveEffect.onRemove(context.entity());
        });


        RemoveOnHit.configure(AXII.effect, true);

        ActionImpairing.configure(AXII.effect, EntityActionsAllowed.STUN);
        ActionImpairing.configure(STAGGER.effect, EntityActionsAllowed.INCAPACITATE);


        for (var entry: entries) {
            entry.register();
        }
    }

}
