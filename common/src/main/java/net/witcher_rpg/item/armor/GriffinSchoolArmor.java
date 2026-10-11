package net.witcher_rpg.item.armor;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.rpg_series.item.Armor;
import net.witcher_rpg.compat.jewelry.JewelrySocketFeature;
import net.witcher_rpg.compat.jewelry.JewelrySockets;

public class GriffinSchoolArmor extends Armor.CustomItem {
    public GriffinSchoolArmor(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        super(material, slot, addGlyphSockets(settings, material, slot));
    }

    private static Settings addGlyphSockets(Settings settings, RegistryEntry<ArmorMaterial> material, Type slot) {
        if (JewelrySocketFeature.glyphsActive()) {
            String materialName = Registries.ARMOR_MATERIAL.getId(material.value()).getPath();
            int glyphSlots = Armors.glyphSlots(materialName, slot);
            if (glyphSlots > 0) {
                JewelrySockets.applyGlyphSockets(settings, glyphSlots);
            }
        }
        return settings;
    }
}
