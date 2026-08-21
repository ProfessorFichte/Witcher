package net.witcher_rpg.spell;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.entity.SpellEntityPredicates;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ModelEffect;
import net.spell_engine.api.spell.fx.ModelEffectBuilder;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.target.SpellTarget;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.effect.WitcherStatusEffects;
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

    public static List<Entry> allEntries() {
        var all = new ArrayList<Entry>(entries);
        all.addAll(WitcherModifiers.entries);
        all.addAll(WitcherPassives.entries);
        return all;
    }
    private static Spell activeSpellBase() {
        var spell = new Spell();
        spell.type = Spell.Type.ACTIVE;
        spell.active = new Spell.Active();
        spell.active.cast = new Spell.Active.Cast();

        spell.learn = new Spell.Learn();

        return spell;
    }

    static Spell passiveSpellBase() {
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

    static Spell modifierSpellBase() {
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

    static Spell createModifierAlikePassiveSpell() {
        var spell = SpellBuilder.createSpellPassive();
        spell.range = 0;
        spell.tooltip = new Spell.Tooltip();
        spell.tooltip.show_activation = false;
        return spell;
    }

    static Spell.Impact createEffectImpact(Identifier effectId, float duration) {
        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effectId.toString();
        buff.action.status_effect.duration = duration;
        return buff;
    }

    static Spell.Impact.TargetModifier createImpactModifier(String entityType) {
        var condition = new Spell.TargetCondition();
        condition.entity_type = entityType;
        var modifier = new Spell.Impact.TargetModifier();
        modifier.conditions = List.of(condition);
        return modifier;
    }

    static void configureCooldown(Spell spell, float duration) {
        if (spell.cost == null) {
            spell.cost = new Spell.Cost();
        }
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = duration;
    }

    static void undeadDeny(Spell.Impact impact) {
        var modifier = createImpactModifier("#minecraft:undead");
        modifier.execute = TriState.DENY;
        impact.target_modifiers = List.of(modifier);
    }
    static void silverVulnerabilityAllow(Spell.Impact impact) {
        var modifier = createImpactModifier("#witcher_rpg:silver_vulnerable");
        modifier.execute = TriState.ALLOW;
        impact.target_modifiers = List.of(modifier);
    }
    static void freezeImmunityDeny(Spell.Impact impact) {
        var modifier = createImpactModifier("#minecraft:freeze_immune_entity_types");
        modifier.execute = TriState.DENY;
        impact.target_modifiers = List.of(modifier);
    }
    static void yrdenAllow(Spell.Impact impact) {
        var modifier = createImpactModifier("#witcher_rpg:yrden_vulnerable");
        modifier.execute = TriState.ALLOW;
        impact.target_modifiers = List.of(modifier);
    }
    static final SpellEntityPredicates.Entry HAS_YRDEN =
            SpellEntityPredicates.hasEffectOptimized(Identifier.of("witcher_rpg", "yrden_circle"));
    public static final SpellEntityPredicates.Entry HAS_WITCHER_SENSES_EXPOSED =
            SpellEntityPredicates.hasEffectOptimized(Identifier.of(MOD_ID, "witcher_senses_exposed"));
    static final SpellEntityPredicates.Entry HAS_BATTLE_TRANCE =
            SpellEntityPredicates.hasEffectOptimized(Identifier.of(MOD_ID, "battle_trance"));
    static final float BATTLE_TRANCE_DURATION_SECONDS = 5F;

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

    public static final String MONSTER_HUNTER =  "monster_hunter";
    public static final String MUTANT = "mutant";
    public static final String SIGNS =  "signs";
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
        spell.group = SIGNS;

        spell.release.animation = PlayerAnimation.of("witcher_rpg:sign_cast_short");
        spell.release.sound = new Sound(Sounds.AARD_SIGN.id());
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
        spell.group = SIGNS;

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
        spell.group = SIGNS;

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
                        .light(LightEmission.RADIATE)
                        .positioning(0F)
                        .scale(3.0F)
                        .initialTranslateY(0.2F)
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
        spell.group = SIGNS;

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
        spell.group = SIGNS;

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
        spell.group = SIGNS;

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
        spell.group = SIGNS;

        spell.active.cast.movement_speed = 0.1F;
        spell.active.cast.duration = 0;
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch("witcher_rpg:aard_sign_cast",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT,
                        0.2F, 0.01F, 0.1F)
        };
        spell.active.cast.start_sound = new Sound(Sounds.AARD_SIGN.id());

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.angle_degrees = 360;

        spell.release.animation = PlayerAnimation.of("witcher_rpg:sign_cast_ground");
        spell.release.sound = new Sound(Sounds.AARD_SIGN.id());
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
        var description = "Places a magical trap that deals {trap_damage} damage and slows enemies that enter it.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var world = args.player().getWorld();
            if (world == null) return args.description();
            var optional = SpellRegistry.from(world).getEntry(yrden_glyph_impact.id());
            if (optional.isEmpty()) return args.description();
            var estimated = SpellHelper.estimate(optional.get().value(), args.player(), ItemStack.EMPTY);
            var desc = args.description();
            if (!estimated.damage().isEmpty()) {
                var dmg = estimated.damage().get(0);
                desc = desc.replace("{trap_damage}", SpellTooltip.formattedRange(dmg.min(), dmg.max()));
            }
            return desc;
        };
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.YRDEN;
        spell.range = 3;
        spell.tier = 4;
        spell.group = SIGNS;

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

        return new Entry(id, spell, title, description).mutator(mutator).book(Book.SIGNS);
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
        spell.group = SIGNS;

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
    public static final Entry QUEN_ACTIVE_SHIELD = add(quen_active_shield());
    private static Entry quen_active_shield() {
        var id = Identifier.of(MOD_ID, "quen_active_shield");
        var title = "Quen Active Shield";
        var description = "Creates an active protective shield that absorbs damage and heals the caster.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.QUEN;
        spell.range = 0;
        spell.tier = 4;
        spell.group = SIGNS;

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
        spell.group = MONSTER_HUNTER;

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
    public static final Entry WITCHER_SENSES = add(witcher_senses());
    private static Entry witcher_senses() {
        var id = Identifier.of(MOD_ID, "witcher_senses");
        var title = "Witcher Senses";
        var description = "Reveals all enemies within range, making them Glow and exposing their weaknesses for {effect_duration} seconds. Critical hits against exposed enemies deal more damage and are more likely to land.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.range = 16;
        spell.tier = 3;
        spell.group = MONSTER_HUNTER;

        SpellBuilder.Casting.instant(spell);

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.angle_degrees = 360;

        spell.release.animation = PlayerAnimation.of("witcher_rpg:sign_cast_ground");
        spell.release.sound = Sound.withVolume(Sounds.WITCHER_SENSES_EXPOSED.id(), 0.6F);
        spell.release.particles_scaled_with_ranged = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.area_effect_637.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.GROUND,
                        1, 0, 0)
                        .scale(0.25F)
                        .color(Color.WHITE.toRGBA()),
        };

        var expose = createEffectImpact(Identifier.of(WitcherStatusEffects.WITCHER_SENSES_EXPOSED.id.toString()), 6);
        spell.impacts = List.of(expose);

        configureCooldown(spell, 25);
        spell.cost.exhaust = 0.6F;

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
        spell.group = MUTANT;

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
        var description = "Enters a battle trance for {effect_duration} seconds, enhancing combat abilities. " +
                "Melee hits stack Adrenaline, increasing attack damage by {adrenaline_bonus} per stack, up to {adrenaline_max_stacks} stacks.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = WitcherStatusEffects.ADRENALINE_GAIN.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            var maxStacks = battle_trance_adrenaline_stacking.spell().impacts.get(0).action.status_effect.amplifier_cap;
            return args.description()
                    .replace("{adrenaline_bonus}", bonus)
                    .replace("{adrenaline_max_stacks}", String.valueOf(maxStacks));
        };
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.range = 0;
        spell.tier = 3;
        spell.group = MUTANT;

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

        return new Entry(id, spell, title, description).mutator(mutator).book(Book.FENCING);
    }
    public static final Entry REND = add(rend());
    private static Entry rend() {
        var id = Identifier.of(MOD_ID, "rend");
        var title = "Rend";
        var description = "A powerful overhead strike that deals heavy damage and disables shields. Range and Damage increases the longer you charge.";
        var spell = activeSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.range_mechanic = Spell.RangeMechanic.MELEE;
        spell.range = 0.5F;
        spell.tier = 4;
        spell.group = MUTANT;

        var charge = SpellBuilder.Casting.charge(spell, 2.0F);
        charge.min_release_ratio = 0.1F;
        charge.output_scaling = 1.2F;
        charge.bonus.range_add = 2.0F;

        spell.active.cast.animation = PlayerAnimation.of("witcher_rpg:rend_cast");
        spell.active.cast.movement_speed = 0F;

        SpellBuilder.Target.none(spell);

        spell.release.sound = Sound.withRandomness(Identifier.of("witcher_rpg:rend_spell"),1.2F);
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch("crimson_spore",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        20.0F, 1.0F, 15.0F),
        };

        var rend = new Spell.Delivery.Melee.Attack();
        rend.attack_speed_multiplier = 1.5F;
        rend.damage_bonus = 1.5F;
        rend.delay = 0.1F;
        rend.hitbox = new Spell.Delivery.Melee.HitBox();
        rend.hitbox.height = 1.0F;
        rend.hitbox.width = 1.0F;
        rend.hitbox.length = 5.0F;
        rend.animation = PlayerAnimation.of("witcher_rpg:rend_release");
        rend.animation.speed = 1F;

        var damage = SpellBuilder.Impacts.damage(0F);

        SpellBuilder.Deliver.melee(spell, List.of(rend));

        var disrupt = SpellBuilder.Impacts.disrupt(true, 4F);

        spell.impacts = List.of(disrupt,damage);

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
        spell.group = MONSTER_HUNTER;

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
    public static void applyTweaksConfig() {
        var cap = Math.max(0, tweaksConfig.value.adrenaline_max_amplifier - 1);
        battle_trance_adrenaline_stacking.spell().impacts.get(0).action.status_effect.amplifier_cap = cap;
    }

    public static final Entry battle_trance_adrenaline_stacking = add(battle_trance_adrenaline_stacking());
    private static Entry battle_trance_adrenaline_stacking() {
        var id = Identifier.of(MOD_ID, "helpers/battle_trance_adrenaline_stacking");
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
    public static final Entry yrden_glyph_impact = add(yrden_glyph_impact());
    private static Entry yrden_glyph_impact() {
        var id = Identifier.of(MOD_ID, "helpers/yrden_glyph_impact");
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
        var id = Identifier.of(MOD_ID, "helpers/quen_active_helper");
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
}
