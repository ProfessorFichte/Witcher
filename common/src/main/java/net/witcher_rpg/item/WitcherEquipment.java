package net.witcher_rpg.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.witcher_rpg.item.armor.Armors;
import net.witcher_rpg.item.weapon.WeaponsRegister;


import java.util.HashMap;

public class WitcherEquipment {

    public static final HashMap<String, Item> entries;
    static {
        entries = new HashMap<>();
        for(var weaponEntry: WeaponsRegister.entries) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        for(var entry: Armors.entries) {
            var set = entry.armorSet();
            for (var piece: set.pieces()) {
                var armorItem = (ArmorItem) piece;
                entries.put(set.idOf(armorItem).toString(), armorItem);
            }
        }
    }
}
