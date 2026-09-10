package net.witcher_rpg.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.internals.SpellExecution;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_power.api.SpellSchool;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.effect.WitcherStatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

import static net.witcher_rpg.WitcherClassMod.*;

@Mixin(SpellExecution.class)
public abstract class SpellExecutionMixin {

    @Inject(at = @At("HEAD"), method = "performSpell", cancellable = true)
    private static void witcherQuenActiveShield(World world, PlayerEntity player, RegistryEntry<Spell> spellEntry, SpellTarget.SearchResult targetResult, SpellCast.Action action, float progress, CallbackInfo callbackInfo) {
        if (!player.isSpectator()&& player instanceof SpellCasterEntity spellCasterEntity) {
            var spell = spellCasterEntity.getCurrentSpell();
            var spellEntryQuen = SpellRegistry.from(player.getWorld()).getEntry(Identifier.of(MOD_ID, "quen_active_shield")).orElse(null);
            if (spell != null) {
                if(action == SpellCast.Action.RELEASE && Objects.equals(spell, spellEntryQuen.value())){
                    player.removeStatusEffect(WitcherStatusEffects.QUEN_ACTIVE.entry);
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "performSpell", cancellable = true)
    private static void damagingSignAdrenalineGain(World world, PlayerEntity player, RegistryEntry<Spell> spellEntry, SpellTarget.SearchResult targetResult, SpellCast.Action action, float progress, CallbackInfo callbackInfo) {
        if (!player.isSpectator()) {
            var spell = spellEntry.value();
            SpellSchool school = spell.school;
            List<Entity> entities = targetResult.entities();

            if (action == SpellCast.Action.RELEASE && entities != null && !entities.isEmpty()
                    && (school == WitcherSpellSchools.IGNI || school == WitcherSpellSchools.AARD)) {
                WitcherStatusEffects.tryGainAdrenaline(player);
            }
        }
    }
}
