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

/// Witcher's per-stack item data on 1.20.1 (the 1.21 data components `glyph_slots` and `runestone_slots`),
/// stored as NBT under a `witcher_rpg` sub-compound. 1.20.1 has no data-component registry, so this is the
/// same NBT facade shape Spell Engine's own `SpellItemData` uses on this line.
///
/// Values not present on the stack fall back to the **item-level defaults** registered through
/// {@link #defaults(Item)} (or {@link #defaults(Item.Settings)} before the item exists), which replaces
/// `Item.Settings#component(...)`: a fresh `new ItemStack(item)` carries no NBT and still reads its slots.
public class WitcherDataComponents {
    public static final String ROOT = "witcher_rpg";
    public static final String GLYPH_SLOTS = "glyph_slots";
    public static final String RUNESTONE_SLOTS = "runestone_slots";

    // MARK: Raw NBT access

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

    /// Removes a key; drops the `witcher_rpg` compound (and an emptied stack NBT) when nothing is left in it
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

    // MARK: Glyph slots

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

    // MARK: Runestone slots

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

    // MARK: Item-level defaults (replacement for `Item.Settings#component(...)`)

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

    /// Defaults attached to an `Item.Settings` **before** the item exists. They are adopted by the item
    /// constructed from these settings (`ItemDefaultsMixin` on `Item.<init>`).
    public static Defaults defaults(Item.Settings settings) {
        return pendingDefaults.computeIfAbsent(settings, key -> new Defaults());
    }

    /// Called by the `Item.<init>` mixin: moves settings-attached defaults to the constructed item
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
