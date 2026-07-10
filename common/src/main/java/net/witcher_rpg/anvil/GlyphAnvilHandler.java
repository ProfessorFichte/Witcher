package net.witcher_rpg.anvil;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;
import net.witcher_rpg.item.component.GlyphSlots;
import net.witcher_rpg.item.component.WitcherDataComponents;
import net.witcher_rpg.util.tags.WitcherItemTags;

import java.util.*;

public class GlyphAnvilHandler {
    private static final Random RANDOM = new Random();

    public static AnvilResult handleAnvilUpdate(ItemStack left, ItemStack right, String newName) {
        if (isGlyphAttachable(left) && isGlyph(right)) {
            return handleGlyphAttachment(left, right, newName);
        }

        if (hasGlyphs(left) && isStone(right)) {
            return handleGlyphRemoval(left, newName);
        }

        return AnvilResult.PASS;
    }

    private static AnvilResult handleGlyphAttachment(ItemStack armor, ItemStack glyph, String newName) {
        GlyphSlots slots = armor.get(WitcherDataComponents.GLYPH_SLOTS);

        if (slots == null) {
            boolean shouldInitialize = armor.isIn(WitcherItemTags.GLYPH_ATTACHABLE);

            if (shouldInitialize) {
                int defaultSlots = getDefaultGlyphSlots(armor);
                slots = new GlyphSlots(defaultSlots, List.of());
            }
        }

        if (slots == null) {
            return AnvilResult.PASS;
        }

        if (!slots.canAttachGlyph()) {
            return AnvilResult.PASS;
        }

        ItemStack result = armor.copy();

        GlyphSlots newSlots = slots.withGlyph(glyph);
        result.set(WitcherDataComponents.GLYPH_SLOTS, newSlots);

        rebuildAllSpellContainers(result, newSlots.attachedGlyphs());

        if (newName != null && !newName.isEmpty()) {
            result.set(DataComponentTypes.CUSTOM_NAME, Text.literal(newName));
        }

        int xpCost = calculateGlyphXPCost(glyph);

        return new AnvilResult(result, xpCost, 1);
    }

    private static AnvilResult handleGlyphRemoval(ItemStack armor, String newName) {
        ItemStack result = armor.copy();

        GlyphSlots slots = result.get(WitcherDataComponents.GLYPH_SLOTS);
        if (slots == null || slots.attachedGlyphs().isEmpty()) {
            return AnvilResult.PASS;
        }

        result.set(WitcherDataComponents.GLYPH_SLOTS, slots.removeAllGlyphs());
        removeGlyphSpellContainers(result, slots.attachedGlyphs());

        if (newName != null && !newName.isEmpty()) {
            result.set(DataComponentTypes.CUSTOM_NAME, Text.literal(newName));
        }

        return new AnvilResult(result, 0, 1);
    }

