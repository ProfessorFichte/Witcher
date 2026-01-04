package net.witcher_rpg.item.armor;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.armor.Armor;
import net.witcher_rpg.item.component.GlyphSlots;
import net.witcher_rpg.item.component.GlyphTooltipComponent;
import net.witcher_rpg.item.component.WitcherDataComponents;

import java.util.List;
import java.util.Optional;

public class WitcherArmor extends Armor.CustomItem {
    public WitcherArmor(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        super(material, slot, addGlyphSlots(settings, slot, Armors.TIER1_GLYPH_SLOTS));
    }

    private static Settings addGlyphSlots(Settings settings, Type slot, int glyphSlots) {
        if (slot == Type.CHESTPLATE) {
            settings.component(WitcherDataComponents.GLYPH_SLOTS, new GlyphSlots(glyphSlots, List.of()));
        }
        return settings;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        if (this.getType() == Type.CHESTPLATE) {
            GlyphSlots slots = stack.get(WitcherDataComponents.GLYPH_SLOTS);
            if (slots != null && slots.maxSlots() > 0) {
                // Add glyph attribute bonuses to tooltip
                addGlyphAttributesTooltip(slots, tooltip);
                tooltip.add(Text.empty());
            }
        }
    }

    private void addGlyphAttributesTooltip(GlyphSlots slots, List<Text> tooltip) {
        if (slots.attachedGlyphs().isEmpty()) return;

        // Consolidate attributes from all glyphs
        java.util.Map<String, Double> consolidatedAttributes = new java.util.HashMap<>();

        for (ItemStack glyph : slots.attachedGlyphs()) {
            Identifier glyphId = Registries.ITEM.getId(glyph.getItem());

            // Find the glyph entry in WitcherTrinkets
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

            // Consolidate attributes
            for (var attrModifier : config.attributes) {
                if (attrModifier.attribute == null) continue;
                consolidatedAttributes.merge(attrModifier.attribute, (double) attrModifier.value, Double::sum);
            }
        }

        // Add consolidated attributes to tooltip
        if (!consolidatedAttributes.isEmpty()) {
            tooltip.add(Text.literal(" ").append(Text.translatable("item.modifiers.chest").formatted(net.minecraft.util.Formatting.GRAY)));

            for (var entry : consolidatedAttributes.entrySet()) {
                String attrId = entry.getKey();
                double value = entry.getValue();

                // Get attribute name
                var attribute = Registries.ATTRIBUTE.get(Identifier.of(attrId));
                if (attribute == null) continue;

                // Format: " +2.0 Quen Intensity"
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

