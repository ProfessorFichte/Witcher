package net.witcher_rpg.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.witcher_rpg.item.armor.Armors;
import net.witcher_rpg.item.weapon.WeaponsRegister;
import org.jetbrains.annotations.Nullable;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.predicate.item.ItemPredicate;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherVanillaAdvancementProvider  extends FabricAdvancementProvider {

    public record Entry(
            Identifier id,
            String title,
            String description,
            @Nullable Identifier parent,
            Item iconItem,
            AdvancementFrame frame,
            boolean showToast,
            boolean announceToChat,
            boolean hidden,
            @Nullable String background,
            @Nullable Item[] requiredItems,
            @Nullable TagKey<Item>[] requiredItemTags,
            @Nullable Integer experienceReward
    ) {
        public String titleKey() {
            return "advancements." + id.getNamespace() + "." + id.getPath().replace("/", ".") + ".title";
        }

        public String descriptionKey() {
            return "advancements." + id.getNamespace() + "." + id.getPath().replace("/", ".") + ".description";
        }
    }


    public static final List<Entry> entries = new ArrayList<>();

    private static Entry addEntry(Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public class ModItemTags {
        public static final TagKey<Item> SILVER_SWORDS =
                TagKey.of(RegistryKeys.ITEM, Identifier.of("witcher_rpg", "silver_swords"));

        public static final TagKey<Item> STEEL_SWORDS =
                TagKey.of(RegistryKeys.ITEM, Identifier.of("witcher_rpg", "steel_swords"));
    }


    public static void init() {
        var witcher = Armors.witcherArmorSet;
        addEntry(new Entry(
                id("equipment/witcher_armor_set"),
                "From Kaer Morhen",
                "Obtain all Kaer Morhen Set parts.",
                Identifier.of("more_rpg_content", "root"),
                witcher.chest,
                AdvancementFrame.GOAL,
                true, true, false, null,
                new Item[]{
                        witcher.head,
                        witcher.chest,
                        witcher.legs,
                        witcher.feet
                },
                null,
                null
        ));
        addEntry(new Entry(
                id("equipment/silver_sword"),
                "How do you like that silver?",
                "Obtain a Silver Witcher Sword!",
                Identifier.of(MOD_ID, "equipment/witcher_armor_set"),
                WeaponsRegister.witcher_silver_sword.item(),
                AdvancementFrame.GOAL,
                true, true, false, null,
                null,
                new TagKey[]{ ModItemTags.SILVER_SWORDS },
                null
        ));
        addEntry(new Entry(
                id("equipment/steel_sword"),
                "You'll choke to death on 3 pounds of steel!",
                "Obtain a Steel Witcher Sword!",
                Identifier.of(MOD_ID, "equipment/witcher_armor_set"),
                WeaponsRegister.steel_witcher_sword.item(),
                AdvancementFrame.GOAL,
                true, true, false, null,
                null,
                new TagKey[]{ ModItemTags.STEEL_SWORDS },
                null
        ));

    }

    public WitcherVanillaAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    private final java.util.Map<Identifier, AdvancementEntry> built = new java.util.HashMap<>();

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {
        for (Entry entry : entries) {
            AdvancementEntry builtEntry = generateAdvancementEntry(entry, consumer);
            built.put(entry.id(), builtEntry);
        }
    }

    private AdvancementEntry generateAdvancementEntry(Entry entry, Consumer<AdvancementEntry> consumer) {
        Item iconItem = entry.iconItem() != null ? entry.iconItem() : Items.BARRIER;

        var builder = Advancement.Builder.create()
                .display(
                        iconItem,
                        Text.translatable(entry.titleKey()),
                        Text.translatable(entry.descriptionKey()),
                        entry.background() != null ? Identifier.tryParse(entry.background()) : null,
                        entry.frame(),
                        entry.showToast(),
                        entry.announceToChat(),
                        entry.hidden()
                );

        var predicates = new ArrayList<ItemPredicate>();

        if (entry.requiredItems() != null) {
            for (Item item : entry.requiredItems()) {
                predicates.add(ItemPredicate.Builder.create().items(item).build());
            }
        }

        if (entry.requiredItemTags() != null) {
            for (TagKey<Item> tag : entry.requiredItemTags()) {
                predicates.add(ItemPredicate.Builder.create().tag(tag).build());
            }
        }

        builder.criterion(
                "has_required_items",
                InventoryChangedCriterion.Conditions.items(
                        predicates.toArray(ItemPredicate[]::new)
                )
        );


        if (entry.parent() != null) {
            @SuppressWarnings("deprecation")
            var builderWithParent = builder.parent(entry.parent());
            builder = builderWithParent;
        }

        if (entry.experienceReward() != null) {
            builder.rewards(AdvancementRewards.Builder.experience(entry.experienceReward()));
        }

        AdvancementEntry builtEntry = builder.build(consumer, entry.id().toString());
        consumer.accept(builtEntry);
        return builtEntry;
    }

    public static List<Entry> getEntries() {
        return entries;
    }
}
