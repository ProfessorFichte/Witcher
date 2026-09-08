package net.witcher_rpg.util;

/// More RPG Library attribute ids as compile-time `String` constants.
///
/// `MRPGCEntityAttributes` registers its attributes from **static field initializers**, so merely naming
/// one of its fields runs `Registry.register` — which on Forge 47 throws *"Can not register to a locked
/// registry"* unless it happens inside the `ATTRIBUTE` `RegisterEvent` window. Witcher only ever needs the
/// id *string* (config-shaped attribute modifiers), and constants like these are inlined by javac, so
/// referencing them never touches the library class at all.
public class MrpgAttributeIds {
    private static final String NAMESPACE = "more_rpg_classes:";

    public static final String LIFESTEAL_MODIFIER = NAMESPACE + "lifesteal_modifier";
    public static final String BURNING_CHANCE = NAMESPACE + "burning_chance";
    public static final String STAGGER_CHANCE = NAMESPACE + "stagger_chance";
    public static final String STUN_CHANCE = NAMESPACE + "stun_chance";
    public static final String POISON_CHANCE = NAMESPACE + "poison_chance";
    public static final String FREEZE_CHANCE = NAMESPACE + "freeze_chance";
    public static final String BLEEDING_CHANCE = NAMESPACE + "bleeding_chance";
    public static final String ARMOR_PIERCING = NAMESPACE + "armor_piercing";
}
