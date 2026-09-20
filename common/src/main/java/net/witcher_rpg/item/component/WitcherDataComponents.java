package net.witcher_rpg.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.witcher_rpg.WitcherClassMod;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class WitcherDataComponents {
    public static final String ROOT = "witcher_rpg";
    public static final String GLYPH_SLOTS = "glyph_slots";
    public static final String RUNESTONE_SLOTS = "runestone_slots";

    @Nullable
    public static NbtCompound root(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return stack.getSubNbt(ROOT);
    }

    public static NbtCompound rootOrCreate(ItemStack stack) {
        return stack.getOrCreateSubNbt(ROOT);
    }

    public static void remove(ItemStack stack, String key) {
        var root = root(stack);
        if (root == null) {
            return;
        }
        root.remove(key);
        if (root.isEmpty()) {
            stack.removeSubNbt(ROOT);
            var nbt = stack.getNbt();
            if (nbt != null && nbt.isEmpty()) {
                stack.setNbt(null);
            }
        }
    }

    @Nullable
    public static <T> T get(ItemStack stack, String key, Codec<T> codec) {
        var root = root(stack);
        if (root == null || !root.contains(key)) {
            return null;
        }
        return codec.parse(NbtOps.INSTANCE, root.get(key))
                .resultOrPartial(error -> WitcherClassMod.LOGGER.error("Failed to read " + ROOT + "." + key + " from item stack: " + error))
                .orElse(null);
    }

    public static <T> void set(ItemStack stack, String key, Codec<T> codec, @Nullable T value) {
        if (value == null) {
            remove(stack, key);
            return;
        }
        var encoded = codec.encodeStart(NbtOps.INSTANCE, value)
                .resultOrPartial(error -> WitcherClassMod.LOGGER.error("Failed to write " + ROOT + "." + key + " to item stack: " + error));
        if (encoded.isPresent()) {
            rootOrCreate(stack).put(key, encoded.get());
        }
    }

    @Nullable
    public static GlyphSlots getGlyphSlots(ItemStack stack) {
        var stored = get(stack, GLYPH_SLOTS, GlyphSlots.CODEC);
        if (stored != null) {
            return stored;
        }
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        var defaults = itemDefaults.get(stack.getItem());
        return defaults != null ? defaults.glyphSlots : null;
    }

    public static void setGlyphSlots(ItemStack stack, @Nullable GlyphSlots value) {
        set(stack, GLYPH_SLOTS, GlyphSlots.CODEC, value);
    }

    public static void removeGlyphSlots(ItemStack stack) {
        remove(stack, GLYPH_SLOTS);
    }

    @Nullable
    public static RunestoneSlots getRunestoneSlots(ItemStack stack) {
        var stored = get(stack, RUNESTONE_SLOTS, RunestoneSlots.CODEC);
        if (stored != null) {
            return stored;
        }
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        var defaults = itemDefaults.get(stack.getItem());
        return defaults != null ? defaults.runestoneSlots : null;
    }

    public static void setRunestoneSlots(ItemStack stack, @Nullable RunestoneSlots value) {
        set(stack, RUNESTONE_SLOTS, RunestoneSlots.CODEC, value);
    }

    public static void removeRunestoneSlots(ItemStack stack) {
        remove(stack, RUNESTONE_SLOTS);
    }

    public static class Defaults {
        @Nullable public GlyphSlots glyphSlots;
        @Nullable public RunestoneSlots runestoneSlots;

        public Defaults glyphSlots(@Nullable GlyphSlots value) {
            this.glyphSlots = value;
            return this;
        }

        public Defaults runestoneSlots(@Nullable RunestoneSlots value) {
            this.runestoneSlots = value;
            return this;
        }

        public boolean isEmpty() {
            return glyphSlots == null && runestoneSlots == null;
        }
    }

    private static final Map<Item, Defaults> itemDefaults = new HashMap<>();
    private static final Map<Item.Settings, Defaults> pendingDefaults = new WeakHashMap<>();

    public static Defaults defaults(Item item) {
        return itemDefaults.computeIfAbsent(item, key -> new Defaults());
    }

    @Nullable
    public static Defaults defaultsOf(Item item) {
        return itemDefaults.get(item);
    }

    public static Defaults defaults(Item.Settings settings) {
        return pendingDefaults.computeIfAbsent(settings, key -> new Defaults());
    }

    public static void adoptPendingDefaults(Item item, Item.Settings settings) {
        var pending = pendingDefaults.remove(settings);
        if (pending == null || pending.isEmpty()) {
            return;
        }
        var target = defaults(item);
        if (pending.glyphSlots != null) { target.glyphSlots = pending.glyphSlots; }
        if (pending.runestoneSlots != null) { target.runestoneSlots = pending.runestoneSlots; }
    }

    public static void register() {
        WitcherClassMod.LOGGER.info("Registering Witcher item data");
    }
}
