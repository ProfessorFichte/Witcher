package net.witcher_rpg.item.misc;

public class OldWitcherArmorDiagram {
    /*
    public static final List<Identifier> BASE_ITEMS = Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(Identifier.of("item/empty_slot_sword"));
                identifiers.add(Identifier.of("item/empty_armor_slot_helmet"));
                identifiers.add(Identifier.of("item/empty_armor_slot_chestplate"));
                identifiers.add(Identifier.of("item/empty_armor_slot_leggings"));
                identifiers.add(Identifier.of("empty_armor_slot_boots"));
            });
    public static final List<Identifier> INGREDIENT_ITEMS_WITCHER_ARMOR = Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(Identifier.of("item/empty_slot_ingot"));
            });

    ///ENHANCED WITCHER GEAR
    public static Item ENHANCED_FELINE_SCHOOL_DIAGRAM = new SmithingTemplateItem(
            Text.translatable("smithing_template.witcher_rpg.enhanced_feline.applies_to").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_feline.ingredients").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_feline.title").formatted(Formatting.GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_feline.base_slot_description"),
            Text.translatable("smithing_template.witcher_rpg.enhanced_feline.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_WITCHER_ARMOR
    );

    public static Item ENHANCED_GRIFFIN_SCHOOL_DIAGRAM = new SmithingTemplateItem(
            Text.translatable("smithing_template.witcher_rpg.enhanced_griffin.applies_to").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_griffin.ingredients").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_griffin.title").formatted(Formatting.GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_griffin.base_slot_description"),
            Text.translatable("smithing_template.witcher_rpg.enhanced_griffin.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_WITCHER_ARMOR
    );

    public static Item ENHANCED_WOLVEN_SCHOOL_DIAGRAM = new SmithingTemplateItem(
            Text.translatable("smithing_template.witcher_rpg.enhanced_wolven.applies_to").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_wolven.ingredients").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_wolven.title").formatted(Formatting.GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_wolven.base_slot_description"),
            Text.translatable("smithing_template.witcher_rpg.enhanced_wolven.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_WITCHER_ARMOR
    );

    public static Item ENHANCED_URSINE_SCHOOL_DIAGRAM = new SmithingTemplateItem(
            Text.translatable("smithing_template.witcher_rpg.enhanced_ursine.applies_to").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_ursine.ingredients").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_ursine.title").formatted(Formatting.GREEN),
            Text.translatable("smithing_template.witcher_rpg.enhanced_ursine.base_slot_description"),
            Text.translatable("smithing_template.witcher_rpg.enhanced_ursine.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_WITCHER_ARMOR
    );

    ///SUPERIOR WITCHER GEAR
    public static Item SUPERIOR_FELINE_SCHOOL_DIAGRAM = new SmithingTemplateItem(
            Text.translatable("smithing_template.witcher_rpg.superior_feline.applies_to").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_feline.ingredients").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_feline.title").formatted(Formatting.GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_feline.base_slot_description"),
            Text.translatable("smithing_template.witcher_rpg.superior_feline.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_WITCHER_ARMOR
    );

    public static Item SUPERIOR_GRIFFIN_SCHOOL_DIAGRAM = new SmithingTemplateItem(
            Text.translatable("smithing_template.witcher_rpg.superior_griffin.applies_to").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_griffin.ingredients").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_griffin.title").formatted(Formatting.GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_griffin.base_slot_description"),
            Text.translatable("smithing_template.witcher_rpg.superior_griffin.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_WITCHER_ARMOR
    );

    public static Item SUPERIOR_WOLVEN_SCHOOL_DIAGRAM = new SmithingTemplateItem(
            Text.translatable("smithing_template.witcher_rpg.superior_wolven.applies_to").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_wolven.ingredients").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_wolven.title").formatted(Formatting.GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_wolven.base_slot_description"),
            Text.translatable("smithing_template.witcher_rpg.superior_wolven.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_WITCHER_ARMOR
    );

    public static Item SUPERIOR_URSINE_SCHOOL_DIAGRAM = new SmithingTemplateItem(
            Text.translatable("smithing_template.witcher_rpg.superior_ursine.applies_to").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_ursine.ingredients").formatted(Formatting.DARK_GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_ursine.title").formatted(Formatting.GREEN),
            Text.translatable("smithing_template.witcher_rpg.superior_ursine.base_slot_description"),
            Text.translatable("smithing_template.witcher_rpg.superior_ursine.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_WITCHER_ARMOR
    );

    public static void registerWitcherArmorDiagrams(){
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"enhanced_feline_diagram"),ENHANCED_FELINE_SCHOOL_DIAGRAM);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"enhanced_griffin_diagram"),ENHANCED_GRIFFIN_SCHOOL_DIAGRAM);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"enhanced_wolven_diagram"),ENHANCED_WOLVEN_SCHOOL_DIAGRAM);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"enhanced_ursine_diagram"),ENHANCED_URSINE_SCHOOL_DIAGRAM);

        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"superior_feline_diagram"),SUPERIOR_FELINE_SCHOOL_DIAGRAM);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"superior_griffin_diagram"),SUPERIOR_GRIFFIN_SCHOOL_DIAGRAM);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"superior_wolven_diagram"),SUPERIOR_WOLVEN_SCHOOL_DIAGRAM);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"superior_ursine_diagram"),SUPERIOR_URSINE_SCHOOL_DIAGRAM);

        ItemGroupEvents.modifyEntriesEvent(WitcherGroup.WITCHER_KEY).register((content) -> {
            content.add(ENHANCED_FELINE_SCHOOL_DIAGRAM);
            content.add(ENHANCED_GRIFFIN_SCHOOL_DIAGRAM);
            content.add(ENHANCED_WOLVEN_SCHOOL_DIAGRAM);
            content.add(ENHANCED_URSINE_SCHOOL_DIAGRAM);

            content.add(SUPERIOR_FELINE_SCHOOL_DIAGRAM);
            content.add(SUPERIOR_GRIFFIN_SCHOOL_DIAGRAM);
            content.add(SUPERIOR_WOLVEN_SCHOOL_DIAGRAM);
            content.add(SUPERIOR_URSINE_SCHOOL_DIAGRAM);
        });
    }
*/
}
