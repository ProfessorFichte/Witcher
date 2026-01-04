package net.witcher_rpg.custom;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.weakness.ScopedWeakness;
import net.spell_engine.api.util.TriState;
import net.spell_power.api.SpellSchool;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.util.tags.WitcherEntityTags;
import net.witcher_rpg.config.WeaknessConfig;

import java.util.List;

public class WitcherSchoolWeakness {
    public static List<ScopedWeakness> getWeaknesses(Identifier schoolId) {
        if (schoolId == null) {
            return List.of();
        }
        var config = WitcherClassMod.weaknessConfig.value;
        if (config == null || config.school_weaknesses == null) {
            return List.of();
        }
        var key = schoolId.toString();
        return config.school_weaknesses.getOrDefault(key, List.of());
    }

    public static List<ScopedWeakness> getWeaknesses(@Nullable SpellSchool school) {
        if (school == null) {
            return List.of();
        }
        return getWeaknesses(school.id);
    }

    public static WeaknessConfig createDefault() {
        var config = new WeaknessConfig();

        var aardWeakness = new Spell.Impact.TargetModifier();
        var aardCondition = new Spell.TargetCondition();
        aardCondition.entity_type = "#" + WitcherEntityTags.AARD_VULNERABLE.id();
        aardWeakness.conditions = List.of(aardCondition);
        aardWeakness.modifier = new Spell.Impact.Modifier();
        aardWeakness.modifier.power_multiplier = 0.3f;
        config.school_weaknesses.put(WitcherSpellSchools.AARD.id.toString(), List.of(
                new ScopedWeakness(Spell.Impact.Action.Type.DAMAGE, aardWeakness)
        ));

        var axiiWeakness = new Spell.Impact.TargetModifier();
        var axiiCondition = new Spell.TargetCondition();
        axiiCondition.entity_type = "#" + WitcherEntityTags.AXII_VULNERABLE.id();
        axiiWeakness.conditions = List.of(axiiCondition);
        axiiWeakness.modifier = new Spell.Impact.Modifier();
        axiiWeakness.modifier.power_multiplier = 0.3f;
        config.school_weaknesses.put(WitcherSpellSchools.AXII.id.toString(), List.of(
                new ScopedWeakness(Spell.Impact.Action.Type.DAMAGE, axiiWeakness)
        ));

        var igniWeakness = new Spell.Impact.TargetModifier();
        var igniCondition = new Spell.TargetCondition();
        igniCondition.entity_type = "#" + WitcherEntityTags.IGNI_VULNERABLE.id();
        igniWeakness.conditions = List.of(igniCondition);
        igniWeakness.modifier = new Spell.Impact.Modifier();
        igniWeakness.modifier.power_multiplier = 0.3f;
        config.school_weaknesses.put(WitcherSpellSchools.IGNI.id.toString(), List.of(
                new ScopedWeakness(Spell.Impact.Action.Type.DAMAGE, igniWeakness)
        ));

        var quenWeakness = new Spell.Impact.TargetModifier();
        var quenCondition = new Spell.TargetCondition();
        quenCondition.entity_type = "#" + WitcherEntityTags.QUEN_VULNERABLE.id();
        quenWeakness.conditions = List.of(quenCondition);
        quenWeakness.modifier = new Spell.Impact.Modifier();
        quenWeakness.modifier.power_multiplier = 0.3f;
        config.school_weaknesses.put(WitcherSpellSchools.QUEN.id.toString(), List.of(
                new ScopedWeakness(Spell.Impact.Action.Type.DAMAGE, quenWeakness)
        ));

        var yrdenDamageAllow = new Spell.Impact.TargetModifier();
        var yrdenCondition = new Spell.TargetCondition();
        yrdenCondition.entity_type = "#" + WitcherEntityTags.YRDEN_VULNERABLE.id();
        yrdenDamageAllow.conditions = List.of(yrdenCondition);
        igniWeakness.modifier = new Spell.Impact.Modifier();
        igniWeakness.modifier.critical_chance_bonus = 1.0f;
        config.school_weaknesses.put(WitcherSpellSchools.YRDEN.id.toString(), List.of(
                new ScopedWeakness(Spell.Impact.Action.Type.DAMAGE, yrdenDamageAllow)
        ));

        return config;
    }

}
