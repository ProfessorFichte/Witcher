package net.witcher_rpg.spell;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.entity.SpellEntityPredicates;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_power.api.SpellSchools;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.entity.attribute.WitcherAttributes;

import java.util.ArrayList;
import java.util.List;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

import static net.witcher_rpg.spell.WitcherSpells.*;

public class WitcherModifiers {
    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    /// GEMERIC WITCHER MEDALLION PASSIVE
    public static final Entry increased_medallion_senses = add(increased_medallion_senses());
    private static Entry increased_medallion_senses() {
        var id = Identifier.of(MOD_ID, "increased_medallion_senses");
        var title = "Medallion's Humming";
        var description = "Increases the range of Witcher Senses by {range_add}.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:witcher_senses";
        modifier.range_add = 5;
        spell.modifiers = List.of(modifier);

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
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(30).speed(0.4F, 0.4F)),
                ParticleGroupBuilder.of(SpellEngineParticles.frost_shard)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.4F, 0.6F)));
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
        area_impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.flame)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(30).speed(0.5F, 0.5F)));
        area_impact.sound = new Sound(SpellEngineSounds.GENERIC_FIRE_IGNITE.id().toString());
        spell.area_impact = area_impact;


        SpellBuilder.Cost.cooldown(spell, 5.0F);
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
        // V1 `electric_arc_a` is retired; `electricArc(lightning_arc_A)` is its 1.10 rebuild.
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.electricArc(SpellEngineParticles.lightning_arc_A)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(15).speed(0.15F, 0.2F)));
        damage.sound = new Sound("");
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 1);

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
        // V1 `electric_arc_a` is retired; `electricArc(lightning_arc_A)` is its 1.10 rebuild.
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.electricArc(SpellEngineParticles.lightning_arc_A)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(15).speed(0.15F, 0.2F)));
        damage.sound = new Sound("");
        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 1);

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
        impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_stripe, ParticleGroup.Motion.ASCEND, Color.ARCANE)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F) // V1 WIDE_PIPE
                                .anchor(ParticleGroup.Anchor.GROUND)
                                .count(20).speed(0.2F, 0.2F)
                                .extent(1.0F)));
        modifier.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifier.impacts = List.of(impact);

        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description, mutator, null);
    }
}
