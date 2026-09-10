package net.witcher_rpg.effect;

import net.spell_engine.Platform;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.spell_engine.client.util.Color;
import net.witcher_rpg.network.ExposedGlowPayload;
import net.witcher_rpg.network.WitcherNetworking;

import java.util.UUID;
import net.witcher_rpg.util.MrpgAttributeIds;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.spell_engine.rpg_series.config.EffectConfig;
import net.spell_engine.api.effect.*;
import net.spell_engine.api.entity.SpellEngineAttributes;
import net.spell_power.api.SpellPowerMechanics;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.entity.attribute.WitcherAttributes;


import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;
import static net.witcher_rpg.WitcherClassMod.tweaksConfig;

public class WitcherStatusEffects {
    /// `EntityAttribute#getIdAsString()` is 1.21-only.
    private static String attributeId(EntityAttribute attribute) {
        return Registries.ATTRIBUTE.getId(attribute).toString();
    }

    public static final List<Effects.Entry> entries = new ArrayList<>();
    private static Effects.Entry add(Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static float sign_intensity_boost = 0.1F;
    public static float specific_sign_intensity_boost = 1.0F;

    public static Effects.Entry AARD_INTENSITY = add(new Effects.Entry(new Identifier(MOD_ID,"aard_intensity"),
            "Aard Sign Intensity",
            "Increases the damage of the Aard Sign.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.AARD.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.AARD_INTENSITY_ID.toString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            )
    ));
    public static Effects.Entry AXII_INTENSITY = add(new Effects.Entry(new Identifier(MOD_ID,"axii_intensity"),
            "Axii Sign Intensity",
            "Increases the damage of the Axii Sign.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.AXII.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.AXII_INTENSITY_ID.toString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            )
    ));
    public static Effects.Entry IGNI_INTENSITY = add(new Effects.Entry(new Identifier(MOD_ID,"igni_intensity"),
            "Igni Sign Intensity",
            "Increases the damage of the Igni Sign.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.IGNI.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.IGNI_INTENSITY_ID.toString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            )
    ));
    public static Effects.Entry QUEN_INTENSITY = add(new Effects.Entry(new Identifier(MOD_ID,"quen_intensity"),
            "Quen Sign Intensity",
            "Increases the damage of the Quen Sign.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.QUEN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.QUEN_INTENSITY_ID.toString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            )
    ));
    public static Effects.Entry YRDEN_INTENSITY = add(new Effects.Entry(new Identifier(MOD_ID,"yrden_intensity"),
            "Yrden Sign Intensity",
            "Increases the damage of the Yrden Sign.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.YRDEN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.YRDEN_INTENSITY_ID.toString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            )
    ));
    public static Effects.Entry SIGN_INTENSITY = add(new Effects.Entry(new Identifier(MOD_ID,"sign_intensity"),
            "Sign Intensity",
            "Increases the damage of all Witcher Signs.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.SIGN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.SIGN_INTENSITY_ID.toString(),
                                    specific_sign_intensity_boost,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            )
    ));
    public static Effects.Entry ADRENALINE_BURST = add(new Effects.Entry(new Identifier(MOD_ID,"adrenaline_burst"),
            "Adrenaline Burst",
            "Increases your Adrenaline Attribute.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.ADRENALINE_MODIFIER_ID.toString(),
                                    sign_intensity_boost,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry ADRENALINE_GAIN = add(new Effects.Entry(new Identifier(MOD_ID,"adrenaline_gain"),
            "Adrenaline",
            "Increases attack damage and sign intensity per stack.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.05F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            ),
                            new AttributeModifier(
                                    WitcherAttributes.SIGN_INTENSITY_ID.toString(),
                                    0.025F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )

                    )
            )
    ));
    public static Effects.Entry AERONDIGHT_CHARGE = add(new Effects.Entry(new Identifier(MOD_ID,"aerondight_charge"),
            "Charged Sword",
            "Increases sign intensity, spell crit chance & spell crit damage per stack.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xbce5fe),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_CHANCE.id,
                                    0.01F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            ),
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_DAMAGE.id,
                                    0.02F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            ),
                            new AttributeModifier(
                                    WitcherAttributes.SIGN_INTENSITY_ID.toString(),
                                    0.01F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry AXII = add(new Effects.Entry(new Identifier(MOD_ID,"axii"),
            "Axii",
            "Stuns the target and makes it more vulnerable to damage.",
            new AxiiEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.AXII.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry AXII_PUPPET = add(new Effects.Entry(new Identifier(MOD_ID,"axii_puppet"),
            "Axii Puppeteer",
            "Makes a hostile monster your ally for a short time and buffs its attack damage.",
            new AxiiPuppetEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.AXII.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry BATTLE_TRANCE = add(new Effects.Entry(new Identifier(MOD_ID,"battle_trance"),
            "Battle Trance",
            "Gain Attack Damage. Melee hits build up Adrenaline while active.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry BEAR_SCHOOL_MEDALLION = add(new Effects.Entry(new Identifier(MOD_ID,"bear_school_medallion"),
            "Bear School Endurance",
            "Incoming damage is reduced.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry QUEN_ACTIVE = add(new Effects.Entry(new Identifier(MOD_ID,"quen_active"),
            "Quen Active Shield",
            "Gives absorption and clears negative status effects, will be removed when no absorption hearts are active.",
            new QuenActiveEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.QUEN.color),
            // `generic.max_absorption` is a 1.20.5 attribute: on 1.20.1 absorption is an uncapped float on
            // `LivingEntity` and the effect sets the amount itself in `onApplied`, so the (cap-only)
            // modifier is dropped rather than left with an id that cannot resolve.
            new EffectConfig(List.of())
    ));
    public static Effects.Entry QUEN_SHIELD = add(new Effects.Entry(new Identifier(MOD_ID,"quen_shield"),
            "Quen Shield",
            "Gives absorption and clears negative status effects.",
            new QuenShieldEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.QUEN.color),
            // See QUEN_ACTIVE: no `generic.max_absorption` attribute on 1.20.1.
            new EffectConfig(List.of())
    ));
    public static Effects.Entry YRDEN_CIRCLE = add(new Effects.Entry(new Identifier(MOD_ID,"yrden_circle"),
            "Magical Trap",
            "Reduces movement speed, damages and traps undead targets.",
            new YrdenCircleEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.YRDEN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    -0.1F,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            )
    ));
    public static Effects.Entry YRDEN_GLYPH = add(new Effects.Entry(new Identifier(MOD_ID,"yrden_glyph"),
            "Yrden Glyph",
            "Reduces movement speed.",
            new YrdenCircleEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.YRDEN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    -0.1F,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            )
    ));
    public static Effects.Entry WOLF_SCHOOL_MEDALLION = add(new Effects.Entry(new Identifier(MOD_ID,"wolf_school_medallion"),
            "Wolf School Sign Blade",
            "Deals magical damage per melee hit.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color)
    ));
    public static Effects.Entry AZURE_WRATH = add(new Effects.Entry(new Identifier(MOD_ID,"azure_wrath"),
            "Azure Wrath",
            "Stops healing and regeneration.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                List.of(
                        new AttributeModifier(
                        SpellEngineAttributes.HEALING_TAKEN.id,
                                -1.0F,
                        EntityAttributeModifier.Operation.MULTIPLY_BASE
                        )
                )
            )
    ));
    public static Effects.Entry IRIS_CHARGE = add(new Effects.Entry(new Identifier(MOD_ID,"iris_charge"),
            "Charged Sword",
            "Increases attack damage and lifesteal per stack.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xbce5fe),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.025F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            ),
                            new AttributeModifier(
                                    MrpgAttributeIds.LIFESTEAL_MODIFIER,
                                    0.01F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )

                    )
            )
    ));
    public static Effects.Entry ROSE_OF_REMEMBRANCE = add(new Effects.Entry(new Identifier(MOD_ID,"rose_of_remembrance"),
            "Rose of Remembrance",
            "Heals 5% of your max health every 2 seconds.",
            new RoseOfRemembranceEffect(StatusEffectCategory.BENEFICIAL, 0xbce5fe),
            new EffectConfig(
                    List.of()
            )
    ));
    public static Effects.Entry SUNSTONE = add(new Effects.Entry(new Identifier(MOD_ID,"sunstone"),
            "Sunstone",
            "Boosts Sign Intensity.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.SIGN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.SIGN_INTENSITY_ID.toString(),
                                    0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry YRDEN_GRIFFIN_MASTER = add(new Effects.Entry(new Identifier(MOD_ID,"yrden_griffin_master"),
            "Yrden Griffin Boost",
            "Boosts Sign Intensity & Reduces incoming Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.SIGN.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    WitcherAttributes.SIGN_INTENSITY_ID.toString(),
                                    0.5F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            ),
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry FELINE_INJURY_MASTER = add(new Effects.Entry(new Identifier(MOD_ID,"feline_injury_master"),
            "Feline Injury Master",
            "Increases Incoming Damage and reduces Movement Speed.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            ),
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    -0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry AXII_LETHARGY = add(new Effects.Entry(new Identifier(MOD_ID,"axii_lethargy"),
            "Axii Lethargy",
            "Slows the Target.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.AXII.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    -0.1F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry QUEN_EXPLOSIVE = add(new Effects.Entry(new Identifier(MOD_ID,"quen_explosive"),
            "Exploding Shield",
            "Deals damage and knocks surrounding entities back when quen breaks.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.QUEN.color),
            new EffectConfig(
                    List.of(
                    )
            )
    ));
    public static Effects.Entry QUEN_DISCHARGE= add(new Effects.Entry(new Identifier(MOD_ID,"quen_discharge"),
            "Quen Discharge",
            "Deals damage to the attacker.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.QUEN.color),
            new EffectConfig(
                    List.of(
                    )
            )
    ));
    public static Effects.Entry MUSCLE_MEMORY = add(new Effects.Entry(new Identifier(MOD_ID,"muscle_memory"),
            "Muscle Memory",
            "Increases attack speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_ATTACK_SPEED),
                                    0.05F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry STRENGTH_TRAINING = add(new Effects.Entry(new Identifier(MOD_ID,"strength_training"),
            "Strength Training",
            "Increases attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.025F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry WITCHER_SENSES_EXPOSED = add(new Effects.Entry(new Identifier(MOD_ID,"witcher_senses_exposed"),
            "Exposed",
            "Weakness exposed by Witcher Senses, taking increased critical hits.",
            new WitcherSensesExposedEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.SIGN.color),
            new EffectConfig(
                    List.of(
                    )
            )
    ));

    public static Effects.Entry COUNTERATTACK_READY = add(new Effects.Entry(new Identifier(MOD_ID,"counterattack_ready"),
            "Counterattack Ready",
            "Your next melee attack deals massively increased damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    1.0F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry RESOLVE = add(new Effects.Entry(new Identifier(MOD_ID,"resolve"),
            "Resolve",
            "Reduces incoming damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.25F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry FOOTWORK = add(new Effects.Entry(new Identifier(MOD_ID,"footwork"),
            "Footwork",
            "Increases evasion chance and movement speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.EVASION_CHANCE.id,
                                    0.15F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            ),
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry ENEMY_KNOWLEDGE = add(new Effects.Entry(new Identifier(MOD_ID,"enemy_knowledge"),
            "Enemy Knowledge",
            "Reduces damage taken from enemies you've exposed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.DAMAGE_TAKEN.id,
                                    -0.25F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry FLOOD_OF_ANGER = add(new Effects.Entry(new Identifier(MOD_ID,"flood_of_anger"),
            "Flood of Anger",
            "Increases attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, WitcherSpellSchools.WITCHER_MELEE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE),
                                    0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry SCORCHED = add(new Effects.Entry(new Identifier(MOD_ID,"scorched"),
            "Scorched",
            "Reduces movement speed.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, WitcherSpellSchools.IGNI.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    attributeId(EntityAttributes.GENERIC_MOVEMENT_SPEED),
                                    -0.2F,
                                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                            )
                    )
            )
    ));

    private static boolean configured = false;

    /// Everything {@link #register(ConfigFile.Effects)} does *before* handing the entries to Spell
    /// Engine: config-driven attribute values, the impairing / removal / glow behaviours and the
    /// synchronisation flag. Split out so Forge's `STATUS_EFFECT` `RegisterEvent` window can run it
    /// ahead of its own registration loop. Idempotent.
    public static void configure() {
        if (configured) {
            return;
        }
        configured = true;
        BATTLE_TRANCE.config().attributes().get(0).value = tweaksConfig.value.battle_trance_attack_damage_bonus;
        ADRENALINE_GAIN.config().attributes().get(0).value = tweaksConfig.value.battle_trance_damage_per_adrenaline_level;

        ActionImpairing.configure(AXII.effect, EntityActionsAllowed.STUN);
        RemoveOnHit.configure(AXII.effect, RemoveOnHit.Trigger.ANY_HIT);

        GlowingItemStatusEffect.register(IRIS_CHARGE.effect, Color.BLOOD, 0.1F);
        GlowingItemStatusEffect.register(AERONDIGHT_CHARGE.effect, Color.ARCANE, 0.1F);
        GlowingItemStatusEffect.register(WOLF_SCHOOL_MEDALLION.effect, Color.from(WitcherSpellSchools.SIGN.color), 0.1F);

        OnRemoval.configure(WITCHER_SENSES_EXPOSED.effect, (context) -> {
            var entity = context.entity();
            UUID source = WitcherExposed.remove(entity.getUuid());
            if (source != null && entity.getWorld() instanceof ServerWorld serverWorld) {
                ServerPlayerEntity player = serverWorld.getServer().getPlayerManager().getPlayer(source);
                if (player != null) {
                    WitcherNetworking.sendToPlayer(player, new ExposedGlowPayload(entity.getId(), false));
                }
            }
        });

        OnRemoval.configure(QUEN_SHIELD.effect, (context) -> {
            QuenShieldEffect.onRemove(context.entity());
            if (context.entity().hasStatusEffect(QUEN_EXPLOSIVE.effect)) {
                context.entity().removeStatusEffect(QUEN_EXPLOSIVE.effect);
            }
            if (context.entity().hasStatusEffect(QUEN_DISCHARGE.effect)) {
                context.entity().removeStatusEffect(QUEN_DISCHARGE.effect);
            }
        });
        OnRemoval.configure(QUEN_ACTIVE.effect, (context) -> {
            QuenActiveEffect.onRemove(context.entity());
            if (context.entity().hasStatusEffect(QUEN_EXPLOSIVE.effect)) {
                context.entity().removeStatusEffect(QUEN_EXPLOSIVE.effect);
            }
            if (context.entity().hasStatusEffect(QUEN_DISCHARGE.effect)) {
                context.entity().removeStatusEffect(QUEN_DISCHARGE.effect);
            }
        });

        for (var entry: entries) {
            Synchronized.configure(entry.effect, true);
        }

    }

    /// Creation only - Forge's `STATUS_EFFECT` `RegisterEvent` window feeds the returned map to its own
    /// `RegisterHelper`, then calls `Effects.linkEntries(entries)` (the helper returns void where
    /// `Registry.registerReference` returned the `RegistryEntry` the Fabric path stores).
    public static Map<Identifier, StatusEffect> effectsToRegister(ConfigFile.Effects config) {
        configure();
        return Effects.effectsToRegister(entries, config.effects);
    }

    public static void register(ConfigFile.Effects config) {
        configure();
        Effects.register(entries, config.effects);
    }

}
