package net.witcher_rpg.spell;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.entity.SpellEntityPredicates;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ModelEffect;
import net.spell_engine.api.spell.fx.ModelEffectBuilder;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
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
import java.util.Iterator;
import java.util.List;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;
import static net.witcher_rpg.WitcherClassMod.tweaksConfig;

public class WitcherSpells {
    public enum Book { FENCING, SIGNS}
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator,
                        @Nullable Book book) {
        public Entry(Identifier id, Spell spell, String title, String description) {
            this(id, spell, title, description, null, null);
        }
        public Entry mutator(SpellTooltip.DescriptionMutator mutator) {
            return new Entry(id, spell, title, description, mutator, book);
        }
        public Entry book(Book book) {
            return new Entry(id, spell, title, description, mutator, book);
        }
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
            SpellEntityPredicates.hasEffectOptimized(Identifier.of("witcher_rpg", "yrden_circle"));
    public static final SpellEntityPredicates.Entry HAS_WITCHER_SENSES_EXPOSED =
            SpellEntityPredicates.hasEffectOptimized(Identifier.of(MOD_ID, "witcher_senses_exposed"));
    private static final float BATTLE_TRANCE_DURATION_SECONDS = 5F;

    public static class TargetConditions {
        public TargetConditions() {
        }
        public static Spell.TargetCondition hasBadEffect() {
            Spell.TargetCondition badEffectCondition = new Spell.TargetCondition();
            badEffectCondition.entity_predicate_id = SpellEntityPredicates.HAS_BAD_EFFECT.id().toString();
            return badEffectCondition;
        }
    }
    public static Spell.Trigger witcherMeleeSkillImpact() {
        Spell.Trigger trigger = new Spell.Trigger();
        trigger.type = net.spell_engine.api.spell.Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.school = WitcherSpellSchools.WITCHER_MELEE.id.toString();
        trigger.spell.type = Spell.Type.ACTIVE;
        return trigger;
    }

    public static List<Spell.Trigger> witcherMeleeImpacts() {
        return List.of(SpellBuilder.Triggers.meleeAttackImpact(), SpellBuilder.Triggers.meleeSkillImpact(), witcherMeleeSkillImpact());
    }

    public static List<Spell.Trigger> badEffectMeleeHit() {
        List<Spell.Trigger> triggers = witcherMeleeImpacts();

        Spell.Trigger trigger;
        Spell.TargetCondition has_bad_effect;
        for(Iterator var1 = triggers.iterator(); var1.hasNext(); trigger.target_conditions = List.of(has_bad_effect)) {
            trigger = (Spell.Trigger)var1.next();
            has_bad_effect = TargetConditions.hasBadEffect();
        }

        return triggers;
    }

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

        spell.release.animation = PlayerAnimation.of("witcher_rpg:sign_cast_short");
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
        configureCooldown(spell, 8);
        spell.cost.exhaust = 0.4F;
        spell.cost.cooldown.group = "aard";

        return new Entry(id, spell, title, description).book(Book.SIGNS);
    }
    public static final Entry IGNI = add(igni());
    private static Entry igni() {
        var id = Identifier.of(MOD_ID, "igni");
        var title = "Igni";
        var description = "Unleashes a stream of fire dealing {damage} damage and setting enemies ablaze.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.IGNI;
        spell.range = 5.0F;
        spell.tier = 1;

        spell.active.cast.animation = PlayerAnimation.of("witcher_rpg:sign_cast_long");
        spell.active.cast.movement_speed = 0.75F;
        spell.active.cast.duration = 2.0F;
        spell.active.cast.sound = Sound.withRandomness(Identifier.of("witcher_rpg:igni_sign"), 0.2F);
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.flame_spark.id().toString(),
                        ParticleBatch.Shape.CONE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK, 40.0F, 1.2F, 2.5F, 90.0F, 0F),
                new ParticleBatch("smoke",
                        ParticleBatch.Shape.CONE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK, 0.1F, 0.01F, 0.4F, 90.0F, 0F),
                new ParticleBatch("witcher_rpg:igni_sign_cast",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT,
                        1.0F, 0.01F, 0.02F)
        };
        spell.active.cast.type = Spell.Active.Cast.Type.CHANNEL;
        spell.active.cast.channel = new Spell.Active.Cast.Channel();
        spell.active.cast.channel.ticks = 6;

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.angle_degrees = 90;
        spell.target.area.horizontal_range_multiplier = 2.0F;
        spell.target.area.vertical_range_multiplier = 1.5F;

        var damage = new Spell.Impact();
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.8F;
        damage.action.damage.knockback = 0.2F;
        damage.sound = new Sound("block.blastfurnace.fire_crackle");
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.flame_medium_a.id().toString(),
                        ParticleBatch.Shape.CONE, ParticleBatch.Origin.LAUNCH_POINT,
                        1.0F, 0.7F, 1.5F),
                new ParticleBatch("large_smoke",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        0.2F, 0.1F, 0.3F),
                new ParticleBatch(SpellEngineParticles.flame_spark.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        4.0F, 0.02F, 0.1F)
        };

        var fire = new Spell.Impact();
        fire.action = new Spell.Impact.Action();
        fire.action.type = Spell.Impact.Action.Type.FIRE;
        fire.action.fire = new Spell.Impact.Action.Fire();
        fire.action.fire.duration = 2;
        fire.sound = new Sound("block.blastfurnace.fire_crackle");

        spell.impacts = List.of(damage, fire);

        configureCooldown(spell, 8);
        spell.cost.exhaust = 0.4F;
        spell.cost.cooldown.group = "igni";

        return new Entry(id, spell, title, description).book(Book.SIGNS);
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

        spell.release.animation = PlayerAnimation.of("witcher_rpg:sign_cast_ground");
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
        cloud.spawn_ticks = 10;
        cloud.despawn_ticks = 10;
        var yrdenCircleTotalTicks = cloud.spawn_ticks + Math.round(cloud.time_to_live_seconds * 20F) + cloud.despawn_ticks;
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.light_level = 14;
        cloud.client_data.model_fx = List.of(
                ModelEffectBuilder.create("witcher_rpg:spell_effect/yrden_circle")
                        .scale(3.0F)
                        .light(LightEmission.RADIATE)
                        .duration(yrdenCircleTotalTicks)
                        .scaleIn(0, cloud.spawn_ticks, ModelEffect.Easing.EASE_OUT_CUBIC)
                        .scaleOut(yrdenCircleTotalTicks - cloud.despawn_ticks, yrdenCircleTotalTicks, ModelEffect.Easing.EASE_IN_CUBIC)
                        .build()
        );
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

        configureCooldown(spell, 15);
        spell.cost.exhaust = 0.4F;
        spell.cost.cooldown.group = "yrden";

        return new Entry(id, spell, title, description).book(Book.SIGNS);
    }
    public static final Entry AXII = add(axii());
    private static Entry axii() {
        var id = Identifier.of(MOD_ID, "axii");
        var title = "Axii";
        var description = "Charms the target for {effect_duration} seconds, causing them to stop attacking.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.AXII;
        spell.range = 10;
        spell.tier = 2;

        spell.active.cast.movement_speed = 0.75F;
        spell.active.cast.duration = 0.5F;
        spell.active.cast.animation = PlayerAnimation.of("witcher_rpg:sign_cast_long");
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:axii_sign_cast",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT,
                        0.2F, 0.01F, 0.1F)
        };

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.sticky = false;
        spell.target.aim.required = true;

        spell.release.animation = PlayerAnimation.of("witcher_rpg:sign_cast_short");
        spell.release.sound = new Sound("witcher_rpg:axii_sign");
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:axii_sign_cast",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT,
                        1.0F, 0.01F, 0.1F)
        };

        var effect = new Spell.Impact();
        effect.action = new Spell.Impact.Action();
        effect.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        effect.action.status_effect = new Spell.Impact.Action.StatusEffect();
        effect.action.status_effect.effect_id = WitcherStatusEffects.AXII.id.toString();
        effect.action.status_effect.duration = 5;
        effect.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.SET;
        effect.action.status_effect.amplifier = 0;
        effect.action.status_effect.amplifier_power_multiplier = 0.25F;
        effect.action.status_effect.show_particles = false;
        var modifier = createImpactModifier("#witcher_rpg:axii_effect_immune");
        modifier.execute = TriState.DENY;
        effect.target_modifiers = List.of(modifier);
        spell.impacts = List.of(effect);

        configureCooldown(spell, 20);
        spell.cost.cooldown.haste_affected = false;
        spell.cost.exhaust = 0.4F;
        spell.cost.cooldown.group = "axii";

        return new Entry(id, spell, title, description).book(Book.SIGNS);
    }
    public static final Entry QUEN = add(quen());
    private static Entry quen() {
        var id = Identifier.of(MOD_ID, "quen");
        var title = "Quen";
        var description = "Creates a protective shield that absorbs damage for {effect_duration} seconds.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.QUEN;
        spell.range = 0;
        spell.tier = 2;

        spell.active.cast.movement_speed = 0.75F;
        spell.active.cast.duration = 0;

        spell.target.type = Spell.Target.Type.CASTER;

        spell.release.animation = PlayerAnimation.of("witcher_rpg:sign_cast_short");
        spell.release.sound = new Sound("witcher_rpg:quen_sign");
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.electric_arc_A.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.LAUNCH_POINT,
                        3.0F, 0.01F, 0.05F).extent(1),
                new ParticleBatch(SpellEngineParticles.electric_arc_B.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        5.0F, 0.01F, 0.05F).extent(1),
                new ParticleBatch("witcher_rpg:quen_sign_cast",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT,
                        3.0F, 0.01F, 0.1F),
                new ParticleBatch("witcher_rpg:quen_sign_cast",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.LAUNCH_POINT,
                        1.0F, 0.01F, 0.2F)
        };

        var effect = new Spell.Impact();
        effect.action = new Spell.Impact.Action();
        effect.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        effect.action.status_effect = new Spell.Impact.Action.StatusEffect();
        effect.action.status_effect.effect_id = WitcherStatusEffects.QUEN_SHIELD.id.toString();
        effect.action.status_effect.duration = 10;
        effect.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.SET;
        effect.action.status_effect.amplifier = 0;
        effect.action.status_effect.amplifier_power_multiplier = 0.25F;
        effect.action.status_effect.show_particles = false;
        spell.impacts = List.of(effect);

        configureCooldown(spell, 24);
        spell.cost.exhaust = 0.4F;
        spell.cost.cooldown.group = "quen";

        return new Entry(id, spell, title, description).book(Book.SIGNS);
    }
    public static final Entry IGNI_FIRESTREAM = add(igni_firestream());
    private static Entry igni_firestream() {
        var id = Identifier.of(MOD_ID, "igni_firestream");
        var title = "Igni Firestream";
        var description = "Unleashes a concentrated stream of fire dealing {damage} damage and setting enemies ablaze.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.IGNI;
        spell.range = 6.0F;
        spell.tier = 3;

        spell.active.cast.animation = PlayerAnimation.of("witcher_rpg:sign_cast_long");
        spell.active.cast.movement_speed = 0.5F;
        spell.active.cast.duration = 7.0F;
        spell.active.cast.sound = Sound.withRandomness(Identifier.of("witcher_rpg:igni_sign"), 0.4F);
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.flame_medium_a.id().toString(),
                        ParticleBatch.Shape.CONE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK, 5.0F, 0.8F, 6.0F, 20.0F, 0F),
                new ParticleBatch("witcher_rpg:igni_sign_cast",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT,
                        0.2F, 0.01F, 0.2F)
        };
        spell.active.cast.type = Spell.Active.Cast.Type.CHANNEL;
        spell.active.cast.channel = new Spell.Active.Cast.Channel();
        spell.active.cast.channel.ticks = 35;

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.angle_degrees = 20;

        spell.release.sound = new Sound("block.blastfurnace.fire_crackle");

        var damage = new Spell.Impact();
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.9F;
        damage.action.damage.knockback = 0.2F;
        damage.sound = new Sound("block.blastfurnace.fire_crackle");
        damage.particles = new ParticleBatch[]{
                new ParticleBatch("lava",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        1.0F, 0.5F, 3.0F),
                new ParticleBatch(SpellEngineParticles.flame_spark.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10.0F, 0.08F, 0.2F)
        };

        var fire = new Spell.Impact();
        fire.action = new Spell.Impact.Action();
        fire.action.type = Spell.Impact.Action.Type.FIRE;
        fire.action.fire = new Spell.Impact.Action.Fire();
        fire.action.fire.duration = 3;

        spell.impacts = List.of(damage, fire);

        configureCooldown(spell, 12);
        spell.cost.exhaust = 0.5F;
        spell.cost.cooldown.group = "igni";

        return new Entry(id, spell, title, description).book(Book.SIGNS);
    }
    public static final Entry AARD_SWEEP = add(aard_sweep());
    private static Entry aard_sweep() {
        var id = Identifier.of(MOD_ID, "aard_sweep");
        var title = "Aard Sweep";
        var description = "A 360 degree telekinetic blast dealing {damage} damage and knocking back all nearby enemies.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.AARD;
        spell.range = 4.0F;
        spell.tier = 3;

        spell.active.cast.movement_speed = 0.1F;
        spell.active.cast.duration = 0;
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:aard_sign_cast",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT,
                        0.2F, 0.01F, 0.1F)
        };
        spell.active.cast.start_sound = new Sound(Sounds.AARD_SIGN_ID);

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.angle_degrees = 360;

        spell.release.animation = PlayerAnimation.of("witcher_rpg:sign_cast_ground");
        spell.release.sound = new Sound(Sounds.AARD_SIGN_ID);
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        40.0F, 0.2F, 0.3F).preSpawnTravel(6),
                new ParticleBatch(SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        40.0F, 0.2F, 0.3F).preSpawnTravel(3),
                new ParticleBatch(SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        40.0F, 0.2F, 0.3F).preSpawnTravel(1)
        };

        var custom = new Spell.Impact();
        custom.action = new Spell.Impact.Action();
        custom.action.type = Spell.Impact.Action.Type.CUSTOM;
        custom.action.custom = new Spell.Impact.Action.Custom();
        custom.action.custom.handler = "more_rpg_classes:stop_arrows";
        custom.action.custom.intent = SpellTarget.Intent.HARMFUL;

        var damage = new Spell.Impact();
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.6F;
        damage.action.damage.knockback = 4.0F;
        damage.particles = new ParticleBatch[]{
                new ParticleBatch("more_rpg_classes:wind_vacuum",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK, 1.0F, 0.1F, 1.0F, 0F, 0F),
                new ParticleBatch("gust",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        1.0F, 0.2F, 0.3F)
        };

        spell.impacts = List.of(custom, damage);

        configureCooldown(spell, 15);
        spell.cost.exhaust = 0.5F;
        spell.cost.cooldown.group = "aard";

        return new Entry(id, spell, title, description).book(Book.SIGNS);
    }

    public static final Entry YRDEN_MAGIC_TRAP = add(yrden_magic_trap());
    private static Entry yrden_magic_trap() {
        var id = Identifier.of(MOD_ID, "yrden_magic_trap");
        var title = "Yrden Magic Trap";
        var description = "Places a magical trap that damages and slows enemies that enter it.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.YRDEN;
        spell.range = 3;
        spell.tier = 4;

        spell.active.cast.animation = PlayerAnimation.of("witcher_rpg:sign_cast_long");
        spell.active.cast.duration = 0.5F;
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:yrden_sign_cast",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        1.0F, 0.001F, 0.006F),
                new ParticleBatch("witcher_rpg:yrden_sign_cast",
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.CENTER,
                        1.0F, 0.01F, 0.06F)
        };

        spell.release.animation = PlayerAnimation.of("witcher_rpg:sign_cast_ground");
        spell.release.sound = new Sound("witcher_rpg:yrden_sign");

        var spawn = new Spell.Impact();
        spawn.action = new Spell.Impact.Action();
        spawn.action.type = Spell.Impact.Action.Type.SPAWN;
        var spawnData = new Spell.Impact.Action.Spawn();
        spawnData.entity_type_id = "witcher_rpg:yrden_magical_trap";
        spawnData.time_to_live_seconds = 20;
        spawn.action.spawns = List.of(spawnData);
        spell.impacts = List.of(spawn);

        configureCooldown(spell, 35);
        spell.cost.exhaust = 0.4F;
        spell.cost.cooldown.group = "yrden";

        return new Entry(id, spell, title, description).book(Book.SIGNS);
    }
    public static final Entry AXII_PUPPET = add(axii_puppet());
    private static Entry axii_puppet() {
        var id = Identifier.of(MOD_ID, "axii_puppet");
        var title = "Axii Puppet";
        var description = "Dominates the target for {effect_duration} seconds, turning them into an ally.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.AXII;
        spell.range = 10;
        spell.tier = 4;

        spell.active.cast.animation = PlayerAnimation.of("witcher_rpg:sign_cast_long");
        spell.active.cast.movement_speed = 0.2F;
        spell.active.cast.duration = 2.0F;
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:axii_sign_cast",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT,
                        0.2F, 0.01F, 0.1F)
        };

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.sticky = false;
        spell.target.aim.required = true;

        spell.release.animation = PlayerAnimation.of("witcher_rpg:sign_cast_short");
        spell.release.sound = new Sound("witcher_rpg:axii_sign");
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:axii_sign_cast",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT,
                        1.0F, 0.01F, 0.1F)
        };

        var effect = new Spell.Impact();
        effect.action = new Spell.Impact.Action();
        effect.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        effect.action.status_effect = new Spell.Impact.Action.StatusEffect();
        effect.action.status_effect.effect_id = WitcherStatusEffects.AXII_PUPPET.id.toString();
        effect.action.status_effect.duration = 8;
        effect.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.SET;
        effect.action.status_effect.amplifier = 0;
        effect.action.status_effect.show_particles = false;
        var modifier = createImpactModifier("#witcher_rpg:axii_effect_immune");
        modifier.execute = TriState.DENY;
        effect.target_modifiers = List.of(modifier);
        spell.impacts = List.of(effect);

        configureCooldown(spell, 25);
        spell.cost.cooldown.haste_affected = false;
        spell.cost.exhaust = 0.4F;
        spell.cost.cooldown.group = "axii";

        return new Entry(id, spell, title, description).book(Book.SIGNS);
    }
    public static final Entry WITCHER_SENSES = add(witcher_senses());
    private static Entry witcher_senses() {
        var id = Identifier.of(MOD_ID, "witcher_senses");
        var title = "Witcher Senses";
        var description = "Reveals all enemies within range, making them Glow and exposing their weaknesses for {effect_duration} seconds. Critical hits against exposed enemies deal more damage and are more likely to land.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.AXII;
        spell.range = 20;
        spell.tier = 3;

        spell.active.cast.movement_speed = 0.5F;
        spell.active.cast.duration = 0.5F;
        spell.active.cast.animation = PlayerAnimation.of("witcher_rpg:sign_cast_ground");
        spell.active.cast.start_sound = new Sound(Sounds.AXII_SIGN_ID);

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.angle_degrees = 360;

        spell.release.animation = PlayerAnimation.of("witcher_rpg:sign_cast_ground");
        spell.release.sound = Sound.withVolume(Sounds.AXII_SIGN_ID, 0.6F);
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        40.0F, 0.2F, 0.3F).preSpawnTravel(6)
                        .color(Color.from(WitcherSpellSchools.AXII.color).toRGBA()),
                new ParticleBatch(SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        40.0F, 0.2F, 0.3F).preSpawnTravel(3)
                        .color(Color.from(WitcherSpellSchools.AXII.color).toRGBA())
        };

        var glow = createEffectImpact(Identifier.of("minecraft", "glowing"), 10);
        var expose = createEffectImpact(Identifier.of(WitcherStatusEffects.WITCHER_SENSES_EXPOSED.id.toString()), 10);
        expose.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:axii_sign_cast",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        3F, 0.1F, 0.3F).color(Color.from(WitcherSpellSchools.AXII.color).toRGBA())
        };
        spell.impacts = List.of(glow, expose);

        configureCooldown(spell, 25);
        spell.cost.exhaust = 0.6F;

        return new Entry(id, spell, title, description).book(Book.SIGNS);
    }
    public static final Entry QUEN_ACTIVE_SHIELD = add(quen_active_shield());
    private static Entry quen_active_shield() {
        var id = Identifier.of(MOD_ID, "quen_active_shield");
        var title = "Quen Active Shield";
        var description = "Creates an active protective shield that absorbs damage and heals the caster.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.QUEN;
        spell.range = 0;
        spell.tier = 4;

        spell.active.cast.animation = PlayerAnimation.of("witcher_rpg:sign_cast_long");
        spell.active.cast.movement_speed = 0.1F;
        spell.active.cast.duration = 5.0F;
        spell.active.cast.sound = Sound.withRandomness(Identifier.of("witcher_rpg:quen_sign"), 0.4F);
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:quen_sign_cast",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.LAUNCH_POINT,
                        0.3F, 0.01F, 0.5F)
        };
        spell.active.cast.type = Spell.Active.Cast.Type.CHANNEL;
        spell.active.cast.channel = new Spell.Active.Cast.Channel();
        spell.active.cast.channel.ticks = 25;

        spell.target.type = Spell.Target.Type.CASTER;

        var custom = new Spell.Impact();
        custom.action = new Spell.Impact.Action();
        custom.action.type = Spell.Impact.Action.Type.CUSTOM;
        custom.action.custom = new Spell.Impact.Action.Custom();
        custom.action.custom.handler = "witcher_rpg:quen_active";
        spell.impacts = List.of(custom);

        configureCooldown(spell, 36);
        spell.cost.cooldown.proportional = false;
        spell.cost.cooldown.haste_affected = false;
        spell.cost.exhaust = 0.4F;
        spell.cost.cooldown.group = "quen";

        return new Entry(id, spell, title, description).book(Book.SIGNS);
    }

    /// FENCING ACTIVE SPELLS
    public static Entry fast_attack = add(fast_attack());
    private static Entry fast_attack() {
        var id = Identifier.of(MOD_ID, "fast_attack");
        var title = "Fast Attack";
        var description = "Performs some fast attacks with small forward momentum.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.range_mechanic = Spell.RangeMechanic.MELEE;
        spell.range = 0;
        spell.tier = 2;

        SpellBuilder.Casting.instant(spell);
        SpellBuilder.Target.none(spell);

        float forward_momentum_fa = 0.5F;
        var fast_attack_1 = new Spell.Delivery.Melee.Attack();
        fast_attack_1.attack_speed_multiplier = 2F;
        fast_attack_1.delay = 0.3F;
        fast_attack_1.hitbox = new Spell.Delivery.Melee.HitBox();
        fast_attack_1.hitbox.height = 0.2F;
        fast_attack_1.hitbox.width = 0.5F;
        fast_attack_1.forward_momentum = forward_momentum_fa;
        fast_attack_1.animation = PlayerAnimation.of("witcher_rpg:fast_attack_witcher_1");
        fast_attack_1.animation.speed = 1.1F;
        fast_attack_1.swing_sound = Sound.of(SpellEngineSounds.WEAPON_SWORD_SWING.id());

        var fast_attack_2 = new Spell.Delivery.Melee.Attack();
        fast_attack_2.attack_speed_multiplier = 2F;
        fast_attack_2.delay = 0.3F;
        fast_attack_2.hitbox = new Spell.Delivery.Melee.HitBox();
        fast_attack_2.hitbox.arc = 90;
        fast_attack_2.hitbox.height = 0.2F;
        fast_attack_2.hitbox.roll = 45;
        fast_attack_2.forward_momentum = forward_momentum_fa;
        fast_attack_2.additional_strike_delay = 0.2F;
        fast_attack_2.animation = PlayerAnimation.of("witcher_rpg:fast_attack_witcher_2");
        fast_attack_2.animation.speed = 1.1F;
        fast_attack_2.swing_sound = Sound.of(SpellEngineSounds.WEAPON_SWORD_SWING.id());

        var fast_attack_3 = new Spell.Delivery.Melee.Attack();
        fast_attack_3.attack_speed_multiplier = 2F;
        fast_attack_3.delay = 0.3F;
        fast_attack_3.hitbox = new Spell.Delivery.Melee.HitBox();
        fast_attack_3.hitbox.arc = 90;
        fast_attack_3.hitbox.height = 0.2F;
        fast_attack_3.hitbox.roll = -45;
        fast_attack_3.forward_momentum = forward_momentum_fa;
        fast_attack_3.additional_strike_delay = 0.2F;
        fast_attack_3.animation = PlayerAnimation.of("witcher_rpg:fast_attack_witcher_3");
        fast_attack_3.animation.speed = 1.1F;
        fast_attack_3.swing_sound = Sound.of(SpellEngineSounds.WEAPON_SWORD_SWING.id());

        var fast_attack_4 = new Spell.Delivery.Melee.Attack();
        fast_attack_4.attack_speed_multiplier = 2F;
        fast_attack_4.delay = 0.3F;
        fast_attack_4.hitbox = new Spell.Delivery.Melee.HitBox();
        fast_attack_4.hitbox.height = 0.2F;
        fast_attack_4.hitbox.width = 0.5F;
        fast_attack_4.forward_momentum = forward_momentum_fa;
        fast_attack_4.animation = PlayerAnimation.of("witcher_rpg:fast_attack_witcher_1");
        fast_attack_4.animation.speed = 1.1F;
        fast_attack_4.swing_sound = Sound.of(SpellEngineSounds.WEAPON_SWORD_SWING.id());

        SpellBuilder.Deliver.melee(spell, List.of(fast_attack_1, fast_attack_2, fast_attack_3,fast_attack_4));

        SpellBuilder.Cost.cooldown(spell, 8);
        spell.cost.cooldown.attempt_duration = 1F;

        return new Entry(id, spell, title, description).book(Book.FENCING);
    }
    public static Entry strong_attack = add(strong_attack());
    private static Entry strong_attack() {
        var id = Identifier.of(MOD_ID, "strong_attack");
        var title = "Strong Attack";
        var description = "Performs two heavy blows.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.range_mechanic = Spell.RangeMechanic.MELEE;
        spell.range = 0;
        spell.tier = 2;

        SpellBuilder.Casting.instant(spell);
        SpellBuilder.Target.none(spell);

        var attack1 = new Spell.Delivery.Melee.Attack();
        attack1.attack_speed_multiplier = 1.25F;
        attack1.delay = 0.1F;
        attack1.hitbox = new Spell.Delivery.Melee.HitBox();
        attack1.hitbox.arc = 90;
        attack1.hitbox.roll = 60F;
        attack1.hitbox.height = 0.25F;
        attack1.damage_bonus = 0.5F;
        attack1.animation = PlayerAnimation.of("witcher_rpg:strong_attack_witcher_1");
        attack1.animation.speed = 0.75F;
        attack1.swing_sound = Sound.of(SpellEngineSounds.WEAPON_SWORD_SWING.id());

        var attack2 = new Spell.Delivery.Melee.Attack();
        attack2.attack_speed_multiplier = 1.25F;
        attack2.delay = 0.3F;
        attack2.hitbox = new Spell.Delivery.Melee.HitBox();
        attack2.hitbox.height = 0.5F;
        attack2.damage_bonus = 0.5F;
        attack2.animation = PlayerAnimation.of("witcher_rpg:strong_attack_witcher_2");
        attack2.animation.speed = 0.75F;
        attack2.swing_sound = Sound.of(SpellEngineSounds.WEAPON_SWORD_SWING.id());

        SpellBuilder.Deliver.melee(spell, List.of(attack1, attack2));

        SpellBuilder.Cost.cooldown(spell, 8);
        spell.cost.cooldown.attempt_duration = 1F;

        return new Entry(id, spell, title, description).book(Book.FENCING);
    }
    public static final Entry BATTLE_TRANCE = add(battle_trance());
    private static Entry battle_trance() {
        var id = Identifier.of(MOD_ID, "battle_trance");
        var title = "Battle Trance";
        var description = "Enters a battle trance for {effect_duration} seconds, enhancing combat abilities.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.range = 0;
        spell.tier = 3;

        spell.active.cast.movement_speed = 0.75F;
        spell.active.cast.duration = 0;

        spell.target.type = Spell.Target.Type.CASTER;

        var effect = new Spell.Impact();
        effect.action = new Spell.Impact.Action();
        effect.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        effect.action.status_effect = new Spell.Impact.Action.StatusEffect();
        effect.action.status_effect.effect_id = WitcherStatusEffects.BATTLE_TRANCE.id.toString();
        effect.action.status_effect.duration = BATTLE_TRANCE_DURATION_SECONDS;
        effect.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.SET;
        effect.action.status_effect.amplifier = 0;
        effect.action.status_effect.show_particles = false;
        spell.impacts = List.of(effect);

        configureCooldown(spell, 30);
        spell.cost.exhaust = 0.8F;

        return new Entry(id, spell, title, description).book(Book.FENCING);
    }
    public static final Entry battle_trance_adrenaline_stacking = add(battle_trance_adrenaline_stacking());
    private static Entry battle_trance_adrenaline_stacking() {
        var id = Identifier.of(MOD_ID, "battle_trance_adrenaline_stacking");
        var title = "Battle Trance - Adrenaline";
        var stashEffect = WitcherStatusEffects.BATTLE_TRANCE;
        var impactEffect = WitcherStatusEffects.ADRENALINE_GAIN;
        var description = "While " + stashEffect.title + " is active, melee hits stack " + impactEffect.title + ".";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var stashTriggers = witcherMeleeImpacts();
        for (var trigger : stashTriggers) {
            trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        }

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = stashEffect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        spell.deliver.stash_effect.triggers = stashTriggers;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var buff = SpellBuilder.Impacts.effectAdd(impactEffect.id.toString(), BATTLE_TRANCE_DURATION_SECONDS, 1, 19);
        buff.action.status_effect.refresh_duration = false;
        spell.impacts = List.of(buff);

        return new Entry(id, spell, title, description);
    }

    public static void applyTweaksConfig() {
        var cap = Math.max(0, tweaksConfig.value.adrenaline_max_amplifier - 1);
        battle_trance_adrenaline_stacking.spell().impacts.get(0).action.status_effect.amplifier_cap = cap;
    }
    public static final Entry REND = add(rend());
    private static Entry rend() {
        var id = Identifier.of(MOD_ID, "rend");
        var title = "Rend";
        var description = "A powerful overhead strike that deals heavy damage and disables shields.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.range_mechanic = Spell.RangeMechanic.MELEE;
        spell.range = 0;
        spell.tier = 4;

        spell.active.cast.animation = PlayerAnimation.of("witcher_rpg:rend_cast");
        spell.active.cast.movement_speed = 0F;
        spell.active.cast.duration = 0.3F;

        SpellBuilder.Target.none(spell);

        spell.release.sound = Sound.withRandomness(Identifier.of("witcher_rpg:rend_spell"),1.2F);
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch("crimson_spore",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        20.0F, 1.0F, 15.0F),
        };

        var rend = new Spell.Delivery.Melee.Attack();
        rend.attack_speed_multiplier = 1.5F;
        rend.delay = 0.1F;
        rend.hitbox = new Spell.Delivery.Melee.HitBox();
        rend.hitbox.height = 1.0F;
        rend.hitbox.width = 1.0F;
        rend.hitbox.length = 5.0F;
        rend.damage_bonus = 1.0F;
        rend.animation = PlayerAnimation.of("witcher_rpg:rend_release");
        rend.animation.speed = 1F;

        SpellBuilder.Deliver.melee(spell, List.of(rend));

        var disrupt = SpellBuilder.Impacts.disrupt(true, 4F);

        spell.impacts = List.of(disrupt);

        configureCooldown(spell, 22);
        spell.cost.exhaust = 1.0F;
        spell.cost.durability = 1;

        return new Entry(id, spell, title, description).book(Book.FENCING);
    }
    public static final Entry WHIRL = add(whirl());
    private static Entry whirl() {
        var id = Identifier.of(MOD_ID, "whirl");
        var title = "Whirl";
        var description = "A spinning attack dealing {damage} damage to all nearby enemies and blocking arrows.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.range = 1.0F;
        spell.range_mechanic = Spell.RangeMechanic.MELEE;
        spell.tier = 4;

        spell.active.cast.movement_speed = 1.3F;
        spell.active.cast.duration = 2.5F;
        spell.active.cast.animation = PlayerAnimation.of("witcher_rpg:witcher_whirl");
        spell.active.cast.sound =  Sound.withVolume(Identifier.of("witcher_rpg:whirl"),0.6F);
        spell.active.cast.type = Spell.Active.Cast.Type.CHANNEL;
        spell.active.cast.channel = new Spell.Active.Cast.Channel();
        spell.active.cast.channel.ticks = 6;

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.angle_degrees = 360;

        var damage = new Spell.Impact();
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 1.35F;
        damage.action.damage.knockback = 0.3F;
        damage.sound = Sound.withVolume(Identifier.of("more_rpg_classes:crippling_strike"), 0.2F);
        spell.impacts = List.of(damage);

        configureCooldown(spell, 30);
        spell.cost.exhaust = 1.0F;
        spell.cost.durability = 1;

        return new Entry(id, spell, title, description).book(Book.FENCING);
    }

    /// ACTIVE SPELL HELPER IMPACTS
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

        return new Entry(id, spell, title, description);
    }
    public static Entry quen_active_helper = add(quen_active_helper());
    private static Entry quen_active_helper() {
        var id = Identifier.of(MOD_ID, "quen_active_helper");
        var description = "Quen Active Helper Impact";
        var effect = WitcherStatusEffects.QUEN_ACTIVE;
        var title = "Quen Active Helper Impact";

        var spell = SpellBuilder.createSpellActive();
        spell.tier = 0;
        spell.school = WitcherSpellSchools.QUEN;

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 20,0);
        buff.action.status_effect.amplifier_power_multiplier = 0.2F;
        spell.impacts = List.of(buff);
        configureCooldown(spell, 0);

        return new Entry(id, spell, title, description);
    }
    /// WEAPON SKILLS
    public static final Entry defensive_witcher_mechanics = add(defensive_witcher_mechanics());
    private static Entry defensive_witcher_mechanics() {
        var id = Identifier.of(MOD_ID, "defensive_witcher_mechanics");
        var description = "You sharpen your reflexes and prepare to block the next attack or arrow.";
        var spell = SpellBuilder.createSpellActive();
        spell.tier = 1;
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        var title = "Witcher Reflexes";

        spell.active.cast.movement_speed = 0.2F;
        spell.active.cast.duration = 5.0F;
        spell.active.cast.animation = PlayerAnimation.of("witcher_rpg:witcher_reflexes");

        SpellBuilder.Cost.cooldownGroupWeapon(spell);
        configureCooldown(spell, 15);
        spell.cost.cooldown.proportional = true;

        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
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
        modifier.power_modifier.power_multiplier = 0.15F;
        var impactfilter = new Spell.Modifier.ImpactFilter();
        impactfilter.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        impactfilter.school = WitcherSpellSchools.QUEN;
        modifier.impact_filters = List.of(impactfilter);
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
    }
    public static Entry improved_yrden = add(improved_yrden());
    private static Entry improved_yrden() {
        var id = Identifier.of(MOD_ID, "improved_yrden");
        var title = "Improved Yrden";
        var description = "Reduces cooldown of the Yrden Sign by {cooldown_duration_deduct} sec";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.YRDEN;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:yrden";
        modifier.cooldown_duration_deduct = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
    }
    //// SKILL TREE MODIFERS
    /// AARD MODIFIERS
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

        return new Entry(id, spell, title, description);
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
        return new Entry(id, spell, title, description);
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
        return new Entry(id, spell, title, description);
    }
    /// AXII MODIFIERS
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

        var debuff = SpellBuilder.Impacts.effectSet(WitcherStatusEffects.AXII_LETHARGY.id.toString(), 10, 0);
        debuff.action.status_effect.amplifier_cap = 5;
        debuff.action.status_effect.amplifier_power_multiplier = 0.15F;
        spell.impacts = List.of(debuff);

        SpellBuilder.Cost.cooldown(spell, 0.5F);

        return new Entry(id, spell, title, description);
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
        return new Entry(id, spell, title, description);
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
        return new Entry(id, spell, title, description);
    }
    /// IGNI MODIFIERS
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
        return new Entry(id, spell, title, description);
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
        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
    }
    /// QUEN MODIFIERS
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

        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
    }
    /// YRDEN MODIFIERS
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

        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
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

        return new Entry(id, spell, title, description);
    }
    /// FAST ATTACK MODIFIERS
    public static final Entry muscle_memory = add(muscle_memory());
    private static Entry muscle_memory() {
        var id = Identifier.of(MOD_ID, "muscle_memory");
        var effect = WitcherStatusEffects.MUSCLE_MEMORY;
        var description = "Increasing melee attack speed by {bonus}, stacking up to {effect_amplifier_cap} times, lasting for {effect_duration} seconds.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var passiveMeleeTrigger = new Spell.Trigger();
        passiveMeleeTrigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        spell.passive.triggers = List.of(passiveMeleeTrigger);

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = effect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        var stashMeleeTrigger = new Spell.Trigger();
        stashMeleeTrigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        stashMeleeTrigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.deliver.stash_effect.triggers = List.of(stashMeleeTrigger);

        var buff = SpellBuilder.Impacts.effectAdd(effect.id.toString(),10,1,5);
        buff.action.status_effect.refresh_duration = false;
        spell.impacts = List.of(buff);
        configureCooldown(spell, 20);

        return new Entry(id, spell, title, description, mutator, null);
    }
    public static Entry whirl_boost_a = add(whirl_boost_a());
    private static Entry whirl_boost_a() {
        var id = Identifier.of(MOD_ID, "whirl_boost_a");
        var title = "Slicing Whirl";
        var description = "Increases the power of Whirl by {power_multiplier}";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:whirl";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.25F;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description);
    }
    /// STRONG ATTACK MODIFIERS
    public static final Entry strength_training = add(strength_training());
    private static Entry strength_training() {
        var id = Identifier.of(MOD_ID, "strength_training");
        var effect = WitcherStatusEffects.STRENGTH_TRAINING;
        var description = "Increasing melee attack damage by {bonus}, stacking up to {effect_amplifier_cap} times, lasting for {effect_duration} seconds.";
        var spell = SpellBuilder.createSpellPassive();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };

        var passiveMeleeTrigger = new Spell.Trigger();
        passiveMeleeTrigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        spell.passive.triggers = List.of(passiveMeleeTrigger);

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = effect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        var stashMeleeTrigger = new Spell.Trigger();
        stashMeleeTrigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        stashMeleeTrigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.deliver.stash_effect.triggers = List.of(stashMeleeTrigger);

        var buff = SpellBuilder.Impacts.effectAdd(effect.id.toString(),10,1,5);
        buff.action.status_effect.refresh_duration = false;
        spell.impacts = List.of(buff);
        configureCooldown(spell, 20);

        return new Entry(id, spell, title, description, mutator, null);
    }
    public static Entry rend_boost_a = add(rend_boost_a());
    private static Entry rend_boost_a() {
        var id = Identifier.of(MOD_ID, "rend_boost_a");
        var title = "Slashing Rend";
        var description = "Increases the power of Rend by {power_multiplier}";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:rend";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.power_multiplier = 0.3F;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description);
    }
    /// DEFENSE MODIFIERS
    public static Entry witcher_reflexes_boost_a = add(witcher_reflexes_boost_a());
    private static Entry witcher_reflexes_boost_a() {
        var id = Identifier.of(MOD_ID, "witcher_reflexes_boost_a");
        var title = "Superhuman Reflexes";
        var description = "Increases the amount of Blocks by {effect_amplifier_add} for Witcher Reflexes.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:defensive_witcher_mechanics";
        modifier.effect_amplifier_add = 2;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description);
    }
    public static Entry arrow_deflection = add(arrow_deflection());
    private static Entry arrow_deflection() {
        var id = Identifier.of(MOD_ID, "arrow_deflection");
        var title = "Arrow Deflection";
        var description = "While blocking Arrows with Witcher Reflexes, you send the Arrow back to the shooter.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:defensive_witcher_mechanics";
        modifier.impacts = List.of();

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description);
    }
    public static Entry counterattack = add(counterattack());
    private static Entry counterattack() {
        var id = Identifier.of(MOD_ID, "counterattack");
        var title = "Counterattack";
        var description = "After blocking, with your next melee attack you have {bonus} attack damage.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.shieldBlock();
        spell.passive.triggers = List.of(trigger);


        return new Entry(id, spell, title, description, null, null);
    }
    /// PASSIVE SPELLS
    /// GENERIC PASSIVE WITCHER TRAITS
    public static final Entry griffin_school_technique = add(griffin_school_technique());
    private static Entry griffin_school_technique() {
        var id = Identifier.of(MOD_ID, "griffin_school_technique");
        var title = "Griffin School Technique";
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

        SpellBuilder.Cost.cooldown(spell, 45F);

        return new Entry(id, spell, title, description);
    }
    public static final Entry cat_school_technique = add(cat_school_technique());
    private static Entry cat_school_technique() {
        var id = Identifier.of(MOD_ID, "cat_school_technique");
        var title = "Cat School Technique";
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

        spell.passive.triggers = SpellBuilder.Triggers.withConditionMustWield(
                badEffectMeleeHit()
        );
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

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

        SpellBuilder.Cost.cooldown(spell, 45F);

        return new Entry(id, spell, title, description);
    }
    public static final Entry bear_school_technique = add(bear_school_technique());
    private static Entry bear_school_technique() {
        var id = Identifier.of(MOD_ID, "bear_school_technique");
        var title = "Bear School Technique";
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

        SpellBuilder.Cost.cooldown(spell, 45F);

        return new Entry(id, spell, title, description, mutator, null);
    }
    public static final Entry wolf_school_technique = add(wolf_school_technique());
    private static Entry wolf_school_technique() {
        var id = Identifier.of(MOD_ID, "wolf_school_technique");
        var title = "Wolf School Technique";
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

        SpellBuilder.Cost.cooldown(spell, 45F);

        return new Entry(id, spell, title, description);
    }
    //// WITCHER RELIC SPELLS
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

        return new Entry(id, spell, title, description);
    }
    public static Entry sunstone = add(sunstone());
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

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_weapon_charge");
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

        return new Entry(id, spell, title, description, mutator, null);
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
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.single("witcher_rpg:spell_projectile/crystal_skull");
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
        return new Entry(id, spell, title, description);
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
        return new Entry(id, spell, title, description);
    }
    /// GRANDMASTER SET PASSIVES
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

        spell.passive.triggers = badEffectMeleeHit();
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var debuff = SpellBuilder.Impacts.effectSet(effect.id.toString(),5,0);
        debuff.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.smoke_medium.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        3F, 0.1F, 0.3F).color(Color.RED.toRGBA())
        };
        spell.impacts = List.of(debuff);

        SpellBuilder.Cost.cooldown(spell, 20F);

        return new Entry(id, spell, title, description, mutator, null);
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

        return new Entry(id, spell, title, description, mutator, null);
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

        var trigger = SpellBuilder.Triggers.activeSpellHit(1.0F, "spell_power:aard");
        var condition = new Spell.TargetCondition();
        condition.entity_predicate_id = HAS_YRDEN.id().toString();
        trigger.target_conditions = List.of(condition);
        spell.passive.triggers = List.of(trigger);

        var damage = SpellBuilder.Impacts.damage(1.25F, 0F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:yrden_sign_cast",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        3F, 0.1F, 0.3F),
                new ParticleBatch("witcher_rpg:aard_sign_cast",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        3F, 0.3F, 0.5F).extent(2)
        };
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Entry(id, spell, title, description);
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
        effect.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:quen_sign_cast",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        3F, 0.3F, 0.5F).extent(2)
        };
        spell.impacts = List.of(effect);

        SpellBuilder.Cost.cooldown(spell, 60F);

        return new Entry(id, spell, title, description);
    }
    /// SWORD PASSIVE SPELLS
    public static final Entry AERONDIGHT_PASSIVE = add(aerondight_passive());
    public static Entry aerondight_passive() {
        var id = Identifier.of(MOD_ID, "aerondight_passive");
        var title = "Aerondight";
        var description = "Each hit deals bonus arcane damage and builds up charges, increasing damage.";
        var spell = passiveSpellBase();
        spell.school = SpellSchools.ARCANE;
        spell.tier = 8;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.chance = 1.0F;
        trigger.equipment_condition = EquipmentSlot.MAINHAND;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var damage = new Spell.Impact();
        damage.attribute = "minecraft:generic.attack_damage";
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.1F;
        damage.particles = new ParticleBatch[]{
                new ParticleBatch("firework",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.05F, 0.2F)
        };

        var charge = new Spell.Impact();
        charge.action = new Spell.Impact.Action();
        charge.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        charge.action.status_effect = new Spell.Impact.Action.StatusEffect();
        charge.action.status_effect.effect_id = WitcherStatusEffects.AERONDIGHT_CHARGE.id.toString();
        charge.action.status_effect.duration = 10;
        charge.action.status_effect.amplifier_cap = 10;
        charge.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
        charge.action.status_effect.amplifier = 1;
        charge.action.status_effect.refresh_duration = true;
        charge.action.status_effect.show_particles = false;
        charge.action.apply_to_caster = true;
        charge.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPELL,
                        SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 2.0F, 0.05F, 0.1F, 0F, 0F)
        };

        spell.impacts = List.of(damage, charge);

        spell.cost = new Spell.Cost();
        spell.cost.batching = true;
        configureCooldown(spell, 1);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description);
    }
    public static final Entry IRIS_PASSIVE = add(iris_passive());
    public static Entry iris_passive() {
        var id = Identifier.of(MOD_ID, "iris_passive");
        var title = "Iris";
        var description = "Each hit builds up charges, increasing physical damage.";
        var spell = passiveSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.tier = 8;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.chance = 1.0F;
        trigger.equipment_condition = EquipmentSlot.MAINHAND;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var charge = new Spell.Impact();
        charge.action = new Spell.Impact.Action();
        charge.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        charge.action.status_effect = new Spell.Impact.Action.StatusEffect();
        charge.action.status_effect.effect_id = WitcherStatusEffects.IRIS_CHARGE.id.toString();
        charge.action.status_effect.duration = 10;
        charge.action.status_effect.amplifier_cap = 10;
        charge.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.ADD;
        charge.action.status_effect.amplifier = 1;
        charge.action.status_effect.refresh_duration = true;
        charge.action.status_effect.show_particles = false;
        charge.action.apply_to_caster = true;
        charge.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPELL,
                        SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 2.0F, 0.05F, 0.1F, 0F, 0F).color(Color.RED.toRGBA())
        };

        var damage = new Spell.Impact();
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.1F;
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.STRIPE,
                        SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
                        25.0F, 0.2F, 1.0F).extent(0.1F)
        };

        spell.impacts = List.of(charge, damage);

        spell.cost = new Spell.Cost();
        spell.cost.batching = true;
        configureCooldown(spell, 1);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description);
    }
    public static final Entry SILVER_SWORD_PASSIVE = add(silver_sword_passive());
    public static Entry silver_sword_passive() {
        var id = Identifier.of(MOD_ID, "silver_sword_passive");
        var title = "Silver Sword";
        var description = "Deals bonus damage to silver vulnerable targets.";
        var spell = passiveSpellBase();
        spell.school = SpellSchools.ARCANE;
        spell.tier = 8;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.chance = 1.0F;
        trigger.equipment_condition = EquipmentSlot.MAINHAND;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var damage = new Spell.Impact();
        damage.attribute = "minecraft:generic.attack_damage";
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.2F;
        silverVulnerabilityAllow(damage);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch("firework",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.05F, 0.2F)
        };

        spell.impacts = List.of(damage);

        spell.cost = new Spell.Cost();
        spell.cost.batching = true;
        configureCooldown(spell, 1);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description);
    }
    public static final Entry REACH_OF_THE_DAMNED_PASSIVE = add(reach_of_the_damned_passive());
    private static Entry reach_of_the_damned_passive() {
        var id = Identifier.of(MOD_ID, "reach_of_the_damned_passive");
        var title = "Reach of the Damned";
        var description = "Deals massive bonus damage to targets below 50%% health.";
        var spell = passiveSpellBase();
        spell.school = SpellSchools.ARCANE;
        spell.tier = 8;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.equipment_condition = EquipmentSlot.MAINHAND;
        var condition = new Spell.TargetCondition();
        condition.health_percent_below = 0.5F;
        trigger.target_conditions = List.of(condition);
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var damage = new Spell.Impact();
        damage.attribute = "minecraft:generic.attack_damage";
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.5F;
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPELL,
                        SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.05F, 0.2F)
        };

        spell.impacts = List.of(damage);

        spell.cost = new Spell.Cost();
        spell.cost.batching = true;
        configureCooldown(spell, 10);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description);
    }
}
