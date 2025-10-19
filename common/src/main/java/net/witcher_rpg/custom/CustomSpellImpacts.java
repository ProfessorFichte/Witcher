package net.witcher_rpg.custom;

import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.witcher_rpg.custom.spell_impacts.QuenActiveImpact;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class CustomSpellImpacts {

    public static void registerCustomImpacts(){
        SpellHandlers.registerCustomImpact(
                Identifier.of(MOD_ID, "quen_active"),
                new QuenActiveImpact()
        );
    }
}
