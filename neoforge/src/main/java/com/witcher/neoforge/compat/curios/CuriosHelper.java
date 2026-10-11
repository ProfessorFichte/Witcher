package com.witcher.neoforge.compat.curios;

import net.witcher_rpg.item.WitcherTrinkets;

public class CuriosHelper {
    public static void register() {
        WitcherTrinkets.factory = args -> new WitcherCurioItem(args.settings(), args.attributes());
    }
}
