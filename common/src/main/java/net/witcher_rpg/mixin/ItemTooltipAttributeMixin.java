package net.witcher_rpg.mixin;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.witcher_rpg.item.WitcherTrinkets;
import net.witcher_rpg.item.armor.WitcherArmor;
import net.witcher_rpg.item.component.GlyphSlots;
import net.witcher_rpg.item.component.RunestoneSlots;
import net.witcher_rpg.item.component.WitcherDataComponents;
import net.witcher_rpg.item.weapon.WitcherSword;
import net.witcher_rpg.item.weapon.WitcherRelicSword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Mixin(ItemStack.class)
public abstract class ItemTooltipAttributeMixin {

    @Inject(method = "getTooltip", at = @At("RETURN"))
    private void addAttributeTooltipForExternalItems(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        Item item = stack.getItem();

        // Skip if this is already a WitcherSword, WitcherRelicSword, or WitcherArmor (they handle tooltips themselves)
        if (item instanceof WitcherSword || item instanceof WitcherRelicSword || item instanceof WitcherArmor) {
            return;
        }

        List<Text> tooltip = cir.getReturnValue();

        // Check for runestones (weapons)
        RunestoneSlots runestoneSlots = stack.get(WitcherDataComponents.RUNESTONE_SLOTS);
        if (runestoneSlots != null && runestoneSlots.maxSlots() > 0 && !runestoneSlots.attachedRunestones().isEmpty()) {
            addRunestoneAttributesTooltip(runestoneSlots, tooltip);
        }

        // Check for glyphs (armor)
        GlyphSlots glyphSlots = stack.get(WitcherDataComponents.GLYPH_SLOTS);
        if (glyphSlots != null && glyphSlots.maxSlots() > 0 && !glyphSlots.attachedGlyphs().isEmpty()) {
            addGlyphAttributesTooltip(glyphSlots, tooltip);
        }
    }

    private void addRunestoneAttributesTooltip(RunestoneSlots slots, List<Text> tooltip) {
        if (slots.attachedRunestones().isEmpty()) return;

        Map<String, Double> flatAttributes = new HashMap<>();
        Map<String, Double> percentageAttributes = new HashMap<>();

        for (ItemStack runestone : slots.attachedRunestones()) {
            Identifier runestoneId = Registries.ITEM.getId(runestone.getItem());

            WitcherTrinkets.Entry runestoneEntry = null;
            for (var entry : WitcherTrinkets.entries) {
                if (entry.id().equals(runestoneId)) {
                    runestoneEntry = entry;
                    break;
                }
            }

            if (runestoneEntry == null) continue;

            var config = runestoneEntry.config();
            if (config == null || config.attributes == null) continue;

            for (var attrModifier : config.attributes) {
                if (attrModifier.attribute == null) continue;
                if (attrModifier.operation == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE) {
                    percentageAttributes.merge(attrModifier.attribute, (double) attrModifier.value, Double::sum);
                } else {
                    flatAttributes.merge(attrModifier.attribute, (double) attrModifier.value, Double::sum);
                }
            }
        }

        if (!flatAttributes.isEmpty() || !percentageAttributes.isEmpty()) {
            int insertPosition = findVanillaAttributeEndPosition(tooltip);

            if (insertPosition == -1) {
                insertPosition = findInsertPositionAfterName(tooltip);
                tooltip.add(insertPosition++, Text.empty());
                tooltip.add(insertPosition++, Text.literal(" ").append(Text.translatable("item.modifiers.mainhand").formatted(Formatting.GRAY)));
            }

            for (var entry : flatAttributes.entrySet()) {
                String attrId = entry.getKey();
                double value = entry.getValue();

                var attribute = Registries.ATTRIBUTE.get(Identifier.of(attrId));
                if (attribute == null) continue;

                String sign = value > 0 ? "+" : "";
                Text attrText = Text.literal(" " + sign + String.format(Locale.US, "%.1f", value) + " ")
                    .append(Text.translatable(attribute.getTranslationKey()))
                    .formatted(Formatting.BLUE);
                tooltip.add(insertPosition++, attrText);
            }

            for (var entry : percentageAttributes.entrySet()) {
                String attrId = entry.getKey();
                double value = entry.getValue() * 100;

                var attribute = Registries.ATTRIBUTE.get(Identifier.of(attrId));
                if (attribute == null) continue;

                String sign = value > 0 ? "+" : "";
                Text attrText = Text.literal(" " + sign + String.format(Locale.US, "%.0f", value) + "% ")
                    .append(Text.translatable(attribute.getTranslationKey()))
                    .formatted(Formatting.BLUE);
                tooltip.add(insertPosition++, attrText);
            }
        }
    }

    private void addGlyphAttributesTooltip(GlyphSlots slots, List<Text> tooltip) {
        if (slots.attachedGlyphs().isEmpty()) return;

        Map<String, Double> consolidatedAttributes = new HashMap<>();

        for (ItemStack glyph : slots.attachedGlyphs()) {
            Identifier glyphId = Registries.ITEM.getId(glyph.getItem());

            WitcherTrinkets.Entry glyphEntry = null;
            for (var entry : WitcherTrinkets.entries) {
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
            int insertPosition = findVanillaAttributeEndPosition(tooltip);

            if (insertPosition == -1) {
                insertPosition = findInsertPositionAfterName(tooltip);
                tooltip.add(insertPosition++, Text.empty());
                tooltip.add(insertPosition++, Text.literal(" ").append(Text.translatable("item.modifiers.chest").formatted(Formatting.GRAY)));
            }

            for (var entry : consolidatedAttributes.entrySet()) {
                String attrId = entry.getKey();
                double value = entry.getValue();

                var attribute = Registries.ATTRIBUTE.get(Identifier.of(attrId));
                if (attribute == null) continue;

                String sign = value > 0 ? "+" : "";
                Text attrText = Text.literal(" " + sign + String.format(Locale.US, "%.1f", value) + " ")
                    .append(Text.translatable(attribute.getTranslationKey()))
                    .formatted(Formatting.BLUE);
                tooltip.add(insertPosition++, attrText);
            }
        }
    }

    private int findVanillaAttributeEndPosition(List<Text> tooltip) {
        int modifierSectionIndex = -1;
        int lastAttributeIndex = -1;

        for (int i = 0; i < tooltip.size(); i++) {
            String text = tooltip.get(i).getString();

            if (text.contains("When in Main Hand") || text.contains("When on Body") ||
                text.contains("When in Off Hand") || text.contains("item.modifiers")) {
                modifierSectionIndex = i;

                for (int j = i + 1; j < tooltip.size(); j++) {
                    String attrText = tooltip.get(j).getString();

                    if (attrText.startsWith(" +") || attrText.startsWith(" -")) {
                        lastAttributeIndex = j;
                    } else if (!attrText.trim().isEmpty() && lastAttributeIndex > i) {
                        return lastAttributeIndex + 1;
                    }
                }

                if (lastAttributeIndex > i) {
                    return lastAttributeIndex + 1;
                }
            }
        }

        return -1;
    }

    private int findInsertPositionAfterName(List<Text> tooltip) {
        for (int i = 0; i < tooltip.size(); i++) {
            if (tooltip.get(i).getString().trim().isEmpty()) {
                return i + 1;
            }
        }

        return Math.min(1, tooltip.size());
    }
}
