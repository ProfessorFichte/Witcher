package net.witcher_rpg.item.armor;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.api.item.armor.Armor;

public class WitcherArmor extends Armor.CustomItem {
    public WitcherArmor(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        super(material, slot, settings);
    }
}

