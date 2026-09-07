package net.witcher_rpg.client.armor;

import net.minecraft.util.Identifier;
import net.rpg_foundation.armor_api.client.GeoArmorRenderer;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public final class CustomArmorRenderer {
    private CustomArmorRenderer() { }

    public static GeoArmorRenderer kaer_morhen() {
        return make("kaer_morhen_armor", "kaer_morhen_armor");
    }
    public static GeoArmorRenderer feline() {
        return make("feline_armor", "feline_armor");
    }
    public static GeoArmorRenderer enhanced_feline() {
        return make("feline_armor", "enhanced_feline_armor");
    }
    public static GeoArmorRenderer superior_feline() {
        return make("feline_armor", "superior_feline_armor");
    }
    public static GeoArmorRenderer mastercrafted_feline() {
        return make("feline_armor", "mastercrafted_feline_armor");
    }
    public static GeoArmorRenderer grandmaster_feline() {
        return make("feline_armor", "grandmaster_feline_armor");
    }
    public static GeoArmorRenderer griffin() {
        return make("griffin_armor", "griffin_armor");
    }
    public static GeoArmorRenderer enhanced_griffin() {
        return make("griffin_armor", "enhanced_griffin_armor");
    }
    public static GeoArmorRenderer superior_griffin() {
        return make("griffin_armor", "superior_griffin_armor");
    }
    public static GeoArmorRenderer mastercrafted_griffin() {
        return make("griffin_armor", "mastercrafted_griffin_armor");
    }
    public static GeoArmorRenderer grandmaster_griffin() {
        return make("griffin_armor", "grandmaster_griffin_armor");
    }
    public static GeoArmorRenderer ursine() {
        return make("ursine_armor", "ursine_armor");
    }
    public static GeoArmorRenderer enhanced_ursine() {
        return make("ursine_armor", "enhanced_ursine_armor");
    }
    public static GeoArmorRenderer superior_ursine() {
        return make("ursine_armor", "superior_ursine_armor");
    }
    public static GeoArmorRenderer mastercrafted_ursine() {
        return make("ursine_armor", "mastercrafted_ursine_armor");
    }
    public static GeoArmorRenderer grandmaster_ursine() {
        return make("ursine_armor", "grandmaster_ursine_armor");
    }
    public static GeoArmorRenderer wolven() {
        return make("wolven_armor", "wolven_armor");
    }
    public static GeoArmorRenderer enhanced_wolven() {
        return make("wolven_armor", "enhanced_wolven_armor");
    }
    public static GeoArmorRenderer superior_wolven() {
        return make("wolven_armor", "superior_wolven_armor");
    }
    public static GeoArmorRenderer mastercrafted_wolven() {
        return make("wolven_armor", "mastercrafted_wolven_armor");
    }
    public static GeoArmorRenderer grandmaster_wolven() {
        return make("wolven_armor", "grandmaster_wolven_armor");
    }

    private static GeoArmorRenderer make(String modelName, String textureName) {
        return GeoArmorRenderer.of(
                Identifier.of(MOD_ID, "geo/" + modelName + ".geo.json"),
                Identifier.of(MOD_ID, "textures/armor/" + textureName + ".png")
        );
    }
}
