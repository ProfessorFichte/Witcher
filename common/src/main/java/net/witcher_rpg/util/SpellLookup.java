package net.witcher_rpg.util;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import org.jetbrains.annotations.Nullable;

/// 1.20.1 has no `Registry#getEntry(Identifier)` — an entry is looked up by `RegistryKey`.
public class SpellLookup {
    @Nullable
    @SuppressWarnings("unchecked")
    public static RegistryEntry<Spell> entry(World world, @Nullable Identifier spellId) {
        if (spellId == null) {
            return null;
        }
        return SpellRegistry.from(world)
                .getEntry(RegistryKey.of(SpellRegistry.KEY, spellId))
                .map(entry -> (RegistryEntry<Spell>) entry)
                .orElse(null);
    }
}
