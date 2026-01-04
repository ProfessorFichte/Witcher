package net.witcher_rpg.anvil;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.witcher_rpg.item.component.RunestoneSlots;
import net.witcher_rpg.item.component.WitcherDataComponents;
import net.witcher_rpg.util.tags.WitcherItemTags;

import java.util.Random;

public class RunestoneAnvilHandler {
    private static final Random RANDOM = new Random();

    public static GlyphAnvilHandler.AnvilResult handleAnvilUpdate(ItemStack left, ItemStack right, String newName) {
        if (isRunestoneAttachable(left) && isRunestone(right)) {
            return handleRunestoneAttachment(left, right, newName);
        }

        if (hasRunestones(left) && isStone(right)) {
            return handleRunestoneRemoval(left, newName);
        }

        return GlyphAnvilHandler.AnvilResult.PASS;
    }

    private static GlyphAnvilHandler.AnvilResult handleRunestoneAttachment(ItemStack weapon, ItemStack runestone, String newName) {
        RunestoneSlots slots = weapon.get(WitcherDataComponents.RUNESTONE_SLOTS);

        if (slots == null) {
            boolean shouldInitialize = weapon.isIn(WitcherItemTags.RUNESTONE_ATTACHABLE);

            if (shouldInitialize) {
                int defaultSlots = getDefaultRunestoneSlots(weapon);
                slots = new RunestoneSlots(defaultSlots, java.util.List.of());
            }
        }

        if (slots == null || !slots.canAttachRunestone()) {
            return GlyphAnvilHandler.AnvilResult.PASS;
        }

        ItemStack result = weapon.copy();

        RunestoneSlots newSlots = slots.withRunestone(runestone);
        result.set(WitcherDataComponents.RUNESTONE_SLOTS, newSlots);

        if (newName != null && !newName.isEmpty()) {
            result.set(DataComponentTypes.CUSTOM_NAME, Text.literal(newName));
        }

        int xpCost = calculateRunestoneXPCost(runestone);

        return new GlyphAnvilHandler.AnvilResult(result, xpCost, 1);
    }

    private static GlyphAnvilHandler.AnvilResult handleRunestoneRemoval(ItemStack weapon, String newName) {
        ItemStack result = weapon.copy();

        RunestoneSlots slots = result.get(WitcherDataComponents.RUNESTONE_SLOTS);
        if (slots == null || slots.attachedRunestones().isEmpty()) {
            return GlyphAnvilHandler.AnvilResult.PASS;
        }

        result.set(WitcherDataComponents.RUNESTONE_SLOTS, slots.removeAllRunestones());

        if (newName != null && !newName.isEmpty()) {
            result.set(DataComponentTypes.CUSTOM_NAME, Text.literal(newName));
        }

        return new GlyphAnvilHandler.AnvilResult(result, 0, 1);
    }

    private static int calculateRunestoneXPCost(ItemStack runestone) {
        Identifier id = Registries.ITEM.getId(runestone.getItem());
        String name = id.getPath();

        if (name.contains("lesser")) {
            return 1 + RANDOM.nextInt(2);
        } else if (name.contains("greater")) {
            return 5 + RANDOM.nextInt(3);
        } else {
            return 3 + RANDOM.nextInt(2);
        }
    }

    private static boolean isRunestoneAttachable(ItemStack stack) {
        RunestoneSlots slots = stack.get(WitcherDataComponents.RUNESTONE_SLOTS);
        if (slots != null && slots.maxSlots() > 0) {
            return true;
        }
        return stack.isIn(WitcherItemTags.RUNESTONE_ATTACHABLE);
    }

    private static int getDefaultRunestoneSlots(ItemStack item) {
        String itemId = Registries.ITEM.getId(item.getItem()).toString();
        if (itemId.contains("netherite") || itemId.contains("diamond")) return 2;
        return 1;
    }

    private static boolean isRunestone(ItemStack stack) {
        return stack.isIn(WitcherItemTags.RUNESTONES_0) ||
               stack.isIn(WitcherItemTags.RUNESTONES_1) ||
               stack.isIn(WitcherItemTags.RUNESTONES_2);
    }

    private static boolean hasRunestones(ItemStack stack) {
        RunestoneSlots slots = stack.get(WitcherDataComponents.RUNESTONE_SLOTS);
        return slots != null && !slots.attachedRunestones().isEmpty();
    }

    private static boolean isStone(ItemStack stack) {
        return stack.isOf(Items.STONE);
    }
}
