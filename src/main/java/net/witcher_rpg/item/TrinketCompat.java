package net.witcher_rpg.item;

import net.fabricmc.loader.api.FabricLoader;

public class TrinketCompat {
    public static void register() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            WitcherTrinkets.factory = args -> new WitcherTrinketItem(args.settings(), args.attributes());
        }
    }
}
