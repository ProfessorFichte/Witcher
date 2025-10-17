package net.witcher_rpg.spell;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.set.EquipmentSet;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import net.witcher_rpg.item.WitcherTrinkets;
import net.witcher_rpg.item.armor.Armors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class SetBonuses {
    private static final String NAMESPACE = MOD_ID;
    public record Entry(Identifier id, String title, Supplier<List<Identifier>> itemSupplier, List<EquipmentSet.Bonus> bonuses) { }
    public static final List<Entry> all = new ArrayList<>();
    private static Entry add(Entry entry) {
        all.add(entry);
        return entry;
    }

    private static AttributeModifiersComponent attribute(RegistryEntry<EntityAttribute> attribute, double value, EntityAttributeModifier.Operation operation, Identifier id) {
        return new AttributeModifiersComponent(
                List.of(
                        new AttributeModifiersComponent.Entry(
                                attribute,
                                new EntityAttributeModifier(
                                        id,
                                        value,
                                        operation
                                ),
                                AttributeModifierSlot.ARMOR)
                ),
                true
        );
    }
    public static float felineAttributeBonus = 0.05F;
    public static float griffinAttributeBonus = 0.1F;
    public static float ursineAttributeBonus = 8.0F;
    public static float wolvenAttributeBonus = 0.05F;
    static Supplier<List<Identifier>> mastercraftedFelineItems = () -> {
        List<Identifier> items = new ArrayList<>(Armors.mastercraftedFelineSchoolArmorSet.pieceIds());
        return items;
    };
    static Supplier<List<Identifier>> grandmasterFelineItems = () -> {
        List<Identifier> items = new ArrayList<>(Armors.grandmasterFelineSchoolArmorSet.pieceIds());
        items.add(WitcherTrinkets.CAT_SCHOOL_MEDALLION.id());
        return items;
    };
    static Supplier<List<Identifier>> mastercraftedGriffinItems = () -> {
        List<Identifier> items = new ArrayList<>(Armors.mastercraftedGriffinArmorSet.pieceIds());
        return items;
    };
    static Supplier<List<Identifier>> grandmasterGriffinItems = () -> {
        List<Identifier> items = new ArrayList<>(Armors.grandmasterGriffinArmorSet.pieceIds());
        items.add(WitcherTrinkets.GRIFFIN_SCHOOL_MEDALLION.id());
        return items;
    };
    static Supplier<List<Identifier>> mastercraftedUrsineItems = () -> {
        List<Identifier> items = new ArrayList<>(Armors.mastercraftedUrsineArmorSet.pieceIds());
        return items;
    };
    static Supplier<List<Identifier>> grandmasterUrsineItems = () -> {
        List<Identifier> items = new ArrayList<>(Armors.grandmasterUrsineArmorSet.pieceIds());
        items.add(WitcherTrinkets.BEAR_SCHOOL_MEDALLION.id());
        return items;
    };
    static Supplier<List<Identifier>> mastercraftedWolvenItems = () -> {
        List<Identifier> items = new ArrayList<>(Armors.mastercraftedWolvenArmorSet.pieceIds());
        return items;
    };
    static Supplier<List<Identifier>> grandmasterWolvenItems = () -> {
        List<Identifier> items = new ArrayList<>(Armors.grandmasterWolvenArmorSet.pieceIds());
        items.add(WitcherTrinkets.WOLF_SCHOOL_MEDALLION.id());
        return items;
    };
    public static Entry mastercrafted_feline = add(mastercrafted_feline());
    private static Entry mastercrafted_feline() {
        var id = Identifier.of(NAMESPACE, "mastercrafted_feline");
        return new Entry(id,
                "Feline School Master",
                mastercraftedFelineItems,
                List.of(
                        EquipmentSet.Bonus.withAttributes(2, attribute(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                felineAttributeBonus/2,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                                id)
                        ),
                        EquipmentSet.Bonus.withSpells(4, SpellContainerHelper.createForModifier(WitcherSpells.improved_whirl.id()))
                )
        );
    }
    public static Entry grandmaster_feline = add(grandmaster_feline());
    private static Entry grandmaster_feline() {
        var id = Identifier.of(NAMESPACE, "grandmaster_feline");
        return new Entry(id,
                "Feline School Grandmaster",
                grandmasterFelineItems,
                List.of(
                        EquipmentSet.Bonus.withAttributes(2, attribute(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                felineAttributeBonus,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                                id)
                        ),
                        EquipmentSet.Bonus.withSpells(4, SpellContainerHelper.createForModifier(WitcherSpells.improved_whirl.id())),
                        EquipmentSet.Bonus.withSpells(5, SpellContainerHelper.createForModifier(WitcherSpells.grandmaster_feline.id()))
                )
        );
    }
    public static Entry mastercrafted_griffin = add(mastercrafted_griffin());
    private static Entry mastercrafted_griffin() {
        var id = Identifier.of(NAMESPACE, "mastercrafted_griffin");
        return new Entry(id,
                "Griffin School Master",
                mastercraftedGriffinItems,
                List.of(
                        EquipmentSet.Bonus.withAttributes(2, attribute(
                                WitcherAttributes.SIGN_INTENSITY,
                                griffinAttributeBonus /2,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                                id)
                        ),
                        EquipmentSet.Bonus.withSpells(4, SpellContainerHelper.createForModifier(WitcherSpells.improved_yrden.id()))
                )
        );
    }
    public static Entry grandmaster_griffin = add(grandmaster_griffin());
    private static Entry grandmaster_griffin() {
        var id = Identifier.of(NAMESPACE, "grandmaster_griffin");
        return new Entry(id,
                "Griffin School Grandmaster",
                grandmasterGriffinItems,
                List.of(
                        EquipmentSet.Bonus.withAttributes(2, attribute(
                                WitcherAttributes.SIGN_INTENSITY,
                                griffinAttributeBonus,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                                id)
                        ),
                        EquipmentSet.Bonus.withSpells(4, SpellContainerHelper.createForModifier(WitcherSpells.improved_yrden.id())),
                        EquipmentSet.Bonus.withSpells(5, SpellContainerHelper.createForModifier(WitcherSpells.grandmaster_griffin.id()))
                )
        );
    }
    public static Entry mastercrafted_wolven= add(mastercrafted_wolven());
    private static Entry mastercrafted_wolven() {
        var id = Identifier.of(NAMESPACE, "mastercrafted_wolven");
        return new Entry(id,
                "Wolven School Master",
                mastercraftedWolvenItems,
                List.of(
                        EquipmentSet.Bonus.withAttributes(2, attribute(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                wolvenAttributeBonus /2,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                                id)
                        ),
                        EquipmentSet.Bonus.withSpells(4, SpellContainerHelper.createForModifier(WitcherSpells.improved_aard.id()))
                )
        );
    }
    public static Entry grandmaster_wolven = add(grandmaster_wolven());
    private static Entry grandmaster_wolven() {
        var id = Identifier.of(NAMESPACE, "grandmaster_wolven");
        return new Entry(id,
                "Wolven School Grandmaster",
                grandmasterWolvenItems,
                List.of(
                        EquipmentSet.Bonus.withAttributes(2, attribute(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                wolvenAttributeBonus,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                                id)
                        ),
                        EquipmentSet.Bonus.withSpells(4, SpellContainerHelper.createForModifier(WitcherSpells.improved_aard.id())),
                        EquipmentSet.Bonus.withSpells(5, SpellContainerHelper.createForModifier(WitcherSpells.grandmaster_wolven.id()))
                )
        );
    }
    public static Entry mastercrafted_ursine= add(mastercrafted_ursine());
    private static Entry mastercrafted_ursine() {
        var id = Identifier.of(NAMESPACE, "mastercrafted_ursine");
        return new Entry(id,
                "Ursine School Master",
                mastercraftedUrsineItems,
                List.of(
                        EquipmentSet.Bonus.withAttributes(2, attribute(
                                EntityAttributes.GENERIC_MAX_HEALTH,
                                ursineAttributeBonus /2,
                                EntityAttributeModifier.Operation.ADD_VALUE,
                                id)
                        ),
                        EquipmentSet.Bonus.withSpells(4, SpellContainerHelper.createForModifier(WitcherSpells.improved_rend.id()))
                )
        );
    }
    public static Entry grandmaster_ursine = add(grandmaster_ursine());
    private static Entry grandmaster_ursine() {
        var id = Identifier.of(NAMESPACE, "grandmaster_ursine");
        return new Entry(id,
                "Ursine School Grandmaster",
                grandmasterUrsineItems,
                List.of(
                        EquipmentSet.Bonus.withAttributes(2, attribute(
                                EntityAttributes.GENERIC_MAX_HEALTH,
                                ursineAttributeBonus,
                                EntityAttributeModifier.Operation.ADD_VALUE,
                                id)
                        ),
                        EquipmentSet.Bonus.withSpells(4, SpellContainerHelper.createForModifier(WitcherSpells.improved_rend.id())),
                        EquipmentSet.Bonus.withSpells(5, SpellContainerHelper.createForModifier(WitcherSpells.grandmaster_ursine.id()))
                )
        );
    }

}
