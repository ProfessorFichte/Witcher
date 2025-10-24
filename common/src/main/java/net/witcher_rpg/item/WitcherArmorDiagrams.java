package net.witcher_rpg.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherArmorDiagrams {

    public static class Container { Item item; }

    public record Entry(Identifier id, Function<Item.Settings, Item> factory, Item.Settings settings, Container container) {
        public Entry(Identifier id, Function<Item.Settings, Item> factory, Item.Settings settings) {
            this(id, factory, settings, new Container());
        }
        public Item item() { return container.item; }
    }

    public static final ArrayList<Entry> ENTRIES = new ArrayList<>();
    private static Entry add(Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static final List<Identifier> BASE_ITEMS = List.of(
            Identifier.of("item/empty_slot_sword"),
            Identifier.of("item/empty_armor_slot_helmet"),
            Identifier.of("item/empty_armor_slot_chestplate"),
            Identifier.of("item/empty_armor_slot_leggings"),
            Identifier.of("item/empty_armor_slot_boots")
    );

    public static final List<Identifier> INGREDIENT_ITEMS_WITCHER_ARMOR = List.of(
            Identifier.of("item/empty_slot_ingot")
    );

    private static final String[] KEYS = {
            "enhanced", "superior", "mastercrafted", "grandmaster"
    };

    static {
        for (String key : KEYS) {
            add(new Entry(
                    Identifier.of(MOD_ID, key + "_diagram"),
                    settings -> new SmithingTemplateItem(
                            Text.translatable("smithing_template.witcher_rpg." + key + ".applies_to").formatted(Formatting.DARK_GREEN),
                            Text.translatable("smithing_template.witcher_rpg." + key + ".ingredients").formatted(Formatting.DARK_GREEN),
                            Text.translatable("smithing_template.witcher_rpg." + key + ".title").formatted(Formatting.GREEN),
                            Text.translatable("smithing_template.witcher_rpg." + key + ".base_slot_description"),
                            Text.translatable("smithing_template.witcher_rpg." + key + ".additions_slot_description"),
                            BASE_ITEMS,
                            INGREDIENT_ITEMS_WITCHER_ARMOR
                    ),
                    new Item.Settings()
            ));
        }
    }

    public static void register() {
        for (Entry entry : ENTRIES) {
            Item item = entry.factory().apply(entry.settings());
            entry.container.item = item;
            Registry.register(Registries.ITEM, entry.id(), item);
        }

        ItemGroupEvents.modifyEntriesEvent(WitcherGroup.WITCHER_KEY).register(content -> {
            for (var entry : ENTRIES) {
                content.add(entry.item());
            }
        });
    }
}
