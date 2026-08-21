package net.witcher_rpg.spell;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.effect.SpellEngineEffects;
import net.spell_engine.api.entity.SpellEntityPredicates;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.util.TriState;
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
        var id = Identifier.of(MOD_ID, "trinket_modifiers/increased_medallion_senses");
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
        var id = Identifier.of(MOD_ID, "glyph_modifiers/greater_aard_glyph");
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
        var id = Identifier.of(MOD_ID, "glyph_modifiers/greater_axii_glyph");
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
        var id = Identifier.of(MOD_ID, "glyph_modifiers/greater_igni_glyph");
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
        var id = Identifier.of(MOD_ID, "glyph_modifiers/greater_quen_glyph");
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
        var id = Identifier.of(MOD_ID, "glyph_modifiers/greater_yrden_glyph");
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
        var id = Identifier.of(MOD_ID, "equipment_set_modifiers/improved_whirl");
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
        var id = Identifier.of(MOD_ID, "equipment_set_modifiers/improved_yrden");
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
        var id = Identifier.of(MOD_ID, "equipment_set_modifiers/improved_aard");
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
        var id = Identifier.of(MOD_ID, "equipment_set_modifiers/improved_rend");
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
    public static final Entry grandmaster_griffin = add(grandmaster_griffin());
    private static Entry grandmaster_griffin() {
        var id = Identifier.of(MOD_ID, "equipment_set_modifiers/grandmaster_griffin");
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
    //// SKILL TREE MODIFERS
    public static Entry aard_shockwave = add(aard_shockwave());
    private static Entry aard_shockwave() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/aard_shockwave");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/aard_frostbite");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/axii_lethargy");
        var title = "Lethargy";
        var effect = WitcherStatusEffects.AXII_LETHARGY;
        var description = "Axii signs inflict Lethargy slowing the target by {bonus}.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.AXII;

        Spell.Trigger trigger = new Spell.Trigger();
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.STATUS_EFFECT.toString();
        trigger.type = net.spell_engine.api.spell.Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.school = WitcherSpellSchools.AXII.id.toString();
        spell.passive.triggers = List.of(trigger);

        var debuff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 10, 0);
        debuff.action.status_effect.amplifier_cap = 5;
        debuff.action.status_effect.amplifier_power_multiplier = 0.15F;
        spell.impacts = List.of(debuff);

        SpellBuilder.Cost.cooldown(spell, 0.5F);

        return new Entry(id, spell, title, description, mutator, null);
    }
    public static Entry axii_link = add(axii_link());
    private static Entry axii_link() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/axii_link");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/axii_domination");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/igni_melt_armor");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/igni_combustion");
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
    /// QUEN MODIFIERS
    public static final Entry quen_exploding_shield = add(quen_exploding_shield());
    private static Entry quen_exploding_shield() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/quen_exploding_shield");
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
    public static final Entry quen_discharge = add(quen_discharge());
    private static Entry quen_discharge() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/quen_discharge");
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
    /// FAST ATTACK MODIFIERS
    public static final Entry muscle_memory = add(muscle_memory());
    private static Entry muscle_memory() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/muscle_memory");
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
        passiveMeleeTrigger.spell = new Spell.Trigger.SpellCondition();
        passiveMeleeTrigger.spell.id = "witcher_rpg:fast_attack";
        spell.passive.triggers = List.of(passiveMeleeTrigger);

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = effect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        var stashMeleeTrigger = new Spell.Trigger();
        stashMeleeTrigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        stashMeleeTrigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        stashMeleeTrigger.spell = new Spell.Trigger.SpellCondition();
        stashMeleeTrigger.spell.id = "witcher_rpg:fast_attack";
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/strength_training");
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
        passiveMeleeTrigger.spell = new Spell.Trigger.SpellCondition();
        passiveMeleeTrigger.spell.id = "witcher_rpg:strong_attack";
        spell.passive.triggers = List.of(passiveMeleeTrigger);

        spell.deliver.type = Spell.Delivery.Type.STASH_EFFECT;
        spell.deliver.stash_effect = new Spell.Delivery.StashEffect();
        spell.deliver.stash_effect.id = effect.id.toString();
        spell.deliver.stash_effect.consume = 0;
        var stashMeleeTrigger = new Spell.Trigger();
        stashMeleeTrigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        stashMeleeTrigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        stashMeleeTrigger.spell = new Spell.Trigger.SpellCondition();
        stashMeleeTrigger.spell.id = "witcher_rpg:strong_attack";
        spell.deliver.stash_effect.triggers = List.of(stashMeleeTrigger);

        var buff = SpellBuilder.Impacts.effectAdd(effect.id.toString(),10,1,5);
        buff.action.status_effect.refresh_duration = false;
        spell.impacts = List.of(buff);
        configureCooldown(spell, 20);

        return new Entry(id, spell, title, description, mutator, null);
    }
    public static Entry counterattack = add(counterattack());
    private static Entry counterattack() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/counterattack");
        var title = "Counterattack";
        var effect = WitcherStatusEffects.COUNTERATTACK_READY;
        var description = "After blocking, your next melee attack deals {bonus} bonus attack damage.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        spell.target.type = Spell.Target.Type.CASTER;

        var trigger = SpellBuilder.Triggers.shieldBlock();
        spell.passive.triggers = List.of(trigger);

        spell.impacts = List.of(createEffectImpact(Identifier.of(effect.id.toString()), 8F));

        return new Entry(id, spell, title, description, mutator, null);
    }
    /// AARD MODIFIERS
    public static final Entry aard_far_reach = add(aard_far_reach());
    private static Entry aard_far_reach() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/aard_far_reach");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/igni_pyromaniac");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/quen_warding_shield");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/yrden_sustained_glyphs");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/yrden_binding_glyphs");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/yrden_supercharged_glyphs");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/whirl_boost_a");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/rend_boost_a");
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
    /// SKILL TREE ADD-ON MODIFIERS
    public static Entry fast_precise_blows = add(fast_precise_blows());
    private static Entry fast_precise_blows() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/fast_precise_blows");
        var title = "Precise Blows";
        var description = "Increases the Critical Damage of Fast Attack by {critical_damage_bonus}";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:fast_attack";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.critical_damage_bonus = 0.5F;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description);
    }
    public static Entry strong_crushing_blows = add(strong_crushing_blows());
    private static Entry strong_crushing_blows() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/strong_crushing_blows");
        var title = "Crushing Blows";
        var description = "Increases the Critical Chance of Strong Attack by {critical_chance_bonus}";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:strong_attack";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.critical_chance_bonus = 0.25F;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description);
    }
    public static Entry rend_devastating_slash = add(rend_devastating_slash());
    private static Entry rend_devastating_slash() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/rend_devastating_slash");
        var title = "Devastating Slash";
        var description = "Highly increases Rend's critical chance by {critical_chance_bonus}";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:rend";
        modifier.power_modifier = new Spell.Impact.Modifier();
        modifier.power_modifier.critical_chance_bonus = 0.35F;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description);
    }
    public static Entry whirl_quick_hands = add(whirl_quick_hands());
    private static Entry whirl_quick_hands() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/whirl_quick_hands");
        var title = "Quick Hands";
        var description = "Whirl releases {channel_ticks_add} additional times during its full cast.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var modifier = new Spell.Modifier();
        modifier.spell_pattern = "witcher_rpg:whirl";
        modifier.channel_ticks_add = 3;
        spell.modifiers = List.of(modifier);

        return new Entry(id, spell, title, description);
    }
    public static Entry whirl_precise_slasher = add(whirl_precise_slasher());
    private static Entry whirl_precise_slasher() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/whirl_precise_slasher");
        var title = "Precise Slasher";
        var description = "Whirl's damage impacts have {trigger_chance} chance to deal an additional {max_health_damage} of the target's maximum health, or {max_health_damage_boss} against Bosses.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var normal = spell.impacts.get(0).action.damage;
            var boss = spell.impacts.get(1).action.damage;
            return args.description()
                    .replace("{max_health_damage}", SpellTooltip.percent(normal.spell_power_coefficient))
                    .replace("{max_health_damage_boss}", SpellTooltip.percent(boss.spell_power_coefficient));
        };

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.3F, WitcherSpellSchools.WITCHER_MELEE.id.toString());
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.id = "witcher_rpg:whirl";
        spell.passive.triggers = List.of(trigger);

        var bossDeny = new Spell.TargetCondition();
        bossDeny.entity_type = "#c:bosses";
        var notBoss = new Spell.Impact.TargetModifier();
        notBoss.conditions = List.of(bossDeny);
        notBoss.execute = TriState.DENY;

        var damageNormal = new Spell.Impact();
        damageNormal.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        damageNormal.attribute_from_target = true;
        damageNormal.action = new Spell.Impact.Action();
        damageNormal.action.type = Spell.Impact.Action.Type.DAMAGE;
        damageNormal.action.damage = new Spell.Impact.Action.Damage();
        damageNormal.action.damage.spell_power_coefficient = 0.03F;
        damageNormal.target_modifiers = List.of(notBoss);

        var bossAllow = new Spell.TargetCondition();
        bossAllow.entity_type = "#c:bosses";
        var onlyBoss = new Spell.Impact.TargetModifier();
        onlyBoss.conditions = List.of(bossAllow);
        onlyBoss.execute = TriState.ALLOW;

        var damageBoss = new Spell.Impact();
        damageBoss.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        damageBoss.attribute_from_target = true;
        damageBoss.action = new Spell.Impact.Action();
        damageBoss.action.type = Spell.Impact.Action.Type.DAMAGE;
        damageBoss.action.damage = new Spell.Impact.Action.Damage();
        damageBoss.action.damage.spell_power_coefficient = 0.01F;
        damageBoss.target_modifiers = List.of(onlyBoss);

        spell.impacts = List.of(damageNormal, damageBoss);
        SpellBuilder.Cost.cooldown(spell, 1F);

        return new Entry(id, spell, title, description, mutator, null);
    }
    public static Entry rend_heavy_swing = add(rend_heavy_swing());
    private static Entry rend_heavy_swing() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/rend_heavy_swing");
        var title = "Heavy Swing";
        var description = "Rend has {trigger_chance} chance to stun the hit target(s) for 3 seconds.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.25F, WitcherSpellSchools.WITCHER_MELEE.id.toString());
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.id = "witcher_rpg:rend";
        spell.passive.triggers = List.of(trigger);

        var stun = SpellBuilder.Impacts.effectSet(SpellEngineEffects.STUN.id.toString(), 3, 0);
        spell.impacts = List.of(stun);

        SpellBuilder.Cost.cooldown(spell, 8F);
        return new Entry(id, spell, title, description);
    }
    public static Entry witcher_senses_monster_expert = add(witcher_senses_monster_expert());
    private static Entry witcher_senses_monster_expert() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/witcher_senses_monster_expert");
        var title = "Monster Expert";
        var description = "Increases critical chance of Sign and Witcher Melee damage by {critical_chance_bonus}.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var schools = List.of(WitcherSpellSchools.AARD, WitcherSpellSchools.IGNI, WitcherSpellSchools.QUEN,
                WitcherSpellSchools.YRDEN, WitcherSpellSchools.AXII, WitcherSpellSchools.WITCHER_MELEE);
        var modifiers = new java.util.ArrayList<Spell.Modifier>();
        for (var school : schools) {
            var modifier = new Spell.Modifier();
            modifier.power_modifier = new Spell.Impact.Modifier();
            modifier.power_modifier.critical_chance_bonus = 0.05F;
            var impactfilter = new Spell.Modifier.ImpactFilter();
            impactfilter.type = Spell.Impact.Action.Type.DAMAGE;
            impactfilter.school = school;
            modifier.impact_filters = List.of(impactfilter);
            modifiers.add(modifier);
        }
        spell.modifiers = modifiers;

        return new Entry(id, spell, title, description);
    }
    public static Entry witcher_senses_enemy_knowledge = add(witcher_senses_enemy_knowledge());
    private static Entry witcher_senses_enemy_knowledge() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/witcher_senses_enemy_knowledge");
        var title = "Enemy Knowledge";
        var effect = WitcherStatusEffects.ENEMY_KNOWLEDGE;
        var description = "After being hit by an enemy affected by Exposed, reduce damage taken by {bonus} for {effect_duration} seconds.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        var exposedCondition = new Spell.TargetCondition();
        exposedCondition.entity_predicate_id = HAS_WITCHER_SENSES_EXPOSED.id().toString();
        trigger.target_conditions = List.of(exposedCondition);
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.CASTER;
        spell.impacts = List.of(createEffectImpact(Identifier.of(effect.id.toString()), 4F));

        SpellBuilder.Cost.cooldown(spell, 3F);
        return new Entry(id, spell, title, description, mutator, null);
    }
    public static Entry battle_trance_resolve = add(battle_trance_resolve());
    private static Entry battle_trance_resolve() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/battle_trance_resolve");
        var title = "Resolve";
        var effect = WitcherStatusEffects.RESOLVE;
        var description = "Casting Battle Trance grants Resolve, reducing incoming damage by {bonus} for {effect_duration} seconds.";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().firstModifier();
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            return args.description().replace("{bonus}", bonus);
        };
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;

        var trigger = SpellBuilder.Triggers.activeSpellCast();
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.id = "witcher_rpg:battle_trance";
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.CASTER;
        spell.impacts = List.of(createEffectImpact(Identifier.of(effect.id.toString()), 4F));

        return new Entry(id, spell, title, description, mutator, null);
    }
    public static Entry battle_trance_undying = add(battle_trance_undying());
    private static Entry battle_trance_undying() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/battle_trance_undying");
        var title = "Undying";
        var description = "While Battle Trance is active, if you fall below 20% health you heal for {heal_percent} of your max health.";
        var spell = createModifierAlikePassiveSpell();
        spell.school = WitcherSpellSchools.WITCHER_MELEE;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var heal = spell.impacts.get(0).action.heal;
            return args.description().replace("{heal_percent}", SpellTooltip.percent(heal.spell_power_coefficient));
        };

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        var lowHealth = new Spell.TargetCondition();
        lowHealth.health_percent_below = 0.2F;
        var battleTranceActive = new Spell.TargetCondition();
        battleTranceActive.entity_predicate_id = HAS_BATTLE_TRANCE.id().toString();
        trigger.target_conditions = List.of(lowHealth, battleTranceActive);
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.CASTER;

        var heal = new Spell.Impact();
        heal.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        heal.action = new Spell.Impact.Action();
        heal.action.type = Spell.Impact.Action.Type.HEAL;
        heal.action.heal = new Spell.Impact.Action.Heal();
        heal.action.heal.spell_power_coefficient = 0.5F;
        spell.impacts = List.of(heal);

        SpellBuilder.Cost.cooldown(spell, 20F);
        return new Entry(id, spell, title, description, mutator, null);
    }
    public static Entry igni_firestreams = add(igni_firestreams());
    private static Entry igni_firestreams() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/igni_firestreams");
        var title = "Firestreams";
        var description = "All Igni Signs reduce the movement speed of damaged targets.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.IGNI;

        var slow = createEffectImpact(Identifier.of(WitcherStatusEffects.SCORCHED.id.toString()), 3F);

        var modifierIgni = new Spell.Modifier();
        modifierIgni.spell_pattern = "witcher_rpg:igni";
        modifierIgni.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifierIgni.impacts = List.of(slow);

        var modifierIgniFirestream = new Spell.Modifier();
        modifierIgniFirestream.spell_pattern = "witcher_rpg:igni_firestream";
        modifierIgniFirestream.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifierIgniFirestream.impacts = List.of(slow);

        spell.modifiers = List.of(modifierIgni, modifierIgniFirestream);

        return new Entry(id, spell, title, description);
    }
    public static Entry aard_whirlwind = add(aard_whirlwind());
    private static Entry aard_whirlwind() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/aard_whirlwind");
        var title = "Aard Whirlwind";
        var description = "All Aard Signs have heavily increased knockback and critical chance.";
        var spell = modifierSpellBase();
        spell.school = WitcherSpellSchools.AARD;

        var knockbackImpact = new Spell.Impact();
        knockbackImpact.action = new Spell.Impact.Action();
        knockbackImpact.action.type = Spell.Impact.Action.Type.DAMAGE;
        knockbackImpact.action.damage = new Spell.Impact.Action.Damage();
        knockbackImpact.action.damage.spell_power_coefficient = 0F;
        knockbackImpact.action.damage.knockback = 2.5F;

        var modifierAard = new Spell.Modifier();
        modifierAard.spell_pattern = "witcher_rpg:aard";
        modifierAard.power_modifier = new Spell.Impact.Modifier();
        modifierAard.power_modifier.critical_chance_bonus = 0.3F;
        modifierAard.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifierAard.impacts = List.of(knockbackImpact);

        var modifierAardSweep = new Spell.Modifier();
        modifierAardSweep.spell_pattern = "witcher_rpg:aard_sweep";
        modifierAardSweep.power_modifier = new Spell.Impact.Modifier();
        modifierAardSweep.power_modifier.critical_chance_bonus = 0.3F;
        modifierAardSweep.mutate_impacts = Spell.Modifier.ImpactListModifier.APPEND;
        modifierAardSweep.impacts = List.of(knockbackImpact);

        spell.modifiers = List.of(modifierAard, modifierAardSweep);

        return new Entry(id, spell, title, description);
    }
    /// DEFENSE MODIFIERS
    public static Entry witcher_reflexes_boost_a = add(witcher_reflexes_boost_a());
    private static Entry witcher_reflexes_boost_a() {
        var id = Identifier.of(MOD_ID, "spell_modifiers/witcher_reflexes_boost_a");
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
        var id = Identifier.of(MOD_ID, "spell_modifiers/arrow_deflection");
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
}
