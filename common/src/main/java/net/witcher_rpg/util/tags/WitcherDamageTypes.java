package net.witcher_rpg.util.tags;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.witcher_rpg.WitcherClassMod;

public class WitcherDamageTypes {
    public static final TagKey<DamageType> SILVER = register("silver");

    public static final TagKey<DamageType> AARD = register("aard");
    public static final TagKey<DamageType> AXII = register("axii");
    public static final TagKey<DamageType> IGNI = register("igni");
    public static final TagKey<DamageType> YRDEN = register("yrden");
    public static final TagKey<DamageType> QUEN = register("quen");
    public static final TagKey<DamageType> SIGNS = register("signs");

    public RegistryKey<DamageType> MAGIC = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(WitcherClassMod.MOD_ID,"magic"));

    private static TagKey<DamageType> register(String id) {
        return TagKey.of(RegistryKeys.DAMAGE_TYPE, WitcherClassMod.id(id));
    }

}
