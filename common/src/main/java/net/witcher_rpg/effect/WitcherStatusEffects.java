package net.witcher_rpg.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.entity.attribute.MRPGCEntityAttributes;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.api.config.EffectConfig;
import net.spell_engine.api.effect.*;
import net.spell_engine.api.entity.SpellEngineAttributes;
import net.spell_engine.api.event.CombatEvents;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.fx.ParticleHelper;
import net.spell_power.api.SpellPowerMechanics;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.entity.attribute.WitcherAttributes;

import java.util.ArrayList;
import java.util.List;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherStatusEffects {
    public static final List<Effects.Entry> entries = new ArrayList<>();
    private static Effects.Entry add(Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static float sign_intensity_boost = 0.1F;
    public static float specific_sign_intensity_boost = 1.0F;

    public static Effects.Entry AARD_INTENSITY = add(new Effects.Entry(Identifier.of(MOD_ID,"aard_intensity"),
            "Aard Sign Intensity",
            "Increases the damage of the Aard Sign.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.AARD.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.AARD_INTENSITY.getIdAsString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry AXII_INTENSITY = add(new Effects.Entry(Identifier.of(MOD_ID,"axii_intensity"),
            "Axii Sign Intensity",
            "Increases the damage of the Axii Sign.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.AXII.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.AXII_INTENSITY.getIdAsString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry IGNI_INTENSITY = add(new Effects.Entry(Identifier.of(MOD_ID,"igni_intensity"),
            "Igni Sign Intensity",
            "Increases the damage of the Igni Sign.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.IGNI.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.IGNI_INTENSITY.getIdAsString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry QUEN_INTENSITY = add(new Effects.Entry(Identifier.of(MOD_ID,"quen_intensity"),
            "Quen Sign Intensity",
            "Increases the damage of the Quen Sign.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.QUEN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.QUEN_INTENSITY.getIdAsString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry YRDEN_INTENSITY = add(new Effects.Entry(Identifier.of(MOD_ID,"yrden_intensity"),
            "Yrden Sign Intensity",
            "Increases the damage of the Yrden Sign.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.YRDEN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.YRDEN_INTENSITY.getIdAsString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry SIGN_INTENSITY = add(new Effects.Entry(Identifier.of(MOD_ID,"sign_intensity"),
            "Sign Intensity",
            "Increases the damage of all Witcher Signs.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.SIGN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.SIGN_INTENSITY.getIdAsString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry ADRENALINE_BURST = add(new Effects.Entry(Identifier.of(MOD_ID,"adrenaline_burst"),
            "Adrenaline Burst",
            "Increases your Adrenaline Attribute.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.ADRENALINE_MODIFIER.getIdAsString(),
                                    sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry ADRENALINE_GAIN = add(new Effects.Entry(Identifier.of(MOD_ID,"adrenaline_gain"),
            "Adrenaline",
            "Increases attack damage and sign intensity per stack.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    0.025F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    WitcherAttributes.SIGN_INTENSITY.getIdAsString(),
                                    0.025F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )

                    )
            )
    ));
    public static Effects.Entry AERONDIGHT_CHARGE = add(new Effects.Entry(Identifier.of(MOD_ID,"aerondight_charge"),
            "Charged Sword",
            "Increases sign intensity, spell crit chance & spell crit damage per stack.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xbce5fe),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_CHANCE.id,
                                    0.01F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_DAMAGE.id,
                                    0.02F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    WitcherAttributes.SIGN_INTENSITY.getIdAsString(),
                                    0.01F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry AXII = add(new Effects.Entry(Identifier.of(MOD_ID,"axii"),
            "Axii",
            "Stuns the target and makes it more vulnerable to damage.",
            new AxiiEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.AXII.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry AXII_PUPPET = add(new Effects.Entry(Identifier.of(MOD_ID,"axii_puppet"),
            "Axii Puppeteer",
            "Makes a hostile monster your ally for a short time and buffs its attack damage.",
            new AxiiPuppetEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.AXII.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry BATTLE_TRANCE = add(new Effects.Entry(Identifier.of(MOD_ID,"battle_trance"),
            "Battle Trance",
            "Gain Movement Speed and deal 10% more melee damage for each Adrenaline Effect Amplifier.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    0.3F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry BEAR_SCHOOL_MEDALLION = add(new Effects.Entry(Identifier.of(MOD_ID,"bear_school_medallion"),
            "Bear School Endurance",
            "Incoming damage is reduced.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry QUEN_ACTIVE = add(new Effects.Entry(Identifier.of(MOD_ID,"quen_active"),
            "Quen Active Shield",
            "Gives absorption and clears negative status effects, will be removed when no absorption hearts are active.",
            new QuenActiveEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.QUEN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MAX_ABSORPTION.getIdAsString(),
                                    8F,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry QUEN_SHIELD = add(new Effects.Entry(Identifier.of(MOD_ID,"quen_shield"),
            "Quen Shield",
            "Gives absorption and clears negative status effects.",
            new QuenShieldEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.QUEN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MAX_ABSORPTION.getIdAsString(),
                                    4F,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry YRDEN_CIRCLE = add(new Effects.Entry(Identifier.of(MOD_ID,"yrden_circle"),
            "Magical Trap",
            "Reduces movement speed, damages and traps undead targets.",
            new YrdenCircleEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.YRDEN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    -0.1F,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry YRDEN_GLYPH = add(new Effects.Entry(Identifier.of(MOD_ID,"yrden_glyph"),
            "Yrden Glyph",
            "Reduces movement speed.",
            new YrdenCircleEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.YRDEN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    -0.1F,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry WOLF_SCHOOL_MEDALLION = add(new Effects.Entry(Identifier.of(MOD_ID,"wolf_school_medallion"),
            "Wolf School Sign Blade",
            "Deals magical damage per melee hit.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color)
    ));
    public static Effects.Entry AZURE_WRATH = add(new Effects.Entry(Identifier.of(MOD_ID,"azure_wrath"),
            "Azure Wrath",
            "Stops healing and regeneration.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                List.of(
                        new AttributeModifier(
                        SpellEngineAttributes.HEALING_TAKEN.id,
                                -1.0F,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        )
                )
            )
    ));
    public static Effects.Entry IRIS_CHARGE = add(new Effects.Entry(Identifier.of(MOD_ID,"iris_charge"),
            "Charged Sword",
            "Increases attack damage and lifesteal per stack.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xbce5fe),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    0.025F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    MRPGCEntityAttributes.LIFESTEAL_MODIFIER.getIdAsString(),
                                    0.01F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )

                    )
            )
    ));
    public static Effects.Entry ROSE_OF_REMEMBRANCE = add(new Effects.Entry(Identifier.of(MOD_ID,"rose_of_remembrance"),
            "Rose of Remembrance",
            "Heals 5% of your max health every 2 seconds.",
            new RoseOfRemembranceEffect(StatusEffectCategory.BENEFICIAL, 0xbce5fe),
            new EffectConfig(
                    List.of()
            )
    ));
    public static Effects.Entry SUNSTONE = add(new Effects.Entry(Identifier.of(MOD_ID,"sunstone"),
            "Sunstone",
            "Boosts Sign Intensity.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.SIGN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.SIGN_INTENSITY.getIdAsString(),
                                    0.2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry YRDEN_GRIFFIN_MASTER = add(new Effects.Entry(Identifier.of(MOD_ID,"yrden_griffin_master"),
            "Yrden Griffin Boost",
            "Boosts Sign Intensity & Reduces incoming Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.SIGN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.SIGN_INTENSITY.getIdAsString(),
                                    0.5F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry FELINE_INJURY_MASTER = add(new Effects.Entry(Identifier.of(MOD_ID,"feline_injury_master"),
            "Feline Injury Master",
            "Increases Incoming Damage and reduces Movement Speed.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    0.2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    -0.2F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry AXII_LETHARGY = add(new Effects.Entry(Identifier.of(MOD_ID,"axii_lethargy"),
            "Axii Lethargy",
            "Slows the Target.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.AXII.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    -0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry QUEN_EXPLOSIVE = add(new Effects.Entry(Identifier.of(MOD_ID,"quen_explosive"),
            "Exploding Shield",
            "Deals damage and knocks surrounding entities back when quen breaks.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.QUEN.color),
            new EffectConfig(
                    List.of(
                    )
            )
    ));
    public static Effects.Entry QUEN_DISCHARGE= add(new Effects.Entry(Identifier.of(MOD_ID,"quen_discharge"),
            "Quen Discharge",
            "Deals damage to the attacker.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.QUEN.color),
            new EffectConfig(
                    List.of(
                    )
            )
    ));
    public static Effects.Entry MUSCLE_MEMORY = add(new Effects.Entry(Identifier.of(MOD_ID,"muscle_memory"),
            "Muscle Memory",
            "Increases attack speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString(),
                                    0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry STRENGTH_TRAINING = add(new Effects.Entry(Identifier.of(MOD_ID,"strength_training"),
            "Strength Training",
            "Increases attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    0.025F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry WITCHER_REFLEXES = add(new Effects.Entry(Identifier.of(MOD_ID,"witcher_reflexes"),
            "Witcher Reflexes",
            "Blocks the next Arrow or Melee Impact",
            new WitcherReflexesEffect(StatusEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig(
                    List.of(
                    )
            )
    ));
    public static Effects.Entry ARROW_DEFLECTION = add(new Effects.Entry(Identifier.of(MOD_ID,"arrow_deflection"),
            "Arrow Deflection",
            "While blocking Arrows with Witcher Reflexes, you send the Arrow back to the shooter.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig(
                    List.of(
                    )
            )
    ));
    public static Effects.Entry COUNTERATTACK = add(new Effects.Entry(Identifier.of(MOD_ID,"counterattack"),
            "Counterattack",
            "After a successful block, your next melee attack deals increased damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    0.3F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static void register(ConfigFile.Effects config) {
        ActionImpairing.configure(AXII.effect, EntityActionsAllowed.STUN);
        RemoveOnHit.configure(AXII.effect, RemoveOnHit.Trigger.ANY_HIT);

        OnRemoval.configure(QUEN_SHIELD.effect, (context) -> {
            QuenShieldEffect.onRemove(context.entity());
            if (context.entity().hasStatusEffect(QUEN_EXPLOSIVE.entry)) {
                context.entity().removeStatusEffect(QUEN_EXPLOSIVE.entry);
            }
            if (context.entity().hasStatusEffect(QUEN_DISCHARGE.entry)) {
                context.entity().removeStatusEffect(QUEN_DISCHARGE.entry);
            }
        });
        OnRemoval.configure(QUEN_ACTIVE.effect, (context) -> {
            QuenActiveEffect.onRemove(context.entity());
            if (context.entity().hasStatusEffect(QUEN_EXPLOSIVE.entry)) {
                context.entity().removeStatusEffect(QUEN_EXPLOSIVE.entry);
            }
            if (context.entity().hasStatusEffect(QUEN_DISCHARGE.entry)) {
                context.entity().removeStatusEffect(QUEN_DISCHARGE.entry);
            }
        });
        OnRemoval.configure(WITCHER_REFLEXES.effect, (context) -> {
            RegistryEntry<StatusEffect> effect = ARROW_DEFLECTION.entry;
            if (context.entity().hasStatusEffect(effect)) {
                int amplifier = context.entity().getStatusEffect(effect).getAmplifier();
                int duration = context.entity().getStatusEffect(effect).getDuration();
                if(amplifier == 0){
                    context.entity().removeStatusEffect(effect);
                }else{
                    context.entity().removeStatusEffect(effect);
                    context.entity().addStatusEffect(new StatusEffectInstance(effect,
                            duration,amplifier-1,false,false,true));
                }
            }
        });
        CombatEvents.PLAYER_MELEE_ATTACK.register((event) -> {
            RegistryEntry<StatusEffect> effect = COUNTERATTACK.entry;
            if (event.player().hasStatusEffect(effect)) {
                event.player().removeStatusEffect(effect);
            }
        });

        for (var entry: entries) {
            Synchronized.configure(entry.effect, true);
        }

        Effects.register(entries, config.effects);
    }

}
