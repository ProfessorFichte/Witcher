package net.witcher_rpg.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.item.armor.Armors;
import net.witcher_rpg.item.weapon.WeaponsRegister;
import net.spell_engine.api.item.SpellBooks;

import java.util.HashMap;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherItems {

    public static final HashMap<String, Item> entries;
    static {
        entries = new HashMap<>();
        for(var weaponEntry: WeaponsRegister.entries) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        for(var entry: Armors.entries) {
            var set = entry.armorSet();
            for (var piece: set.pieces()) {
                var armorItem = (ArmorItem) piece;
                entries.put(set.idOf(armorItem).toString(), armorItem);
            }
        }
    }

    public static final Item SILVER_INGOT  = registerItem("silver_ingot", new Item(new Item.Settings()));
    public static final Item SILVER_NUGGET  = registerItem("silver_nugget", new Item(new Item.Settings()));
    public static final Item METEORITE_SILVER_INGOT = registerItem("meteorite_silver_ingot", new Item(new Item.Settings()));
    public static final Item DARK_STEEL_INGOT= registerItem("dark_steel_ingot", new Item(new Item.Settings()));
    public static final Item STEEL_INGOT= registerItem("steel_ingot", new Item(new Item.Settings()));
    public static final Item STEEL_NUGGET= registerItem("steel_nugget", new Item(new Item.Settings()));
    public static final Item RAW_SILVER = registerItem("raw_silver", new Item(new Item.Settings()));
    public static final Item METEORITE = registerItem("meteorite", new Item(new Item.Settings()));
    public static final Item METEORITE_INGOT = registerItem("meteorite_ingot", new Item(new Item.Settings()));
    public static final Item DARK_IRON_INGOT = registerItem("dark_iron_ingot", new Item(new Item.Settings()));
    public static final Item RAW_DARK_IRON = registerItem("raw_dark_iron", new Item(new Item.Settings()));
    public static final MasterSpellBook MASTER_BOOK = new MasterSpellBook( Identifier.of(MOD_ID,"master_book"),new Item.Settings().maxCount(1));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), item);
    }

    public static void registerModItems(){

        SpellBooks.createAndRegister(Identifier.of(MOD_ID,"base_signs"), WitcherGroup.WITCHER_KEY);
        SpellBooks.createAndRegister(Identifier.of(MOD_ID,"fencing"), WitcherGroup.WITCHER_KEY);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "master_spell_book"), MASTER_BOOK);

        ItemGroupEvents.modifyEntriesEvent(WitcherGroup.WITCHER_KEY).register((content) -> {
            content.add(MASTER_BOOK);
            content.add(RAW_SILVER);
            content.add(RAW_DARK_IRON);
            content.add(METEORITE);
            content.add(SILVER_INGOT);
            content.add(SILVER_NUGGET);
            content.add(METEORITE_SILVER_INGOT);
            content.add(STEEL_INGOT);
            content.add(STEEL_NUGGET);
            content.add(METEORITE_INGOT);
            content.add(DARK_IRON_INGOT);
            content.add(DARK_STEEL_INGOT);
        });
        WitcherArmorDiagrams.register();
        //WitcherArmorDiagram.registerWitcherArmorDiagrams();
        WitcherClassMod.LOGGER.info("Registering Mod Items for " + MOD_ID);
    }
}
