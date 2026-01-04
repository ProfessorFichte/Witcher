package net.witcher_rpg.client.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.item.component.RunestoneSlots;
import net.witcher_rpg.item.component.RunestoneTooltipComponent;

import java.util.LinkedHashMap;
import java.util.Map;

public class RunestoneTooltipRenderer implements TooltipComponent {
    private static final Identifier EMPTY_SLOT_TEXTURE = WitcherClassMod.id("textures/gui/empty_runestone_slot.png");
    private final RunestoneSlots slots;

    public RunestoneTooltipRenderer(RunestoneTooltipComponent component) {
        this.slots = component.slots();
    }

    @Override
    public int getHeight() {
        if (!Screen.hasAltDown()) {
            return 20; // Just header and hint
        }

        // Calculate height: header (10) + items display (20) + list
        int uniqueItems = countUniqueItems();
        int emptySlots = slots.maxSlots() - slots.attachedRunestones().size();
        int listLines = uniqueItems + (emptySlots > 0 ? 1 : 0);

        // 10 (header) + 20 (items) + (listLines * 10) + 2 (padding)
        return 32 + (listLines * 10);
    }

    private int countUniqueItems() {
        Map<String, Integer> itemCounts = new LinkedHashMap<>();
        for (ItemStack runestone : slots.attachedRunestones()) {
            String key = runestone.getItem().toString();
            itemCounts.put(key, itemCounts.getOrDefault(key, 0) + 1);
        }
        return itemCounts.size();
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return Math.max(120, 18 * slots.maxSlots() + 4);
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        // Always show the slot count
        Text text = Text.translatable("item.witcher_rpg.runestone_slots.tooltip")
            .append(String.format("%d/%d", slots.attachedRunestones().size(), slots.maxSlots()));
        context.drawText(textRenderer, text, x, y, 0xAAAAAA, false);

        // Show hint or details based on Alt key
        if (!Screen.hasAltDown()) {
            // Show hint in grey
            Text hint = Text.translatable("item.witcher_rpg.runestone_render.tooltip");
            context.drawText(textRenderer, hint, x, y + 10, 0x808080, false);
        } else {
            // Show runestone items and empty slots
            int slotY = y + 12;
            int slotX = x;

            for (ItemStack runestone : slots.attachedRunestones()) {
                context.drawItem(runestone, slotX, slotY);
                slotX += 18;
            }

            for (int i = slots.attachedRunestones().size(); i < slots.maxSlots(); i++) {
                context.drawTexture(
                    EMPTY_SLOT_TEXTURE,
                    slotX, slotY,
                    0, 0,
                    16, 16,
                    16, 16
                );
                slotX += 18;
            }

            // Draw item list below the items
            int listY = y + 32;
            Map<String, Integer> itemCounts = new LinkedHashMap<>();
            Map<String, String> itemNames = new LinkedHashMap<>();

            // Count items by type
            for (ItemStack runestone : slots.attachedRunestones()) {
                String key = runestone.getItem().toString();
                String displayName = runestone.getName().getString();
                itemCounts.put(key, itemCounts.getOrDefault(key, 0) + 1);
                itemNames.putIfAbsent(key, displayName);
            }

            // Draw counted items
            for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
                String displayName = itemNames.get(entry.getKey());
                int count = entry.getValue();
                Text listText = Text.literal("- " + count + "x " + displayName);
                context.drawText(textRenderer, listText, x, listY, 0xAAAAAA, false);
                listY += 10;
            }

            // Draw empty slots if any
            int emptySlots = slots.maxSlots() - slots.attachedRunestones().size();
            if (emptySlots > 0) {
                Text emptyText = Text.literal("- " + emptySlots + "x ")
                    .append(Text.translatable("item.witcher_rpg.empty_runestone_slot"));
                context.drawText(textRenderer, emptyText, x, listY, 0x808080, false);
            }
        }
    }
}
