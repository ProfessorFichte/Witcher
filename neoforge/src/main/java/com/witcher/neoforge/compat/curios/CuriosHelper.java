package com.witcher.neoforge.compat.curios;

import net.witcher_rpg.item.WitcherTrinkets;

public class CuriosHelper {
    public static void register() {
        WitcherTrinkets.factory = args -> {
            if (args.name().contains("glyph")) {
                return new WitcherCurioGlyphItem(args.settings(), args.attributes());
            } else if (args.name().contains("runestone")) {
                return new WitcherCurioRunestoneItem(args.settings(), args.attributes());
            }
            return new WitcherCurioItem(args.settings(), args.attributes());
        };
    }
}