    private static void rebuildAllSpellContainers(ItemStack armor, List<ItemStack> glyphs) {
        // LinkedHashSet preserves insertion order and deduplicates spell IDs across glyphs
        Set<String> uniqueSpellIds = new LinkedHashSet<>();

        for (ItemStack glyph : glyphs) {
            SpellContainer container = glyph.get(SpellDataComponents.SPELL_CONTAINER);
            if (container != null) {
                uniqueSpellIds.addAll(extractSpellIds(container));
            }
        }

        if (uniqueSpellIds.isEmpty()) {
            armor.remove(SpellDataComponents.SPELL_CONTAINER);
            return;
        }

        SpellContainer merged = createSpellContainer(new ArrayList<>(uniqueSpellIds));
        if (merged != null) {
            armor.set(SpellDataComponents.SPELL_CONTAINER, merged);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<String> extractSpellIds(SpellContainer container) {
        try {
            var componentType = Registries.DATA_COMPONENT_TYPE.get(Identifier.of("spell_engine", "spell_container"));
            if (componentType != null) {
                var rawCodec = componentType.getCodec();
                if (rawCodec != null) {
                    var codec = (com.mojang.serialization.Codec<SpellContainer>) rawCodec;
                    var result = codec.encodeStart(NbtOps.INSTANCE, container);
                    if (result.isSuccess()) {
                        NbtElement nbt = result.getOrThrow();
                        if (nbt instanceof NbtCompound compound) {
                            if (compound.contains("spell_ids", NbtElement.LIST_TYPE)) {
                                NbtList list = compound.getList("spell_ids", NbtElement.STRING_TYPE);
                                List<String> ids = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    ids.add(list.getString(i));
                                }
                                return ids;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // If extraction fails, return empty list
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private static SpellContainer createSpellContainer(List<String> spellIds) {
        try {
            NbtCompound compound = new NbtCompound();
            NbtList list = new NbtList();
            for (String id : spellIds) {
                list.add(NbtString.of(id));
            }
            compound.put("spell_ids", list);
            compound.putString("content", "ANY");

            var componentType = Registries.DATA_COMPONENT_TYPE.get(Identifier.of("spell_engine", "spell_container"));
            if (componentType != null) {
                var rawCodec = componentType.getCodec();
                if (rawCodec != null) {
                    var codec = (com.mojang.serialization.Codec<SpellContainer>) rawCodec;
                    var result = codec.decode(NbtOps.INSTANCE, compound);
                    if (result.isSuccess()) {
                        return result.getOrThrow().getFirst();
                    }
                }
            }
        } catch (Exception e) {
            // If creation fails, return null
        }
        return null;
    }

    private static boolean hasDuplicateSpell(List<ItemStack> existingGlyphs, ItemStack newGlyph) {
        SpellContainer newContainer = newGlyph.get(SpellDataComponents.SPELL_CONTAINER);
        if (newContainer == null) {
            return false;
        }

        List<String> newSpellIds = extractSpellIds(newContainer);
        if (newSpellIds.isEmpty()) {
            return false;
        }

        for (ItemStack existingGlyph : existingGlyphs) {
            SpellContainer existingContainer = existingGlyph.get(SpellDataComponents.SPELL_CONTAINER);
            if (existingContainer != null) {
                List<String> existingSpellIds = extractSpellIds(existingContainer);
                for (String newSpellId : newSpellIds) {
                    if (existingSpellIds.contains(newSpellId)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static void removeGlyphSpellContainers(ItemStack armor, java.util.List<ItemStack> glyphs) {
        if (glyphs.isEmpty()) return;

        SpellContainer current = armor.get(SpellDataComponents.SPELL_CONTAINER);
        if (current == null) return;

        armor.remove(SpellDataComponents.SPELL_CONTAINER);
    }

    private static int calculateGlyphXPCost(ItemStack glyph) {
        Identifier id = Registries.ITEM.getId(glyph.getItem());
        String name = id.getPath();

        if (name.contains("lesser")) {
            return 1 + RANDOM.nextInt(2);
        } else if (name.contains("greater")) {
            return 5 + RANDOM.nextInt(3);
        } else {
            return 3 + RANDOM.nextInt(2);
        }
    }

    private static boolean isGlyphAttachable(ItemStack stack) {
        GlyphSlots slots = stack.get(WitcherDataComponents.GLYPH_SLOTS);
        return (slots != null && slots.maxSlots() > 0) || stack.isIn(WitcherItemTags.GLYPH_ATTACHABLE);
    }

    private static int getDefaultGlyphSlots(ItemStack item) {
        String itemId = Registries.ITEM.getId(item.getItem()).toString();
        if (itemId.contains("netherite") || itemId.contains("diamond")) return 2;
        return 1;
    }

    private static boolean isGlyph(ItemStack stack) {
        return stack.isIn(WitcherItemTags.GLYPHS_0) ||
               stack.isIn(WitcherItemTags.GLYPHS_1) ||
               stack.isIn(WitcherItemTags.GLYPHS_2);
    }

    private static boolean hasGlyphs(ItemStack stack) {
        GlyphSlots slots = stack.get(WitcherDataComponents.GLYPH_SLOTS);
        return slots != null && !slots.attachedGlyphs().isEmpty();
    }

    private static boolean isStone(ItemStack stack) {
        return stack.isOf(Items.STONE);
    }

    public static class AnvilResult {
        public static final AnvilResult PASS = new AnvilResult(null, 0, 0);

        public final ItemStack output;
        public final int xpCost;
        public final int materialCost;

        public AnvilResult(ItemStack output, int xpCost, int materialCost) {
            this.output = output;
            this.xpCost = xpCost;
            this.materialCost = materialCost;
        }

        public boolean shouldHandle() {
            return output != null;
        }
    }
}
