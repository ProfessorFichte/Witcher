package net.witcher_rpg.custom.spell_impacts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.internals.SpellHelper;
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
            SpellHelper.ImpactContext context
    ) {
        if (!caster.getWorld().isClient) {
            if(!caster.hasStatusEffect(WitcherStatusEffects.QUEN_ACTIVE.entry)){
                RegistryEntry<Spell> helper_spell = SpellRegistry.from(caster.getWorld()).getEntry(Identifier.of(MOD_ID, "quen_active_helper")).get();
                SpellHelper.performImpacts(caster.getWorld(), caster, target, target, helper_spell,
                        helper_spell.value().impacts, new SpellHelper.ImpactContext().power(SpellPower.getSpellPower(WitcherSpellSchools.QUEN, caster)).position(target.getPos()));
            }
        }
        return new SpellHandlers.ImpactResult(true, false);
    }
}

