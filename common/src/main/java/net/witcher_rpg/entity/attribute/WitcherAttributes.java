package net.witcher_rpg.entity.attribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;


public class WitcherAttributes {
    public static final Identifier SIGN_INTENSITY_ID = new Identifier(MOD_ID, "sign_intensity");
    public static final Identifier ADRENALINE_MODIFIER_ID = new Identifier(MOD_ID, "adrenaline_modifier");
    public static final Identifier AARD_INTENSITY_ID = new Identifier(MOD_ID, "aard_intensity");
    public static final Identifier AXII_INTENSITY_ID = new Identifier(MOD_ID, "axii_intensity");
    public static final Identifier IGNI_INTENSITY_ID = new Identifier(MOD_ID, "igni_intensity");
    public static final Identifier QUEN_INTENSITY_ID = new Identifier(MOD_ID, "quen_intensity");
    public static final Identifier YRDEN_INTENSITY_ID = new Identifier(MOD_ID, "yrden_intensity");

    public static EntityAttribute SIGN_INTENSITY;
    public static EntityAttribute ADRENALINE_MODIFIER;
    public static EntityAttribute AARD_INTENSITY;
    public static EntityAttribute AXII_INTENSITY;
    public static EntityAttribute IGNI_INTENSITY;
    public static EntityAttribute QUEN_INTENSITY;
    public static EntityAttribute YRDEN_INTENSITY;

    private static EntityAttribute create(final Identifier id, double base, double min, double max) {
        return new ClampedEntityAttribute("attribute.name." + id.getNamespace() + '.' + id.getPath(), base, min, max).setTracked(true);
    }

    private static boolean created = false;

    public static Map<Identifier, EntityAttribute> attributesToRegister() {
        if (created) {
            return Map.of();
        }
        created = true;
        var map = new LinkedHashMap<Identifier, EntityAttribute>();
        map.put(SIGN_INTENSITY_ID, SIGN_INTENSITY = create(SIGN_INTENSITY_ID, 0.0, 0.0, 1024.0));
        map.put(ADRENALINE_MODIFIER_ID, ADRENALINE_MODIFIER = create(ADRENALINE_MODIFIER_ID, 100.0, 100.0, 1024.0));
        map.put(AARD_INTENSITY_ID, AARD_INTENSITY = create(AARD_INTENSITY_ID, 0.0, 0.0, 1024.0));
        map.put(AXII_INTENSITY_ID, AXII_INTENSITY = create(AXII_INTENSITY_ID, 0.0, 0.0, 1024.0));
        map.put(IGNI_INTENSITY_ID, IGNI_INTENSITY = create(IGNI_INTENSITY_ID, 0.0, 0.0, 1024.0));
        map.put(QUEN_INTENSITY_ID, QUEN_INTENSITY = create(QUEN_INTENSITY_ID, 0.0, 0.0, 1024.0));
        map.put(YRDEN_INTENSITY_ID, YRDEN_INTENSITY = create(YRDEN_INTENSITY_ID, 0.0, 0.0, 1024.0));
        return map;
    }

    public static void registerAttributes() {
        attributesToRegister().forEach((id, attribute) -> Registry.register(Registries.ATTRIBUTE, id, attribute));
    }
}
