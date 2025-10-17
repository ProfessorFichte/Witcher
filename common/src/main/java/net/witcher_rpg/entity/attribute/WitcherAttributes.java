package net.witcher_rpg.entity.attribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;


public class WitcherAttributes {
    public static final RegistryEntry<EntityAttribute> SIGN_INTENSITY = register("sign_intensity", 0.0, 0.0, 1024.0);
    public static final RegistryEntry<EntityAttribute> ADRENALINE_MODIFIER= register("adrenaline_modifier", 100.0, 100.0, 1024.0);
    public static final RegistryEntry<EntityAttribute> AARD_INTENSITY = register("aard_intensity", 0.0, 0.0, 1024.0);
    public static final RegistryEntry<EntityAttribute> AXII_INTENSITY = register("axii_intensity", 0.0, 0.0, 1024.0);
    public static final RegistryEntry<EntityAttribute> IGNI_INTENSITY = register("igni_intensity", 0.0, 0.0, 1024.0);
    public static final RegistryEntry<EntityAttribute> QUEN_INTENSITY = register("quen_intensity", 0.0, 0.0, 1024.0);
    public static final RegistryEntry<EntityAttribute> YRDEN_INTENSITY = register("yrden_intensity", 0.0, 0.0, 1024.0);


    private static RegistryEntry<EntityAttribute> register(final String name, double base, double min, double max) {
        EntityAttribute attribute = new ClampedEntityAttribute("attribute.name." + MOD_ID + '.' + name, base, min, max).setTracked(true);
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, name), attribute);
    }

    public static void registerAttributes(){
    }

}
