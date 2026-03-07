package net.witcher_rpg.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.item.misc.MasterSpellBook;
import net.witcher_rpg.item.misc.UpgradeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherMaterials {

    public static class Container { Item item; }

    public record Entry(Identifier id, String translatedName,Function<Item.Settings, Item> factory,
                        Item.Settings settings, Container container) {
        public Entry(Identifier id, String translatedName,Function<Item.Settings, Item> factory, Item.Settings settings) {
            this(id, translatedName, factory, settings, new Container());
        }
        public Item item() { return container.item; }
    }

    public static final ArrayList<Entry> ENTRIES = new ArrayList<>();
    public static Entry add(Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }
    public static final Entry SILVER_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "silver_ingot"),
            "Silver Ingot",
            Item::new,
            new Item.Settings()
    ));

    public static final Entry SILVER_NUGGET = add(new Entry(
            Identifier.of(MOD_ID, "silver_nugget"),
            "Silver Nugger",
            Item::new,
            new Item.Settings()
    ));

    public static final Entry METEORITE_SILVER_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "meteorite_silver_ingot"),
            "Meteorite Silver Ingot",
            Item::new,
            new Item.Settings()
    ));

    public static final Entry DARK_STEEL_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "dark_steel_ingot"),
            "Dark Steel Ingot",
            Item::new,
            new Item.Settings()
    ));

    public static final Entry STEEL_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "steel_ingot"),
            "Steel Ingot",
            Item::new,
            new Item.Settings()
    ));

    public static final Entry STEEL_NUGGET = add(new Entry(
            Identifier.of(MOD_ID, "steel_nugget"),
            "Steel Nugget",
            Item::new,
            new Item.Settings()
    ));

    public static final Entry RAW_SILVER = add(new Entry(
            Identifier.of(MOD_ID, "raw_silver"),
            "Raw Silver",
            Item::new,
            new Item.Settings()
    ));

    public static final Entry METEORITE = add(new Entry(
            Identifier.of(MOD_ID, "meteorite"),
            "Meteorite",
            Item::new,
            new Item.Settings()
    ));

    public static final Entry METEORITE_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "meteorite_ingot"),
            "Meteorite Ingot",
            Item::new,
            new Item.Settings()
    ));

    public static final Entry DARK_IRON_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "dark_iron_ingot"),
            "Dark Iron Ingot",
            Item::new,
            new Item.Settings()
    ));

    public static final Entry RAW_DARK_IRON = add(new Entry(
            Identifier.of(MOD_ID, "raw_dark_iron"),
            "Raw Dark Iron",
            Item::new,
            new Item.Settings()
    ));

    public static final Entry DIMERITIUM_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "dimeritium_ingot"),
            "Dimeritium Ingot",
            settings -> new UpgradeItem(settings, "item.witcher_rpg.dimeritium_ingot.applies_to"),
            new Item.Settings()
    ));

    // Container for MASTER_BOOK - initialized during registration
    private static final Container MASTER_BOOK_CONTAINER = new Container();
    public static MasterSpellBook MASTER_BOOK() {
        return (MasterSpellBook) MASTER_BOOK_CONTAINER.item;
    }

    public static void registerModItems() {
        for (Entry e : ENTRIES) {
            Item item = e.factory().apply(e.settings());
            e.container.item = item;
            Registry.register(Registries.ITEM, e.id(), item);
        }
        MASTER_BOOK_CONTAINER.item = new MasterSpellBook(new Item.Settings().maxCount(1));
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "master_spell_book"), MASTER_BOOK());
        ItemGroupEvents.modifyEntriesEvent(WitcherGroup.WITCHER_KEY).register(content -> {
            for (Entry e : ENTRIES) {
                content.add(e.item());
            }
            content.add(MASTER_BOOK());
        });

        WitcherArmorDiagrams.register();
        WitcherClassMod.LOGGER.info("Registered Witcher Items");
    }
}
