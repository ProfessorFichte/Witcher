package net.witcher_rpg.util;

// Plain string ids on purpose, touching MRPGCEntityAttributes registers its attributes and throws on Forge 47 outside the ATTRIBUTE RegisterEvent.
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
