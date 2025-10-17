package net.witcher_rpg.util.tags;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.witcher_rpg.WitcherClassMod;

public class WitcherEntityTags {

    public static final TagKey<EntityType<?>> SILVER_VULNERABLE = register("silver_vulnerable");
    public static final TagKey<EntityType<?>> IGNI_VULNERABLE = register("igni_vulnerable");
    public static final TagKey<EntityType<?>> YRDEN_VULNERABLE = register("yrden_vulnerable");
    public static final TagKey<EntityType<?>> AARD_VULNERABLE = register("aard_vulnerable");
    public static final TagKey<EntityType<?>> AXII_VULNERABLE = register("axii_vulnerable");
    public static final TagKey<EntityType<?>> QUEN_VULNERABLE = register("quen_vulnerable");
    public static final TagKey<EntityType<?>> AXII_EFFECT_IMMUNE = register("axii_effect_immune");

    private static TagKey<EntityType<?>> register(String id) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, WitcherClassMod.id(id));
    }
}
