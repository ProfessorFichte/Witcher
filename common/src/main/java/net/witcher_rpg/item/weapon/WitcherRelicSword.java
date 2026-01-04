package net.witcher_rpg.item.weapon;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.spell_engine.api.item.weapon.SpellSwordItem;
import net.witcher_rpg.item.WitcherTrinkets;
import net.witcher_rpg.item.component.RunestoneSlots;
import net.witcher_rpg.item.component.RunestoneTooltipComponent;
import net.witcher_rpg.item.component.WitcherDataComponents;

import java.util.List;
import java.util.Optional;

public class WitcherRelicSword extends SpellSwordItem {
    public WitcherRelicSword(ToolMaterial material, Settings settings) {
        super(material, addRunestoneSlots(settings));
    }

    private static Settings addRunestoneSlots(Settings settings) {
        settings.component(WitcherDataComponents.RUNESTONE_SLOTS, new RunestoneSlots(3, List.of()));
        return settings;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        RunestoneSlots slots = stack.get(WitcherDataComponents.RUNESTONE_SLOTS);
        if (slots != null && slots.maxSlots() > 0) {
            addRunestoneAttributesTooltip(slots, tooltip);
            tooltip.add(Text.empty());
        }
    }

    private void addRunestoneAttributesTooltip(RunestoneSlots slots, List<Text> tooltip) {
        if (slots.attachedRunestones().isEmpty()) return;

        java.util.Map<String, Double> flatAttributes = new java.util.HashMap<>();
        java.util.Map<String, Double> percentageAttributes = new java.util.HashMap<>();

        for (ItemStack runestone : slots.attachedRunestones()) {
            net.minecraft.util.Identifier runestoneId = Registries.ITEM.getId(runestone.getItem());

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
                if (attrModifier.operation == net.minecraft.entity.attribute.EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE) {
                    percentageAttributes.merge(attrModifier.attribute, (double) attrModifier.value, Double::sum);
                } else {
                    flatAttributes.merge(attrModifier.attribute, (double) attrModifier.value, Double::sum);
                }
            }
        }

        if (!flatAttributes.isEmpty() || !percentageAttributes.isEmpty()) {
            tooltip.add(Text.literal(" ").append(Text.translatable("item.modifiers.mainhand").formatted(net.minecraft.util.Formatting.GRAY)));

            for (var entry : flatAttributes.entrySet()) {
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

            for (var entry : percentageAttributes.entrySet()) {
                String attrId = entry.getKey();
                double value = entry.getValue() * 100;

                var attribute = Registries.ATTRIBUTE.get(net.minecraft.util.Identifier.of(attrId));
                if (attribute == null) continue;

                String sign = value > 0 ? "+" : "";
                Text attrText = Text.literal(" " + sign + String.format(java.util.Locale.US, "%.0f", value) + "% ")
                    .append(Text.translatable(attribute.getTranslationKey()))
                    .formatted(net.minecraft.util.Formatting.BLUE);
                tooltip.add(attrText);
            }
        }
    }

    @Override
    public Optional<net.minecraft.item.tooltip.TooltipData> getTooltipData(ItemStack stack) {
        RunestoneSlots slots = stack.get(WitcherDataComponents.RUNESTONE_SLOTS);
        if (slots != null && slots.maxSlots() > 0) {
            return Optional.of(new RunestoneTooltipComponent(slots));
        }
        return super.getTooltipData(stack);
    }
}
