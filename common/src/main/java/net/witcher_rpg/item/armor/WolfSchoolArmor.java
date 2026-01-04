package net.witcher_rpg.item.armor;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.spell_engine.api.item.armor.Armor;
import net.witcher_rpg.item.component.GlyphSlots;
import net.witcher_rpg.item.component.GlyphTooltipComponent;
import net.witcher_rpg.item.component.WitcherDataComponents;

import java.util.List;
import java.util.Optional;

public class WolfSchoolArmor extends Armor.CustomItem {
    public WolfSchoolArmor(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        super(material, slot, addGlyphSlots(settings, material, slot));
    }

    private static Settings addGlyphSlots(Settings settings, RegistryEntry<ArmorMaterial> material, Type slot) {
        if (slot == Type.CHESTPLATE) {
            String materialName = Registries.ARMOR_MATERIAL.getId(material.value()).getPath();
            int glyphSlots = determineGlyphSlots(materialName);
            settings.component(WitcherDataComponents.GLYPH_SLOTS, new GlyphSlots(glyphSlots, List.of()));
        }
        return settings;
    }

    private static int determineGlyphSlots(String materialName) {
        if (materialName.contains("grandmaster")) return Armors.TIER5_GLYPH_SLOTS;
        if (materialName.contains("mastercrafted")) return Armors.TIER4_GLYPH_SLOTS;
        if (materialName.contains("superior")) return Armors.TIER3_GLYPH_SLOTS;
        if (materialName.contains("enhanced")) return Armors.TIER2_GLYPH_SLOTS;
        return Armors.TIER1_GLYPH_SLOTS;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if (this.getType() == Type.CHESTPLATE) {
            GlyphSlots slots = stack.get(WitcherDataComponents.GLYPH_SLOTS);
            if (slots != null && slots.maxSlots() > 0) {
                addGlyphAttributesTooltip(slots, tooltip);
                tooltip.add(Text.empty());
            }
        }
    }

    private void addGlyphAttributesTooltip(GlyphSlots slots, List<Text> tooltip) {
        if (slots.attachedGlyphs().isEmpty()) return;

        java.util.Map<String, Double> consolidatedAttributes = new java.util.HashMap<>();

        for (ItemStack glyph : slots.attachedGlyphs()) {
            net.minecraft.util.Identifier glyphId = Registries.ITEM.getId(glyph.getItem());

            net.witcher_rpg.item.WitcherTrinkets.Entry glyphEntry = null;
            for (var entry : net.witcher_rpg.item.WitcherTrinkets.entries) {
                if (entry.id().equals(glyphId)) {
                    glyphEntry = entry;
                    break;
                }
            }

            if (glyphEntry == null) continue;

            var config = glyphEntry.config();
            if (config == null || config.attributes == null) continue;

            for (var attrModifier : config.attributes) {
                if (attrModifier.attribute == null) continue;
                consolidatedAttributes.merge(attrModifier.attribute, (double) attrModifier.value, Double::sum);
            }
        }

        if (!consolidatedAttributes.isEmpty()) {
            tooltip.add(Text.literal(" ").append(Text.translatable("item.modifiers.chest").formatted(net.minecraft.util.Formatting.GRAY)));

            for (var entry : consolidatedAttributes.entrySet()) {
                String attrId = entry.getKey();
                double value = entry.getValue();

                var attribute = Registries.ATTRIBUTE.get(net.minecraft.util.Identifier.of(attrId));
                if (attribute == null) continue;

                String sign = value > 0 ? "+" : "";
                Text attrText = Text.literal(" " + sign + String.format(java.util.Locale.US, "%.1f", value) + " ")
                    .append(Text.translatable(attribute.getTranslationKey()))
                    .formatted(net.minecraft.util.Formatting.BLUE);
                tooltip.add(attrText);
            }
        }
    }

    @Override
    public Optional<net.minecraft.item.tooltip.TooltipData> getTooltipData(ItemStack stack) {
        if (this.getType() == Type.CHESTPLATE) {
            GlyphSlots slots = stack.get(WitcherDataComponents.GLYPH_SLOTS);
            if (slots != null && slots.maxSlots() > 0) {
                return Optional.of(new GlyphTooltipComponent(slots));
            }
        }
        return super.getTooltipData(stack);
    }
}
