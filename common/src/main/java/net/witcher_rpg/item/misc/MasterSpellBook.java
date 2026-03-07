package net.witcher_rpg.item.misc;

import net.minecraft.item.Item;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;

import java.util.List;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class MasterSpellBook extends Item {
    private static final SpellContainer CONTAINER = new SpellContainer(
            SpellContainer.ContentType.ANY, "", MOD_ID + ":master_witcher", "", 5, List.of(), 2);

    public MasterSpellBook(Settings settings) {
        super(settings.component(SpellDataComponents.SPELL_CONTAINER, CONTAINER));
    }
}
