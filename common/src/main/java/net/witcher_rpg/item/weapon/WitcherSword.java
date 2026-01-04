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

public class WitcherSword extends SpellSwordItem {
    private final int runestoneSlots;

    public WitcherSword(ToolMaterial material, Settings settings) {
        this(material, settings, 1);
    }

    public WitcherSword(ToolMaterial material, Settings settings, int runestoneSlots) {
        super(material, settings.component(WitcherDataComponents.RUNESTONE_SLOTS,
            new RunestoneSlots(runestoneSlots, List.of())));
        this.runestoneSlots = runestoneSlots;
    }

    // Factory methods for Weapon.Entry
    public static net.spell_engine.api.item.weapon.Weapon.Factory with1Slot() {
        return (material, settings) -> new WitcherSword(material, settings, 1);
    }

    public static net.spell_engine.api.item.weapon.Weapon.Factory with2Slots() {
        return (material, settings) -> new WitcherSword(material, settings, 2);
    }

    public static net.spell_engine.api.item.weapon.Weapon.Factory with3Slots() {
        return (material, settings) -> new WitcherSword(material, settings, 3);
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
