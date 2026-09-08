package net.witcher_rpg.item.armor;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.spell_engine.rpg_series.item.Armor;
import net.witcher_rpg.item.component.GlyphSlots;
import net.witcher_rpg.item.component.GlyphTooltipComponent;
import net.witcher_rpg.item.component.WitcherDataComponents;

import java.util.List;
import java.util.Optional;

public class CatSchoolArmor extends Armor.CustomItem {
    public CatSchoolArmor(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, addGlyphSlots(settings, material, slot));
    }

    private static Settings addGlyphSlots(Settings settings, ArmorMaterial material, Type slot) {
        if (slot == Type.CHESTPLATE) {
            String materialName = materialName(material);
            int glyphSlots = determineGlyphSlots(materialName);
            WitcherDataComponents.defaults(settings).glyphSlots( new GlyphSlots(glyphSlots, List.of()));
        }
        return settings;
    }

    /// 1.20.1 has no armor-material registry: Spell Engine's `Armor.CustomMaterial` carries its own id.
    private static String materialName(ArmorMaterial material) {
        return material instanceof Armor.CustomMaterial custom ? custom.id.getPath() : material.getName();
    }

    private static int determineGlyphSlots(String materialName) {
        if (materialName.contains("grandmaster")) return Armors.TIER5_GLYPH_SLOTS;
        if (materialName.contains("mastercrafted")) return Armors.TIER4_GLYPH_SLOTS;
        if (materialName.contains("superior")) return Armors.TIER3_GLYPH_SLOTS;
        if (materialName.contains("enhanced")) return Armors.TIER2_GLYPH_SLOTS;
        return Armors.TIER1_GLYPH_SLOTS;
    }

    @Override
    public void appendTooltip(ItemStack stack,  World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (this.getType() == Type.CHESTPLATE) {
            GlyphSlots slots = WitcherDataComponents.getGlyphSlots(stack);
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
            net.minecraft.util.Identifier glyphId = Registries.ITEM.getId(glyph.getItem());

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
                if (attrModifier.attribute == null) continue; // Skip null attribute IDs
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
                var attribute = Registries.ATTRIBUTE.get(new net.minecraft.util.Identifier(attrId));
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
    public Optional<net.minecraft.client.item.TooltipData> getTooltipData(ItemStack stack) {
        if (this.getType() == Type.CHESTPLATE) {
            GlyphSlots slots = WitcherDataComponents.getGlyphSlots(stack);
            if (slots != null && slots.maxSlots() > 0) {
                return Optional.of(new GlyphTooltipComponent(slots));
            }
        }
        return super.getTooltipData(stack);
    }
}

