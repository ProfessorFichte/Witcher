package net.witcher_rpg.item.armor;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.api.item.armor.Armor;

public class WolfSchoolArmor extends Armor.CustomItem {
    public WolfSchoolArmor (RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        super(material, slot, settings);
    }
}

