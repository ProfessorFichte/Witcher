package net.witcher_rpg.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.SpellBooks;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.item.misc.MasterSpellBook;
import net.witcher_rpg.item.misc.UpgradeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherMaterials {

    public static class Container { Item item; }

    public record Entry(Identifier id, Function<Item.Settings, Item> factory,
                        Item.Settings settings, Container container) {
        public Entry(Identifier id, Function<Item.Settings, Item> factory, Item.Settings settings) {
            this(id, factory, settings, new Container());
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
            Item::new,
            new Item.Settings()
    ));

    public static final Entry SILVER_NUGGET = add(new Entry(
            Identifier.of(MOD_ID, "silver_nugget"),
            Item::new,
            new Item.Settings()
    ));

    public static final Entry METEORITE_SILVER_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "meteorite_silver_ingot"),
            Item::new,
            new Item.Settings()
    ));

    public static final Entry DARK_STEEL_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "dark_steel_ingot"),
            Item::new,
            new Item.Settings()
    ));

    public static final Entry STEEL_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "steel_ingot"),
            Item::new,
            new Item.Settings()
    ));

    public static final Entry STEEL_NUGGET = add(new Entry(
            Identifier.of(MOD_ID, "steel_nugget"),
            Item::new,
            new Item.Settings()
    ));

    public static final Entry RAW_SILVER = add(new Entry(
            Identifier.of(MOD_ID, "raw_silver"),
            Item::new,
            new Item.Settings()
    ));

    public static final Entry METEORITE = add(new Entry(
            Identifier.of(MOD_ID, "meteorite"),
            Item::new,
            new Item.Settings()
    ));

    public static final Entry METEORITE_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "meteorite_ingot"),
            Item::new,
            new Item.Settings()
    ));

    public static final Entry DARK_IRON_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "dark_iron_ingot"),
            Item::new,
            new Item.Settings()
    ));

    public static final Entry RAW_DARK_IRON = add(new Entry(
            Identifier.of(MOD_ID, "raw_dark_iron"),
            Item::new,
            new Item.Settings()
    ));

    public static final Entry DIMERITIUM_INGOT = add(new Entry(
            Identifier.of(MOD_ID, "dimeritium_ingot"),
            settings -> new UpgradeItem(settings, "item.witcher_rpg.dimeritium_ingot.applies_to"),
            new Item.Settings()
    ));

    public static final MasterSpellBook MASTER_BOOK =
            new MasterSpellBook(Identifier.of(MOD_ID, "master_spell_book"), new Item.Settings().maxCount(1));

    public static void registerModItems() {
        for (Entry e : ENTRIES) {
            Item item = e.factory().apply(e.settings());
            e.container.item = item;
            Registry.register(Registries.ITEM, e.id(), item);
        }
        var books = List.of("base_signs", "fencing");
        for (var name : books) {
            SpellBooks.createAndRegister(Identifier.of(MOD_ID, name), WitcherGroup.WITCHER_KEY);
        }
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "master_spell_book"), MASTER_BOOK);
        ItemGroupEvents.modifyEntriesEvent(WitcherGroup.WITCHER_KEY).register(content -> {
            for (Entry e : ENTRIES) {
                content.add(e.item());
            }
            content.add(MASTER_BOOK);
        });

        WitcherArmorDiagrams.register();
        WitcherClassMod.LOGGER.info("Registered Witcher Items");
    }
}
