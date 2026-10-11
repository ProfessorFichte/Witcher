package net.witcher_rpg.item.weapon;

import net.minecraft.item.ToolMaterial;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.witcher_rpg.compat.jewelry.JewelrySocketFeature;
import net.witcher_rpg.compat.jewelry.JewelrySockets;

public class WitcherRelicSword extends SpellSwordItem {
    public WitcherRelicSword(ToolMaterial material, Settings settings) {
        super(material, addRunestoneSockets(settings));
    }

    private static Settings addRunestoneSockets(Settings settings) {
        if (JewelrySocketFeature.runestonesActive()) {
            JewelrySockets.applyRunestoneSockets(settings, 3);
        }
        return settings;
    }
}
