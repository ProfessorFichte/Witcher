package net.witcher_rpg.item.weapon;

import net.minecraft.item.ToolMaterial;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.spell_engine.rpg_series.item.Weapon;
import net.witcher_rpg.compat.jewelry.JewelrySocketFeature;
import net.witcher_rpg.compat.jewelry.JewelrySockets;

public class WitcherSword extends SpellSwordItem {
    public WitcherSword(ToolMaterial material, Settings settings) {
        this(material, settings, 1);
    }

    public WitcherSword(ToolMaterial material, Settings settings, int runestoneSlots) {
        super(material, addRunestoneSockets(settings, runestoneSlots));
    }

    private static Settings addRunestoneSockets(Settings settings, int runestoneSlots) {
        if (JewelrySocketFeature.runestonesActive()) {
            JewelrySockets.applyRunestoneSockets(settings, runestoneSlots);
        }
        return settings;
    }

    public static Weapon.Factory with1Slot() {
        return (material, settings) -> new WitcherSword(material, settings, 1);
    }

    public static Weapon.Factory with2Slots() {
        return (material, settings) -> new WitcherSword(material, settings, 2);
    }

    public static Weapon.Factory with3Slots() {
        return (material, settings) -> new WitcherSword(material, settings, 3);
    }
}
