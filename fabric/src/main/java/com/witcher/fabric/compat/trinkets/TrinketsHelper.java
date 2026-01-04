package com.witcher.fabric.compat.trinkets;


import net.witcher_rpg.item.GlyphItem;
import net.witcher_rpg.item.RunestoneItem;
import net.witcher_rpg.item.WitcherTrinkets;

public class TrinketsHelper {
    public static void register() {
        WitcherTrinkets.factory = args -> {
            // Use custom item classes for glyphs and runestones
            if (args.name().contains("glyph")) {
                return new WitcherTrinketGlyphItem(args.settings(), args.attributes());
            } else if (args.name().contains("runestone")) {
                return new WitcherTrinketRunestoneItem(args.settings(), args.attributes());
            }
            return new WitcherTrinketItem(args.settings(), args.attributes());
        };
    }
}
