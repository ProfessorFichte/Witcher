package net.witcher_rpg.compat.jewelry;

import com.google.common.base.Suppliers;
import net.jewelry.gems.GemCut;
import net.jewelry.gems.GemItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.spell_engine.PlatformEvents;
import net.witcher_rpg.item.WitcherGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherGlyphRunestones {
    public static final List<Entry> entries = new ArrayList<>();

    public static final class Entry {
        private final String name;
        private final String translatedName;
        private final boolean glyph;
        private final boolean showInItemGroup;
        private final String tooltipKey;
        private final Supplier<Item> item;

        private Entry(String name, String translatedName, boolean glyph, boolean showInItemGroup, String tooltipKey) {
            this.name = name;
            this.translatedName = translatedName;
            this.glyph = glyph;
            this.showInItemGroup = showInItemGroup;
            this.tooltipKey = tooltipKey;
            this.item = Suppliers.memoize(() -> {
                var settings = new Item.Settings().rarity(rarityFor(name));
                return new InfusionGemItem(settings, tooltipKey);
            });
        }

        private static Rarity rarityFor(String name) {
            if (name.contains("lesser")) return Rarity.COMMON;
            if (name.contains("greater")) return Rarity.RARE;
            if (name.contains("glyph")) return Rarity.UNCOMMON;
            return Rarity.COMMON;
        }

        public Identifier id() { return Identifier.of(MOD_ID, name); }
        public String name() { return name; }
        public String translatedName() { return translatedName; }
        public Supplier<Item> item() { return item; }
        public boolean isGlyph() { return glyph; }
        public boolean showInItemGroup() { return showInItemGroup; }
        public String tooltipKey() { return tooltipKey; }
    }

    private static final class InfusionGemItem extends GemItem {
        private final String tooltipKey;

        private InfusionGemItem(Item.Settings settings, String tooltipKey) {
            super(settings);
            this.tooltipKey = tooltipKey;
        }

        @Override
        public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
            super.appendTooltip(stack, context, tooltip, type);
            if (tooltipKey != null && GemCut.of(stack).isEmpty()) {
                tooltip.add(Text.translatable(tooltipKey).formatted(Formatting.GRAY));
            }
        }
    }

    private static Entry glyph(String name, String translatedName) {
        return glyph(name, translatedName, false, null);
    }

    private static Entry glyph(String name, String translatedName, boolean showInItemGroup, String tooltipKey) {
        var entry = new Entry(name, translatedName, true, showInItemGroup, tooltipKey);
        entries.add(entry);
        return entry;
    }

    private static Entry runestone(String name, String translatedName) {
        return runestone(name, translatedName, false, null);
    }

    private static Entry runestone(String name, String translatedName, boolean showInItemGroup, String tooltipKey) {
        var entry = new Entry(name, translatedName, false, showInItemGroup, tooltipKey);
        entries.add(entry);
        return entry;
    }

    ///GLYPHS
    public static final Entry LESSER_GLYPH_OF_INFUSION = glyph("lesser_glyph_of_infusion", "Lesser Glyph of Infusion",
            true, Identifier.of(MOD_ID, "lesser_glyph_of_infusion").toTranslationKey("item") + ".tooltip");
    public static final Entry GLYPH = glyph("glyph", "Glyph");
    public static final Entry GREATER_GLYPH = glyph("greater_glyph", "Greater Glyph");

    ///RUNESTONES
    public static final Entry LESSER_RUNESTONE = runestone("lesser_runestone", "Lesser Runestone",
            true, Identifier.of(MOD_ID, "lesser_runestone").toTranslationKey("item") + ".tooltip");
    public static final Entry RUNESTONE = runestone("runestone", "Runestone");
    public static final Entry GREATER_RUNESTONE = runestone("greater_runestone", "Greater Runestone");

    private static final Supplier<Item> CUT_TABLE_SENTINEL = Suppliers.memoize(() -> new GemItem(new Item.Settings()));
    public static Identifier cutTableSentinelId() { return Identifier.of(MOD_ID, "cut_table_sentinel"); }
    public static Item cutTableSentinel() { return CUT_TABLE_SENTINEL.get(); }

    public static void register() {
        if (!JewelrySocketFeature.isJewelryPresent()) return;
        for (var entry : entries) {
            boolean enabled = entry.isGlyph() ? JewelrySocketFeature.glyphsActive() : JewelrySocketFeature.runestonesActive();
            if (enabled) {
                Registry.register(Registries.ITEM, entry.id(), entry.item().get());
            }
        }
        Registry.register(Registries.ITEM, cutTableSentinelId(), cutTableSentinel());
        PlatformEvents.onItemGroupModify(WitcherGroup.WITCHER_KEY, (content, context) -> {
            for (var entry : entries) {
                if (!entry.showInItemGroup()) continue;
                boolean enabled = entry.isGlyph() ? JewelrySocketFeature.glyphsActive() : JewelrySocketFeature.runestonesActive();
                if (enabled) {
                    content.add(entry.item().get());
                }
            }
        });
    }
}
