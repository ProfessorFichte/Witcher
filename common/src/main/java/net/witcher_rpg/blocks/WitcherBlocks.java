package net.witcher_rpg.blocks;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
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

    public static final Entry SILVER_ORE = entry("silver_ore", "Silver Ore",new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
            FabricBlockSettings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.0F, 3.0F)
    ));
    public static final Entry DEEPSLATE_SILVER_ORE= entry("deepslate_silver_ore", "Deepslate Silver Ore",new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
            FabricBlockSettings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(4.0F, 3.0F)
    ));
    public static final Entry METEORITE_ORE= entry("meteorite_ore", "Meteorite Ore",new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
            FabricBlockSettings.create()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(4.0F, 3.0F)
    ));
    public static final Entry DEEPSLATE_DARK_IRON_ORE= entry("deepslate_dark_iron_ore", "Deepslate Dark Iron Ore",new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
            FabricBlockSettings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(4.0F, 3.0F)
    ));
    public static final Entry DARK_IRON_ORE= entry("dark_iron_ore", "Dark Iron Ore",new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
            FabricBlockSettings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.0F, 3.0F)
    ));
    public static final Entry NETHER_DARK_IRON_ORE= entry("nether_dark_iron_ore", "Nether Dark Iron Ore",new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
            FabricBlockSettings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.0F, 3.0F)
    ));
    public static final Entry SILVER_BLOCK = entry("silver_block", "Block of Silver",new Block(
            FabricBlockSettings.create()
                    .mapColor(MapColor.LIGHT_BLUE_GRAY)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    ));
    public static final Entry RAW_SILVER_BLOCK = entry("raw_silver_block", "Block of Silver Ore",new Block(
            FabricBlockSettings.create()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(5.0F, 6.0F)
    ));
    public static final Entry STEEL_BLOCK = entry("steel_block", "Block of Steel",new Block(
            FabricBlockSettings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    ));
    public static final Entry METEORITE_BLOCK = entry("meteorite_block", "Block of Meteorite",new Block(
            FabricBlockSettings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    ));
    public static final Entry DARK_IRON_BLOCK = entry("dark_iron_block", "Block of Dark Iron",new Block(
            FabricBlockSettings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    ));
    public static final Entry METEORITE_SILVER_BLOCK = entry("meteorite_silver_block","Block of Meteorite Silver", new Block(
            FabricBlockSettings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(50.0F, 1200.0F)
                    .sounds(BlockSoundGroup.NETHERITE)
    ));
    public static final Entry DARK_STEEL_BLOCK = entry("dark_steel_block", "Block of Dark Steel",new Block(
            FabricBlockSettings.create()
                    .mapColor(MapColor.PALE_GREEN)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(50.0F, 1200.0F)
                    .sounds(BlockSoundGroup.NETHERITE)
    ));


    public static void register() {
        for (var entry : all) {
            Registry.register(Registries.BLOCK, Identifier.of(WitcherClassMod.MOD_ID, entry.name), entry.block);
            Registry.register(Registries.ITEM, Identifier.of(WitcherClassMod.MOD_ID, entry.name), entry.item());
        }
        ItemGroupEvents.modifyEntriesEvent(WitcherGroup.WITCHER_KEY).register((content) -> {
            for (var entry : all) {
                content.add(entry.item());
            }
        });
    }


}
