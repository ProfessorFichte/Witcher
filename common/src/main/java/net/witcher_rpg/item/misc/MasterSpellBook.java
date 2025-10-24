package net.witcher_rpg.item.misc;

import net.minecraft.util.Identifier;
import net.spell_engine.api.item.trinket.SpellBookItem;

public class MasterSpellBook extends SpellBookItem {
    public MasterSpellBook(Identifier poolId, Settings settings) {
        super(poolId, settings.fireproof());
    }
}
