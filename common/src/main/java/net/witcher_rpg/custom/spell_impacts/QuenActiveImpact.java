package net.witcher_rpg.custom.spell_impacts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.witcher_rpg.util.SpellLookup;
import net.spell_engine.internals.SpellExecution;
import net.spell_engine.internals.impact.SpellImpacts;
import net.spell_power.api.SpellPower;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.effect.WitcherStatusEffects;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;


public class QuenActiveImpact implements SpellHandlers.CustomImpact {

    @Override
    public SpellHandlers.ImpactResult onSpellImpact(
            RegistryEntry<Spell> spell,
            SpellPower.Result powerResult,
            LivingEntity caster,
            Entity target,
            SpellExecution.ImpactContext context
    ) {
        if (!caster.getWorld().isClient) {
            if(!caster.hasStatusEffect(WitcherStatusEffects.QUEN_ACTIVE.effect)){
                RegistryEntry<Spell> helper_spell = SpellLookup.entry(caster.getWorld(), new Identifier(MOD_ID, "quen_active_helper"));
                SpellImpacts.performImpacts(caster.getWorld(), caster, target, target, helper_spell,
                        helper_spell.value().impacts, new SpellExecution.ImpactContext().power(SpellPower.getSpellPower(WitcherSpellSchools.QUEN, caster)).position(target.getPos()));
            }
        }
        return new SpellHandlers.ImpactResult(true, false);
    }
}

