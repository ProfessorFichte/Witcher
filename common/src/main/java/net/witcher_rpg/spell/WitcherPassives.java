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
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ModelEffect;
import net.spell_engine.api.spell.fx.ModelEffectBuilder;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.util.TriState;
import net.spell_engine.api.entity.SpellEngineAttributes;
import net.spell_engine.api.spell.tooltip.TooltipTokens;
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

import static net.witcher_rpg.spell.WitcherSpells.*;

public class WitcherPassives {
    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static void applyTweaksConfig() {
        var cap = Math.max(0, tweaksConfig.value.adrenaline_max_amplifier - 1);
        battle_trance_adrenaline_stacking.spell().impacts.get(0).action.status_effect.amplifier_cap = cap;
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
        custom.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.dripping_blood)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(35).speed(0.4F, 1.0F)),
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_medium)
                        .color(Color.RED.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(10).speed(0.2F, 0.5F)));

        spell.impacts = List.of(custom);

        SpellBuilder.Cost.cooldown(spell, 45F);

        return new Entry(id, spell, title, description);
    }
    public static final Entry bear_school_technique = add(bear_school_technique());
    private static Entry bear_school_technique() {
        var id = Identifier.of(MOD_ID, "bear_school_technique");
        var title = "Bear School Technique";
        var effect = WitcherStatusEffects.BEAR_SCHOOL_MEDALLION;
        // Single modifier, so the token's blank-attribute fallback is unambiguous. `ABS` because the
        // configured value is negative (-20%) while the prose already says "reduces ... by" - the old
        // mutator used the raw value and rendered "reduces incoming damage by -20%".
        var description = "Taking Damage reduces incoming damage by "
                + TooltipTokens.effect(effect.id, 0, null, TooltipTokens.Format.ABS)
                + " for {effect_duration} secs.";
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

        return new Entry(id, spell, title, description, null);
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
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.BURST, Color.ELECTRIC)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.1F, 0.2F)));
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
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.BURST, Color.GREEN)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.1F, 0.3F)));
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
        fire.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.BURST, Color.WHITE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.1F, 0.3F)));
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
        // The effect carries two modifiers (damage taken +20%, movement speed -20%) and the status
        // effect's modifier map is unordered, so the attribute is named explicitly rather than read
        // by list position the way the old mutator did (`attributes().get(0)`).
        var description = "Fencing Spells and melee hits inflict injuries if the target has a bad effect, " +
                "increasing incoming damage and reducing movement speed by "
                + TooltipTokens.effect(effect.id, 0, SpellEngineAttributes.DAMAGE_TAKEN.id)
                + " {effect_duration} sec.";
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
        debuff.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_medium)
                        .color(Color.RED.toRGBA())
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(3F).speed(0.1F, 0.3F)));
        spell.impacts = List.of(debuff);

        SpellBuilder.Cost.cooldown(spell, 20F);

        return new Entry(id, spell, title, description, null);
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
        // Mod-local particle types with their own factories — batch geometry only, exactly as in V1.
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("witcher_rpg:yrden_sign_cast")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(3F).speed(0.1F, 0.3F)),
                ParticleGroupBuilder.of("witcher_rpg:aard_sign_cast")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(3F).speed(0.3F, 0.5F)
                                .extent(2F)));
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
        // Mod-local particle type with its own factory — batch geometry only, exactly as in V1.
        effect.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("witcher_rpg:quen_sign_cast")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(3F).speed(0.3F, 0.5F)
                                .extent(2F)));
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
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("firework")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.05F, 0.2F)));

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
        charge.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spell, ParticleGroup.Motion.ASCEND)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(2.0F).speed(0.05F, 0.1F)));

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
        charge.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spell, ParticleGroup.Motion.ASCEND, Color.RED)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(2.0F).speed(0.05F, 0.1F)));

        var damage = new Spell.Impact();
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.damage.spell_power_coefficient = 0.1F;
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_stripe, ParticleGroup.Motion.BURST)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F) // V1 WIDE_PIPE
                                .count(25.0F).speed(0.2F, 1.0F)
                                .extent(0.1F)));

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
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("firework")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.05F, 0.2F)));

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
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spell, ParticleGroup.Motion.ASCEND)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.05F, 0.2F)));

        spell.impacts = List.of(damage);

        spell.cost = new Spell.Cost();
        spell.cost.batching = true;
        configureCooldown(spell, 10);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description);
    }
}
