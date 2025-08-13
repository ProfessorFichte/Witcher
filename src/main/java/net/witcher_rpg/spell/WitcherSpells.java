package net.witcher_rpg.spell;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_engine.internals.target.SpellTarget;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.sounds.Sounds;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    ///MODIFERS
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

}
