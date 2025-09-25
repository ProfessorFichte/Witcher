package net.witcher_rpg.compat;


import net.witcher_rpg.item.WitcherTrinkets;

public class TrinketsHelper {
    public static void register() {
        WitcherTrinkets.factory = args -> new WitcherTrinketItem(args.settings(), args.attributes());
    }
}
