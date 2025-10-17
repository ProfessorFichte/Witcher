package net.witcher_rpg.spell;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.effect.TickingStatusEffect;
import net.spell_engine.api.entity.SpellEntityPredicates;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_power.api.SpellSchools;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import net.witcher_rpg.sounds.Sounds;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static net.spell_engine.client.util.Color.from;
import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherSpells {
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator) {
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }
    private static Spell activeSpellBase() {
        var spell = new Spell();
        spell.type = Spell.Type.ACTIVE;
        spell.active = new Spell.Active();
        spell.active.cast = new Spell.Active.Cast();

        spell.learn = new Spell.Learn();

        return spell;
    }

    private static Spell passiveSpellBase() {
        var spell = new Spell();
        spell.range = 0;
        spell.tier = 7;

        spell.type = Spell.Type.PASSIVE;
        spell.passive = new Spell.Passive();

        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        return spell;
    }

    private static Spell modifierSpellBase() {
        var spell = new Spell();
        spell.range = 0;
        spell.tier = 1;

        spell.type = Spell.Type.MODIFIER;

        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, true);
        spell.tooltip.description.color = Formatting.GRAY.asString();
        spell.tooltip.description.show_in_compact = true;
        spell.tooltip.name.show_in_compact = false;
        spell.tooltip.name.show_in_details = false;
        spell.tooltip.show_header = false;

        return spell;
    }

    private static Spell createModifierAlikePassiveSpell() {
        var spell = SpellBuilder.createSpellPassive();
        spell.range = 0;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_activation = false;
        return spell;
    }

    private static Spell.Impact createEffectImpact(Identifier effectId, float duration) {
        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effectId.toString();
        buff.action.status_effect.duration = duration;
        return buff;
    }

    private static Spell.Impact createHeal(float coefficient) {
        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.HEAL;
        buff.action.heal = new Spell.Impact.Action.Heal();
        buff.action.heal.spell_power_coefficient = coefficient;
        return buff;
    }

    private static Spell.Impact createDamage(float coefficient, float knockback) {
        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.DAMAGE;
        buff.action.damage = new Spell.Impact.Action.Damage();
        buff.action.damage.spell_power_coefficient = coefficient;
        buff.action.damage.knockback = knockback;
        return buff;
    }

    private static Spell.Impact.TargetModifier createImpactModifier(String entityType) {
        var condition = new Spell.TargetCondition();
        condition.entity_type = entityType;
        var modifier = new Spell.Impact.TargetModifier();
        modifier.conditions = List.of(condition);
        return modifier;
    }

    private static void configureCooldown(Spell spell, float duration) {
        if (spell.cost == null) {
            spell.cost = new Spell.Cost();
        }
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = duration;
    }

    public static float sign_vulnerability = 0.3F;
    private static Spell.Impact.TargetModifier extraDamageAard() {
        var modifier = createImpactModifier("#witcher_rpg:aard_vulnerable");
        var powerModifier = new Spell.Impact.Modifier();
        powerModifier.power_multiplier = sign_vulnerability;
        modifier.modifier = powerModifier;
        return modifier;
    }
    private static Spell.Impact.TargetModifier extraDamageIgni() {
        var modifier = createImpactModifier("#witcher_rpg:igni_vulnerable");
        var powerModifier = new Spell.Impact.Modifier();
        powerModifier.power_multiplier = sign_vulnerability;
        modifier.modifier = powerModifier;
        return modifier;
    }
    private static void axiiDeny(Spell.Impact impact) {
        var modifier = createImpactModifier("#witcher_rpg:axii_effect_immune");
        modifier.execute = TriState.DENY;
        impact.target_modifiers = List.of(modifier);
    }
    private static void undeadDeny(Spell.Impact impact) {
        var modifier = createImpactModifier("#minecraft:undead");
        modifier.execute = TriState.DENY;
        impact.target_modifiers = List.of(modifier);
    }
    private static void silverVulnerabilityAllow(Spell.Impact impact) {
        var modifier = createImpactModifier("#witcher_rpg:silver_vulnerable");
        modifier.execute = TriState.ALLOW;
        impact.target_modifiers = List.of(modifier);
    }
    private static void freezeImmunityDeny(Spell.Impact impact) {
        var modifier = createImpactModifier("#minecraft:freeze_immune_entity_types");
        modifier.execute = TriState.DENY;
        impact.target_modifiers = List.of(modifier);
    }
    private static void yrdenAllow(Spell.Impact impact) {
        var modifier = createImpactModifier("#witcher_rpg:yrden_vulnerable");
        modifier.execute = TriState.ALLOW;
        impact.target_modifiers = List.of(modifier);
    }
    private static final SpellEntityPredicates.Entry HAS_YRDEN =
            SpellEntityPredicates.hasEffectOptimized(Identifier.of("witcher_rpg", "yrden"));

    ///ACTIVE SPELLS
    public static final Entry AARD = add(aard());
    private static Entry aard() {
        var id = Identifier.of(MOD_ID, "aard");
        var title = "Aard";
        var description = "Telekinetic thrust that knocks targets back and deals {damage} sign-damage around the caster.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.AARD;
        spell.range = 7.5F;
        spell.tier = 1;

        spell.release.animation = "witcher_rpg:sign_cast_short";
        spell.release.sound = new Sound(Sounds.AARD_SIGN_ID);
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:aard_sign_cast",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.CENTER,
                        4, 0.01F, 0.1F),
                new ParticleBatch("more_rpg_classes:wind_vacuum",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.LAUNCH_POINT,
                        1, 0.1F, 1.0F),
                new ParticleBatch("small_gust",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        30, 0.25F, 0.25F)
        };

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.angle_degrees= 90;

        var custom = new Spell.Impact();
        custom.action = new Spell.Impact.Action();
        custom.action.custom = new Spell.Impact.Action.Custom();
        custom.action.type = Spell.Impact.Action.Type.CUSTOM;
        custom.action.custom.intent = SpellTarget.Intent.HARMFUL;
        custom.action.custom.handler = "more_rpg_classes:stop_arrows";

        var damage = new Spell.Impact();
        damage.target_modifiers = List.of(extraDamageAard());
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.75F;
        damage.action.damage.knockback = 5.0F;
        damage.particles = new ParticleBatch[]{
                new ParticleBatch("more_rpg_classes:wind_vacuum",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.LAUNCH_POINT,
                        1, 0.1F, 1.0F),
                new ParticleBatch("gust",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        10, 0.2F, 0.5F),
        };

        spell.impacts = List.of(damage,custom);
        configureCooldown(spell, 16);
        spell.cost.exhaust = 0.4F;

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry yrden = add(yrden());
    private static Entry yrden() {
        var id = Identifier.of(MOD_ID, "yrden");
        var title = "Yrden";
        var description = "Slows enemies for {cloud_duration} seconds, dealing {damage} to undead entities.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.YRDEN;
        spell.range = 0;
        spell.tier = 2;

        spell.release.animation = "witcher_rpg:sign_cast_ground";
        spell.release.sound = new Sound("witcher_rpg:yrden_sign");
        spell.release.particles = new ParticleBatch[]{ new ParticleBatch(
                "witcher_rpg:yrden_sign_cast",
                ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                1, 0.001F, 0.006F),
                new ParticleBatch(
                        "witcher_rpg:yrden_sign_cast",
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.CENTER,
                        1, 0.001F, 0.006F)
        };

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 3.0F;
        cloud.volume.area.vertical_range_multiplier = 1.5F;
        cloud.impact_tick_interval = 10;
        cloud.time_to_live_seconds = 10;
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.light_level = 14;
        cloud.client_data.model = new Spell.ProjectileModel();
        cloud.client_data.model.model_id = "witcher_rpg:effect/yrden_circle";
        cloud.client_data.model.scale = 3.0F;
        cloud.client_data.model.rotate_degrees_per_tick = 0;
        cloud.client_data.model.light_emission = LightEmission.RADIATE;
        cloud.client_data.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.ground_glow.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.GROUND,
                        1, 0, 0).scale(3.25F).color(Color.ARCANE.alpha(0.25F).toRGBA())
        };
        cloud.placement.force_onto_ground = true;
        cloud.placement.location_offset_y = 0;
        spell.deliver.clouds = List.of(cloud);

        var debuff = SpellBuilder.Impacts.effectSet(WitcherStatusEffects.YRDEN_CIRCLE.id.toString(), 1,0);
        debuff.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:yrden_cloud",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        2, 0.05F, 0.2F)
        };
        debuff.action.status_effect.amplifier_power_multiplier = 0.3F;
        debuff.sound = Sound.withRandomness(Identifier.of("witcher_rpg:yrden_sign"),0.2F);
        var damage = SpellBuilder.Impacts.damage(0.1F,0);
        yrdenAllow(damage);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.1F, 0.3F).extent(0.5F)
                        .color(Color.ARCANE.toRGBA()),
        };
        spell.impacts = List.of(debuff,damage);

        configureCooldown(spell, 18);
        spell.cost.exhaust = 0.4F;

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry yrden_glyph_impact = add(yrden_glyph_impact());
    private static Entry yrden_glyph_impact() {
        var id = Identifier.of(MOD_ID, "yrden_glyph_impact");
        var spell = activeSpellBase();
        var title = "Yrden Glyph Impact";
        var description = "The yrden glyph deals {damage} damage and slows nearby targets by {effect_duration} sec.";
        spell.school = WitcherSpellSchools.YRDEN;
        spell.range = 100;
        spell.tier = 1;

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        var debuff = SpellBuilder.Impacts.effectSet(WitcherStatusEffects.YRDEN_GLYPH.id.toString(), 6,0);
        debuff.action.status_effect.amplifier_power_multiplier = 0.1F;
        var damage = SpellBuilder.Impacts.damage(0.75F,0.1F);

        spell.impacts = List.of( damage, debuff);
        configureCooldown(spell, 1);

        return new Entry(id, spell, title, description, null);
    }

    ///GLYPH MODIFERS
    public static final Entry GREATER_AARD_GLYPH = add(greater_aard_glyph());
    private static Entry greater_aard_glyph() {
        var id = Identifier.of(MOD_ID, "greater_aard_glyph");
        var title = "Greater Aard Glyph";
        var description = "Increases critical chance of Aard Signs by {critical_chance_bonus}";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.AARD;

        var modifier = new Spell.Modifier();
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.critical_chance_bonus = 0.05F;
        var impactfilter = new Spell.Modifier.ImpactFilter();
        impactfilter.type = Spell.Impact.Action.Type.DAMAGE;
        impactfilter.school = WitcherSpellSchools.AARD;
        modifier.impact_filters = List.of(impactfilter);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry GREATER_AXII_GLYPH = add(greater_axii_glyph());
    private static Entry greater_axii_glyph() {
        var id = Identifier.of(MOD_ID, "greater_axii_glyph");
        var title = "Greater Axii Glyph";
        var description = "Increases duration of Axii Sign Effects by {effect_duration_add} sec";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.AXII;

        var modifier = new Spell.Modifier();
        modifier.effect_duration_add = 2;
        var impactfilter = new Spell.Modifier.ImpactFilter();
        impactfilter.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        impactfilter.school = WitcherSpellSchools.AXII;
        modifier.impact_filters = List.of(impactfilter);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry GREATER_IGNI_GLYPH = add(greater_igni_glyph());
    private static Entry greater_igni_glyph() {
        var id = Identifier.of(MOD_ID, "greater_igni_glyph");
        var title = "Greater Igni Glyph";
        var description = "Increases critical damage of Igni Signs by {critical_damage_bonus}";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.IGNI;

        var modifier = new Spell.Modifier();
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.critical_damage_bonus = 0.1F;
        var impactfilter = new Spell.Modifier.ImpactFilter();
        impactfilter.type = Spell.Impact.Action.Type.DAMAGE;
        impactfilter.school = WitcherSpellSchools.IGNI;
        modifier.impact_filters = List.of(impactfilter);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry GREATER_QUEN_GLYPH = add(greater_quen_glyph());
    private static Entry greater_quen_glyph() {
        var id = Identifier.of(MOD_ID, "greater_quen_glyph");
        var title = "Greater Quen Glyph";
        var description = "Quen Signs get {power_multiplier} more power bonus.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.QUEN;

        var modifier = new Spell.Modifier();
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.25F;
        var impactfilter = new Spell.Modifier.ImpactFilter();
        impactfilter.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        impactfilter.school = WitcherSpellSchools.QUEN;
        modifier.impact_filters = List.of(impactfilter);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry GREATER_YRDEN_GLYPH = add(greater_yrden_glyph());
    private static Entry greater_yrden_glyph() {
        var id = Identifier.of(MOD_ID, "greater_yrden_glyph");
        var title = "Greater Yrden Glyph";
        var description = "Increases the duration Yrden Signs by {spawn_duration_add} sec.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.YRDEN;

        var modifier = new Spell.Modifier();
        modifier.spawn_duration_add = 2;
        var impactfilter_spawn = new Spell.Modifier.ImpactFilter();
        impactfilter_spawn.school = WitcherSpellSchools.YRDEN;
        modifier.impact_filters = List.of(impactfilter_spawn);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    ///EQUIPMENT SET MODIFERS
    public static Entry improved_whirl = add(improved_whirl());
    private static Entry improved_whirl() {
        var id = Identifier.of(MOD_ID, "improved_whirl");
        var title = "Improved Whirl";
        var description = "Reduces cooldown of Whirl by {cooldown_duration_deduct} sec";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:whirl";
        modifier.cooldown_duration_deduct = 3;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    public static Entry improved_yrden = add(improved_yrden());
    private static Entry improved_yrden() {
        var id = Identifier.of(MOD_ID, "improved_yrden");
        var title = "Improved Whirl";
        var description = "Reduces cooldown of the Yrden Sign by {cooldown_duration_deduct} sec";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.YRDEN;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:yrden";
        modifier.cooldown_duration_deduct = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    public static Entry improved_aard = add(improved_aard());
    private static Entry improved_aard() {
        var id = Identifier.of(MOD_ID, "improved_aard");
        var title = "Improved Aard";
        var description = "Reduces cooldown of the Aard Sign by {cooldown_duration_deduct} sec";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.AARD;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:aard";
        modifier.cooldown_duration_deduct = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    public static Entry improved_rend = add(improved_rend());
    private static Entry improved_rend() {
        var id = Identifier.of(MOD_ID, "improved_rend");
        var title = "Improved Rend";
        var description = "Increases critical chance of Rend by {critical_chance_bonus}";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:rend";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.critical_chance_bonus = 0.05F;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    //// SKILL TREE MODIFERS
    /// AARD
    public static final Entry aard_far_reach = add(aard_far_reach());
    private static Entry aard_far_reach() {
        var id = Identifier.of(MOD_ID, "aard_far_reach");
        var title = "Far-Reaching Aard";
        var description = "Increases the range of Aard Signs by {range_add_1}.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.AARD;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:aard";
        modifier.range_add = 2;
        var modifier2 = new Spell.Modifier();
        modifier2.spell_pattern = "witcher_rpg:aard_sweep";
        modifier2.range_add = 2;
        spell.modifiers = List.of(modifier,modifier2);

        return new Entry(id, spell, title, description, null);
    }
    public static Entry aard_shockwave = add(aard_shockwave());
    private static Entry aard_shockwave() {
        var id = Identifier.of(MOD_ID, "aard_shockwave");
        var title = "Shockwave";
        var description = "Aard impacts now have a {trigger_chance} chance to stun the target.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.AARD;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.15F,WitcherSpellSchools.AARD.id.toString());
        spell.passive.triggers = List.of(trigger);

        var stun = SpellBuilder.Impacts.effectSet(SpellEngineEffects.STUN.id.toString(), 3, 0);
        spell.impacts = List.of(stun);

        SpellBuilder.Cost.cooldown(spell, 0.5F);
        return new Entry(id, spell, title, description, null);
    }
    public static Entry aard_frostbite = add(aard_frostbite());
    private static Entry aard_frostbite() {
        var id = Identifier.of(MOD_ID, "aard_frostbite");
        var title = "Frostbite";
        var description = "Aard impacts now deals additional frost {damage} damage and has {impact_chance} chance to freeze the target.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = SpellSchools.FROST;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(1.0F,WitcherSpellSchools.AARD.id.toString());
        spell.passive.triggers = List.of(trigger);

        var damage = SpellBuilder.Impacts.damage(0.5F,0);
        damage.attribute = WitcherAttributes.AARD_INTENSITY.getIdAsString();
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.snowflake.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        30, 0.4F, 0.4F),
                new ParticleBatch(
                        SpellEngineParticles.frost_shard.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.4F, 0.6F)
        };
        var freeze = SpellBuilder.Impacts.effectSet(MRPGCEffects.FROSTED.id.toString(), 3, 0);
        freeze.chance = 0.2F;
        spell.impacts = List.of(freeze);

        SpellBuilder.Cost.cooldown(spell, 0.5F);
        return new Entry(id, spell, title, description, null);
    }
    /// AXII
    public static final Entry axii_lethargy = add(axii_lethargy());
    private static Entry axii_lethargy() {
        var id = Identifier.of(MOD_ID, "axii_lethargy");
        var title = "Lethargy";
        var description = "Axii signs inflict Lethargy slowing the target by {bonus}.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.AXII;

        Spell.Trigger trigger = new Spell.Trigger();
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.STATUS_EFFECT.toString();
        trigger.type = net.spell_engine.api.spell.Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.school = WitcherSpellSchools.AXII.id.toString();
        spell.passive.triggers = List.of(trigger);

        var debuff = SpellBuilder.Impacts.effectSet(WitcherStatusEffects.AXII_LETHARGY.toString(), 4, 0);
        debuff.action.status_effect.amplifier_cap = 5;
        debuff.action.status_effect.amplifier_power_multiplier = 0.15F;
        spell.impacts = List.of(debuff);

        SpellBuilder.Cost.cooldown(spell, 0.5F);

        return new Entry(id, spell, title, description, null);
    }
    public static Entry axii_link = add(axii_link());
    private static Entry axii_link() {
        var id = Identifier.of(MOD_ID, "axii_link");
        var title = "Link";
        var description = "{trigger_chance} chance that the Axii sign spreads around the target.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.AXII;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        Spell.Trigger trigger = new Spell.Trigger();
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.STATUS_EFFECT.toString();
        trigger.type = net.spell_engine.api.spell.Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.chance = 0.25F;
        trigger.spell.id = "witcher_rpg:axii";
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(WitcherStatusEffects.AXII.id.toString(),3,0);
        impact.action.status_effect.amplifier_power_multiplier = 0.15F;
        impact.action.allow_on_center_target = false;
        spell.impacts = List.of(impact);

        var area_impact = new Spell.AreaImpact();
        area_impact.radius = 2.0F;
        area_impact.extra_radius = new Spell.AreaImpact.ExtraRadius();
        area_impact.extra_radius.power_coefficient = 0.1F;
        area_impact.area = new Spell.Target.Area();
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        spell.area_impact = area_impact;

        SpellBuilder.Cost.cooldown(spell, 5.0F);
        return new Entry(id, spell, title, description, null);
    }
    public static Entry axii_domination = add(axii_domination());
    private static Entry axii_domination() {
        var id = Identifier.of(MOD_ID, "axii_domination");
        var title = "Domination";
        var description = "";
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.AXII;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        Spell.Trigger trigger = new Spell.Trigger();
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.STATUS_EFFECT.toString();
        trigger.type = net.spell_engine.api.spell.Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.id = "witcher_rpg:axii";
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.effectSet(WitcherStatusEffects.AXII.id.toString(),3,0);
        impact.action.status_effect.amplifier_power_multiplier = 0.15F;
        impact.action.allow_on_center_target = false;
        spell.impacts = List.of(impact);

        SpellBuilder.Cost.cooldown(spell, 5.0F);
        return new Entry(id, spell, title, description, null);
    }
    /// IGNI
    public static Entry igni_melt_armor = add(igni_melt_armor());
    private static Entry igni_melt_armor() {
        var id = Identifier.of(MOD_ID, "igni_melt_armor");
        var title = "Molten Armor";
        var description = "Igni impacts now have a {trigger_chance} chance to stun the target.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.IGNI;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(1.0F,WitcherSpellSchools.IGNI.id.toString());
        spell.passive.triggers = List.of(trigger);

        var debuff = SpellBuilder.Impacts.effectAdd(MRPGCEffects.MOLTEN_ARMOR.id.toString(), 4, 1,5);
        debuff.action.status_effect.amplifier_cap_power_multiplier = 0.15F;
        spell.impacts = List.of(debuff);

        SpellBuilder.Cost.cooldown(spell, 0.5F);
        return new Entry(id, spell, title, description, null);
    }
    public static Entry igni_combustion = add(igni_combustion());
    private static Entry igni_combustion() {
        var id = Identifier.of(MOD_ID, "igni_combustion");
        var title = "Combustion";
        var description = "Igni impacts on burning targets spread fire around them.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.IGNI;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(1.0F,WitcherSpellSchools.IGNI.id.toString());
        trigger.target_conditions = List.of(SpellBuilder.TargetConditions.ofPredicate(SpellEntityPredicates.IS_ON_FIRE));
        spell.passive.triggers = List.of(trigger);

        var impact = SpellBuilder.Impacts.fire(3);
        impact.action.allow_on_center_target = false;
        spell.impacts = List.of(impact);
        var area_impact = new Spell.AreaImpact();
        area_impact.radius = 3.0F;
        area_impact.area = new Spell.Target.Area();
        area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        area_impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.flame.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.5F, 0.5F),
        };
        area_impact.sound = new Sound(SpellEngineSounds.GENERIC_FIRE_IGNITE.id().toString());
        spell.area_impact = area_impact;


        SpellBuilder.Cost.cooldown(spell, 5.0F);
        return new Entry(id, spell, title, description, null);
    }
    public static final Entry igni_pyromaniac = add(igni_pyromaniac());
    private static Entry igni_pyromaniac() {
        var id = Identifier.of(MOD_ID, "igni_pyromaniac");
        var title = "Pyromaniac";
        var description = "Increases the critical damage of Igni Signs by {critical_damage_bonus_1}.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.IGNI;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:igni";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.critical_damage_bonus = 0.2F;

        var modifier2 = new Spell.Modifier();
        modifier2.spell_pattern = "witcher_rpg:igni_firestream";
        modifier2.power_modifier = new Spell.Impact.Modifier();
        modifier2.power_modifier.critical_damage_bonus = 0.2F;

        spell.modifiers = List.of(modifier,modifier2);

        return new Entry(id, spell, title, description, null);
    }
    /// QUEN
    public static final Entry quen_exploding_shield = add(quen_exploding_shield());
    private static Entry quen_exploding_shield() {
        var id = Identifier.of(MOD_ID, "quen_exploding_shield");
        var effect = WitcherStatusEffects.QUEN_EXPLOSIVE;
        var title = effect.title;
        var description = "Quen shields now knock back attackers.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = WitcherSpellSchools.QUEN;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellCast(WitcherSpellSchools.QUEN);
        spell.passive.triggers = List.of(trigger);

        var stashTrigger = SpellBuilder.Triggers.damageTaken();
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 20, stashTrigger);
        spell.deliver.stash_effect.consume = 0;

        var damage = SpellBuilder.Impacts.damage(0F, 1.5F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.electric_arc_A.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.15F, 0.2F)
        };
        damage.sound = new Sound("");
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 1);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry quen_warding_shield = add(quen_warding_shield());
    private static Entry quen_warding_shield() {
        var id = Identifier.of(MOD_ID, "quen_warding_shield");
        var title = "Warding Shield";
        var description = "The Quen Signs stays up longer for {effect_duration_add} sec.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.QUEN;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:quen";
        modifier.effect_duration_add = 4.0F;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry quen_discharge = add(quen_discharge());
    private static Entry quen_discharge() {
        var id = Identifier.of(MOD_ID, "quen_discharge");
        var effect = WitcherStatusEffects.QUEN_DISCHARGE;
        var title = effect.title;
        var description = "Quen shields now reflect {damage} to the attacker.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.QUEN;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellCast(WitcherSpellSchools.QUEN);
        spell.passive.triggers = List.of(trigger);

        var stashTrigger = SpellBuilder.Triggers.damageTaken();
        SpellBuilder.Deliver.stash(spell, effect.id.toString(), 20, stashTrigger);
        spell.deliver.stash_effect.consume = 0;

        var damage = SpellBuilder.Impacts.damage(0.25F, 0.1F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.electric_arc_A.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.15F, 0.2F)
        };
        damage.sound = new Sound("");
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 1);

        return new Entry(id, spell, title, description, null);
    }
    /// YRDEN
    /// TO DO - INCREASE RANGE OF THE CIRCLE INSTEAD OF DURATION
    public static final Entry yrden_sustained_glyphs = add(yrden_sustained_glyphs());
    private static Entry yrden_sustained_glyphs() {
        var id = Identifier.of(MOD_ID, "yrden_sustained_glyphs");
        var title = "Sustained Glyphs";
        var description = "Increases Yrden Signs duration by {spawn_duration_add} sec.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.YRDEN;

        var modifier = new Spell.Modifier();
        modifier.spawn_duration_add = 2;
        var impactfilter_spawn = new Spell.Modifier.ImpactFilter();
        impactfilter_spawn.school = WitcherSpellSchools.YRDEN;
        modifier.impact_filters = List.of(impactfilter_spawn);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry yrden_binding_glyphs = add(yrden_binding_glyphs());
    private static Entry yrden_binding_glyphs() {
        var id = Identifier.of(MOD_ID, "yrden_binding_glyphs");
        var title = "Binding Glyphs";
        var description = "The power of yrden signs is increased by {power_multiplier}.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.YRDEN;

        var modifier = new Spell.Modifier();
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.15F;
        var impactfilter_spawn = new Spell.Modifier.ImpactFilter();
        impactfilter_spawn.school = WitcherSpellSchools.YRDEN;
        modifier.impact_filters = List.of(impactfilter_spawn);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry yrden_supercharged_glyphs = add(yrden_supercharged_glyphs());
    private static Entry yrden_supercharged_glyphs() {
        var id = Identifier.of(MOD_ID, "yrden_supercharged_glyphs");
        var title = "Super Charged Glyphs";
        var description = "The Yrden circle now deals {damage} damage to all entities.";
        var spell = SpellBuilder.createSpellModifier();
        spell.school = WitcherSpellSchools.YRDEN;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:yrden";

        var impact = SpellBuilder.Impacts.damage(0.2F, 0F);
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.PREPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, null);
    }
    /// PASSIVE SPELLS
    public static final Entry ROSE_OF_REMEMBRANCE = add(rose_of_remembrance());
    private static Entry rose_of_remembrance() {
        var id = Identifier.of(MOD_ID, "rose_of_remembrance");
        var effect = WitcherStatusEffects.ROSE_OF_REMEMBRANCE;
        var title = "Rose of Remembrance";
        var description = "On taking damage: {trigger_chance} chance, to heal yourself for 5%% of your max health for {effect_duration} seconds..";
        var spell = passiveSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var trigger_damage_taken = new Spell.Trigger();
        trigger_damage_taken.chance = 0.15F;
        trigger_damage_taken.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger_damage_taken.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger_damage_taken);

        var debuff = createEffectImpact(Identifier.of(effect.id.toString()),10);
        debuff.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.SET;
        debuff.action.status_effect.amplifier = 1;
        debuff.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_3.id());
        debuff.action.status_effect.refresh_duration = false;
        spell.impacts = List.of(debuff);

        configureCooldown(spell, 45);

        return new Entry(id, spell, title, description, null);
    }
    public static Entry SUNSTONE = add(sunstone());
    private static Entry sunstone() {
        var id = Identifier.of(MOD_ID, "sunstone");
        var description = "Use: Increases sign intensity by {bonus} for {effect_duration} seconds.";
        var effect = WitcherStatusEffects.SUNSTONE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var spell = new Spell();
        spell.type = Spell.Type.ACTIVE;
        spell.active = new Spell.Active();
        spell.active.cast = new Spell.Active.Cast();
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        spell.school = WitcherSpellSchools.SIGN;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPELL,
                                SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.6F)
                        .color(Color.WHITE.toRGBA()),
        };
        spell.impacts = List.of(createEffectImpact(Identifier.of(effect.id.toString()), 15));
        configureCooldown(spell, 90);

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry crystal_skull = add(crystal_skull());
    private static Entry crystal_skull() {
        var id = Identifier.of(MOD_ID, "crystal_skull");
        var description = "On melee hit: {trigger_chance} to shoot crystal shards, dealing {damage} to enemies.";
        var title = "Crystal Skull";

        var spell = passiveSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.range = 16;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.melee = new Spell.Trigger.MeleeCondition();
        trigger.chance = 0.2F;

        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();

        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        spell.deliver.projectile.inherit_shooter_pitch = false;
        spell.deliver.projectile.launch_properties.velocity = 1.25F;
        var projectile = new Spell.ProjectileData();
        projectile.homing_angle = 0F;
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.model = new Spell.ProjectileModel();
        projectile.client_data.model.model_id = "witcher_rpg:projectile/crystal_skull";
        projectile.perks.pierce = 999;
        spell.deliver.projectile.projectile = projectile;

        var damage = new Spell.Impact();
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.2F;
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 20, 0.1F, 0.3F, 0.0F, 0F)
                        .color(Color.GREEN.toRGBA())
        };
        spell.impacts = List.of(damage);

        configureCooldown(spell, 5);
        return new Entry(id, spell, title, description, null);
    }
    public static Entry pure_silver = add(pure_silver());
    private static Entry pure_silver() {
        var id = Identifier.of(MOD_ID, "pure_silver");
        var description = "On melee hit: Sets silver vulnerable targets on fire.";
        var title = "Pure Silver";

        var spell = passiveSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.melee = new Spell.Trigger.MeleeCondition();
        trigger.chance = 1.0F;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var fire = SpellBuilder.Impacts.fire(3);
        silverVulnerabilityAllow(fire);
        fire.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.ARCANE,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 20, 0.1F, 0.3F, 0.0F, 0F)
                        .color(Color.WHITE.toRGBA())
        };
        spell.impacts = List.of(fire);

        configureCooldown(spell, 1);
        return new Entry(id, spell, title, description, null);
    }
    public static final Entry grandmaster_feline = add(grandmaster_feline());
    private static Entry grandmaster_feline() {
        var id = Identifier.of(MOD_ID, "grandmaster_feline");
        var effect = WitcherStatusEffects.FELINE_INJURY_MASTER;
        var title = "Grandmaster Feline Technique";
        var description = "Fencing Spells and melee hits inflict injuries if the target has a bad effect, " +
                "increasing incoming damage and reducing movement speed by {bonus} {effect_duration} sec.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().attributes().get(0);
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };
        var spell = SpellBuilder.createSpellPassive();
        spell.school = WitcherSpellSchools.AARD;
        spell.range = 0;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        Spell.Trigger trigger = new Spell.Trigger();
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.id = "#witcher_rpg:fencing";
        var condition = new Spell.TargetCondition();
        condition.entity_predicate_id = SpellEntityPredicates.HAS_BAD_EFFECT.id().toString();
        trigger.target_conditions = List.of(condition);

        var trigger2 = SpellBuilder.Triggers.meleeAttack(false);
        var condition2 = new Spell.TargetCondition();
        condition2.entity_predicate_id = SpellEntityPredicates.HAS_BAD_EFFECT.id().toString();
        trigger2.target_conditions = List.of(condition2);
        spell.passive.triggers = List.of(trigger,trigger2);

        var debuff = SpellBuilder.Impacts.effectSet(effect.id.toString(),5,0);
        spell.impacts = List.of(debuff);

        SpellBuilder.Cost.cooldown(spell, 20F);

        return new Entry(id, spell, title, description, mutator);
    }
    public static final Entry grandmaster_griffin = add(grandmaster_griffin());
    private static Entry grandmaster_griffin() {
        var id = Identifier.of(MOD_ID, "grandmaster_griffin");
        var title = "Grandmaster Griffin Technique";
        var effect = WitcherStatusEffects.YRDEN_GRIFFIN_MASTER;
        var description = "The Yrden circle increases the sign intensity of the caster by {bonus2} & reduces incoming damage by {bonus}.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().attributes().get(1);
            var modifier2 = effect.config().attributes().get(0);
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            var bonus2 = SpellTooltip.bonus(modifier2.value, modifier2.operation);
            return args.description()
                    .replace("{bonus}", bonus)
                    .replace("{bonus2}", bonus2);
        };
        var spell = SpellBuilder.createSpellModifier();
        spell.school = WitcherSpellSchools.YRDEN;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:yrden";
        var impact = SpellBuilder.Impacts.effectSet(effect.id.toString(),2,0);
        impact.action.apply_to_caster = true;
        impact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.STRIPE,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.GROUND,
                        20, 0.2F, 0.2F).extent(1.0F)
                        .color(Color.ARCANE.toRGBA())
        };
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, mutator);
    }
    public static final Entry grandmaster_wolven = add(grandmaster_wolven());
    private static Entry grandmaster_wolven() {
        var id = Identifier.of(MOD_ID, "grandmaster_wolven");
        var title = "Grandmaster Wolven Technique";
        var description = "Targets in the yrden circle, receive extra {damage} damage with aard signs.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = WitcherSpellSchools.AARD;
        spell.range = 0;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(1.0F, "aard");
        var condition = new Spell.TargetCondition();
        condition.entity_predicate_id = HAS_YRDEN.id().toString();
        trigger.target_conditions = List.of(condition);
        spell.passive.triggers = List.of(trigger);

        var damage = SpellBuilder.Impacts.damage(1.0F, 0F);
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry grandmaster_ursine = add(grandmaster_ursine());
    private static Entry grandmaster_ursine() {
        var id = Identifier.of(MOD_ID, "grandmaster_ursine");
        var title = "Grandmaster Ursine Technique";
        var description = "Taking Damage has {trigger_chance} chance to apply a quen shield.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = WitcherSpellSchools.QUEN;
        spell.range = 0;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger.chance = 0.2F;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var effect = SpellBuilder.Impacts.effectSet(WitcherStatusEffects.QUEN_SHIELD.id.toString(),10,0);
        effect.action.status_effect.amplifier_power_multiplier = 0.3F;
        spell.impacts = List.of(effect);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry griffin_school_medallion = add(griffin_school_medallion());
    private static Entry griffin_school_medallion() {
        var id = Identifier.of(MOD_ID, "griffin_school_medallion");
        var title = "Witcher Griffin Medallion";
        var description = "Casting Signs halves the active cooldowns of all signs.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = WitcherSpellSchools.SIGN;
        spell.range = 0;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellCast();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        Spell.Impact aard = new Spell.Impact();
        aard.action = new Spell.Impact.Action();
        aard.action.type = net.spell_engine.api.spell.Spell.Impact.Action.Type.COOLDOWN;
        aard.action.cooldown = new Spell.Impact.Action.Cooldown();
        aard.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        aard.action.cooldown.actives.school = WitcherSpellSchools.AARD.id.toString();
        aard.action.cooldown.actives.duration_multiplier = 0.5F;
        Spell.Impact axii = new Spell.Impact();
        axii.action = new Spell.Impact.Action();
        axii.action.type = net.spell_engine.api.spell.Spell.Impact.Action.Type.COOLDOWN;
        axii.action.cooldown = new Spell.Impact.Action.Cooldown();
        axii.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        axii.action.cooldown.actives.school = WitcherSpellSchools.AXII.id.toString();
        axii.action.cooldown.actives.duration_multiplier = 0.5F;
        Spell.Impact igni = new Spell.Impact();
        igni.action = new Spell.Impact.Action();
        igni.action.type = net.spell_engine.api.spell.Spell.Impact.Action.Type.COOLDOWN;
        igni.action.cooldown = new Spell.Impact.Action.Cooldown();
        igni.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        igni.action.cooldown.actives.school = WitcherSpellSchools.IGNI.id.toString();
        igni.action.cooldown.actives.duration_multiplier = 0.5F;
        Spell.Impact quen = new Spell.Impact();
        quen.action = new Spell.Impact.Action();
        quen.action.type = net.spell_engine.api.spell.Spell.Impact.Action.Type.COOLDOWN;
        quen.action.cooldown = new Spell.Impact.Action.Cooldown();
        quen.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        quen.action.cooldown.actives.school = WitcherSpellSchools.QUEN.id.toString();
        quen.action.cooldown.actives.duration_multiplier = 0.5F;
        Spell.Impact yrden = new Spell.Impact();
        yrden.action = new Spell.Impact.Action();
        yrden.action.type = net.spell_engine.api.spell.Spell.Impact.Action.Type.COOLDOWN;
        yrden.action.cooldown = new Spell.Impact.Action.Cooldown();
        yrden.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        yrden.action.cooldown.actives.school = WitcherSpellSchools.YRDEN.id.toString();
        yrden.action.cooldown.actives.duration_multiplier = 0.5F;
        Spell.Impact sign = new Spell.Impact();
        sign.action = new Spell.Impact.Action();
        sign.action.type = net.spell_engine.api.spell.Spell.Impact.Action.Type.COOLDOWN;
        sign.action.cooldown = new Spell.Impact.Action.Cooldown();
        sign.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        sign.action.cooldown.actives.school = WitcherSpellSchools.SIGN.id.toString();
        sign.action.cooldown.actives.duration_multiplier = 0.5F;
        spell.impacts = List.of(aard,axii,igni);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry cat_school_medallion = add(cat_school_medallion());
    private static Entry cat_school_medallion() {
        var id = Identifier.of(MOD_ID, "cat_school_medallion");
        var title = "Witcher Cat Medallion";
        var description = "Hitting targets with bad effects inflicts extra damage, dealing more damage the less health the target has.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.range = 0;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        var trigger = SpellBuilder.Triggers.meleeAttack(false);
        var condition = new Spell.TargetCondition();
        condition.entity_predicate_id = SpellEntityPredicates.HAS_BAD_EFFECT.id().toString();
        trigger.target_conditions = List.of(condition);
        spell.passive.triggers = List.of(trigger);

        var custom = new Spell.Impact();
        custom.action = new Spell.Impact.Action();
        custom.action.custom = new Spell.Impact.Action.Custom();
        custom.action.type = Spell.Impact.Action.Type.CUSTOM;
        custom.action.custom.intent = SpellTarget.Intent.HARMFUL;
        custom.action.custom.handler = "more_rpg_classes:damage_according_to_missing_health";
        custom.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.dripping_blood.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        35, 0.4F, 1.0F),
                new ParticleBatch(
                        SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.2F, 0.5F).color(Color.RED.toRGBA())
        };

        spell.impacts = List.of(custom);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry bear_school_medallion = add(bear_school_medallion());
    private static Entry bear_school_medallion() {
        var id = Identifier.of(MOD_ID, "bear_school_medallion");
        var title = "Witcher Bear Medallion";
        var effect = WitcherStatusEffects.BEAR_SCHOOL_MEDALLION;
        var description = "Taking Damage reduces incoming damage by {bonus} for {effect_duration} secs.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().attributes().get(0);
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description()
                    .replace("{bonus}", bonus);
        };
        var spell = SpellBuilder.createSpellPassive();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.range = 0;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(),5,0);
        spell.impacts = List.of(buff);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Entry(id, spell, title, description, mutator);
    }
    public static final Entry wolf_school_medallion = add(wolf_school_medallion());
    private static Entry wolf_school_medallion() {
        var id = Identifier.of(MOD_ID, "wolf_school_medallion");
        var title = "Witcher Wolf Medallion";
        var effect = WitcherStatusEffects.WOLF_SCHOOL_MEDALLION;
        var description = "After casting a sign, you deal {damage} magical damage per melee attack for {stash_duration} sec.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = WitcherSpellSchools.SIGN;
        spell.range = 0;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_header = false;
        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellCast();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        var trigger_stash_damage_taken = new Spell.Trigger();
        trigger_stash_damage_taken.type = Spell.Trigger.Type.MELEE_IMPACT;
        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.duration = 8;
        spell.deliver.stash_effect.amplifier = 0;
        spell.deliver.stash_effect.id = effect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        spell.deliver.stash_effect.triggers = List.of(trigger_stash_damage_taken);

        var damage = SpellBuilder.Impacts.damage(0.15F,0.0F);
        damage.attribute = "minecraft:generic.attack_damage";
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPARK,
                                SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.1F, 0.2F).color(Color.ELECTRIC.toRGBA())
        };
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Entry(id, spell, title, description, null);
    }

}
