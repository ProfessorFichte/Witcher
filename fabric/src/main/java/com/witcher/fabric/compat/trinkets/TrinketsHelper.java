package com.witcher.fabric.compat.trinkets;


import net.witcher_rpg.item.WitcherTrinkets;

public class TrinketsHelper {
    public static void register() {
        WitcherTrinkets.factory = args -> new WitcherTrinketItem(args.settings(), args.attributes());
    }
}
