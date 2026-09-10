package net.witcher_rpg.blocks;

import net.spell_engine.PlatformEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.item.WitcherGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class WitcherBlocks {
    public record Entry(String name, String translatedName ,Block block, BlockItem item) {
        public Entry(String name, String translatedName ,Block block) {
            this(name, translatedName, block, new BlockItem(block, new Item.Settings()));
        }
    }

    public static final ArrayList<Entry> all = new ArrayList<>();

    private static Entry entry(String name, String translatedName,Block block) {
        var entry = new Entry(name, translatedName,block);
        all.add(entry);
        return entry;
    }

    public static final Entry SILVER_ORE = entry("silver_ore", "Silver Ore",new ExperienceDroppingBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(Instrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.0F, 3.0F), UniformIntProvider.create(3, 7)
    ));
    public static final Entry DEEPSLATE_SILVER_ORE= entry("deepslate_silver_ore", "Deepslate Silver Ore",new ExperienceDroppingBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(Instrument.BASEDRUM)
                    .requiresTool()
                    .strength(4.0F, 3.0F), UniformIntProvider.create(3, 7)
    ));
    public static final Entry METEORITE_ORE= entry("meteorite_ore", "Meteorite Ore",new ExperienceDroppingBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .instrument(Instrument.BASEDRUM)
                    .requiresTool()
                    .strength(4.0F, 3.0F), UniformIntProvider.create(3, 7)
    ));
    public static final Entry DEEPSLATE_DARK_IRON_ORE= entry("deepslate_dark_iron_ore", "Deepslate Dark Iron Ore",new ExperienceDroppingBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(Instrument.BASEDRUM)
                    .requiresTool()
                    .strength(4.0F, 3.0F), UniformIntProvider.create(3, 7)
    ));
    public static final Entry DARK_IRON_ORE= entry("dark_iron_ore", "Dark Iron Ore",new ExperienceDroppingBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(Instrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.0F, 3.0F), UniformIntProvider.create(3, 7)
    ));
    public static final Entry NETHER_DARK_IRON_ORE= entry("nether_dark_iron_ore", "Nether Dark Iron Ore",new ExperienceDroppingBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(Instrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.0F, 3.0F), UniformIntProvider.create(3, 7)
    ));
    public static final Entry SILVER_BLOCK = entry("silver_block", "Block of Silver",new Block(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE_GRAY)
                    .instrument(Instrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    ));
    public static final Entry RAW_SILVER_BLOCK = entry("raw_silver_block", "Block of Silver Ore",new Block(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .instrument(Instrument.BASEDRUM)
                    .requiresTool()
                    .strength(5.0F, 6.0F)
    ));
    public static final Entry STEEL_BLOCK = entry("steel_block", "Block of Steel",new Block(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(Instrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    ));
    public static final Entry METEORITE_BLOCK = entry("meteorite_block", "Block of Meteorite",new Block(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .instrument(Instrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    ));
    public static final Entry DARK_IRON_BLOCK = entry("dark_iron_block", "Block of Dark Iron",new Block(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .instrument(Instrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    ));
    public static final Entry METEORITE_SILVER_BLOCK = entry("meteorite_silver_block","Block of Meteorite Silver", new Block(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .instrument(Instrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(50.0F, 1200.0F)
                    .sounds(BlockSoundGroup.NETHERITE)
    ));
    public static final Entry DARK_STEEL_BLOCK = entry("dark_steel_block", "Block of Dark Steel",new Block(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_GREEN)
                    .instrument(Instrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(50.0F, 1200.0F)
                    .sounds(BlockSoundGroup.NETHERITE)
    ));


    /// Creation only - the blocks already exist as static fields, so this is just the id-keyed view
    /// Forge's `BLOCK` `RegisterEvent` window feeds to its own `RegisterHelper`.
    public static Map<Identifier, Block> blocksToRegister() {
        var map = new LinkedHashMap<Identifier, Block>();
        for (var entry : all) {
            map.put(new Identifier(WitcherClassMod.MOD_ID, entry.name), entry.block);
        }
        return map;
    }

    /// Blocks only. Forge 47 locks every registry outside its own `RegisterEvent` window, so the block
    /// items cannot be registered from here - they go through {@link #registerItems()}.
    public static void register() {
        blocksToRegister().forEach((id, block) -> Registry.register(Registries.BLOCK, id, block));
    }

    /// Block items, plus the creative-tab hook (installed here so both loaders install it exactly once).
    /// Must run inside the `ITEM` registration window on Forge.
    public static Map<Identifier, Item> blockItemsToRegister() {
        var map = new LinkedHashMap<Identifier, Item>();
        for (var entry : all) {
            map.put(new Identifier(WitcherClassMod.MOD_ID, entry.name), entry.item());
        }
        PlatformEvents.onItemGroupModify(WitcherGroup.WITCHER_KEY, (content, context) -> {
            for (var entry : all) {
                content.add(entry.item());
            }
        });
        return map;
    }

    /// Block items and the creative-tab hook; must run inside the `ITEM` registration window.
    public static void registerItems() {
        blockItemsToRegister().forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
    }
}
