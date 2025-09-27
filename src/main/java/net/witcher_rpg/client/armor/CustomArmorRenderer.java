package net.witcher_rpg.client.armor;

import mod.azure.azurelibarmor.rewrite.render.AzRendererConfig;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class CustomArmorRenderer extends AzArmorRenderer {
    public CustomArmorRenderer(AzRendererConfig<ItemStack> config) {
        super(config);
    }

    public static CustomArmorRenderer kaer_morhen() {
        return new CustomArmorRenderer("kaer_morhen_armor", "kaer_morhen_armor");
    }
    public static CustomArmorRenderer feline() {
        return new CustomArmorRenderer("feline_armor", "feline_armor");
    }
    public static CustomArmorRenderer enhanced_feline() {
        return new CustomArmorRenderer("feline_armor", "enhanced_feline_armor");
    }
    public static CustomArmorRenderer superior_feline() {
        return new CustomArmorRenderer("feline_armor", "superior_feline_armor");
    }
    public static CustomArmorRenderer mastercrafted_feline() {
        return new CustomArmorRenderer("feline_armor", "mastercrafted_feline_armor");
    }
    public static CustomArmorRenderer grandmaster_feline() {
        return new CustomArmorRenderer("feline_armor", "grandmaster_feline_armor");
    }
    public static CustomArmorRenderer griffin() {
        return new CustomArmorRenderer("griffin_armor", "griffin_armor");
    }
    public static CustomArmorRenderer enhanced_griffin() {
        return new CustomArmorRenderer("griffin_armor", "enhanced_griffin_armor");
    }
    public static CustomArmorRenderer superior_griffin() {
        return new CustomArmorRenderer("griffin_armor", "superior_griffin_armor");
    }
    public static CustomArmorRenderer mastercrafted_griffin() {
        return new CustomArmorRenderer("griffin_armor", "mastercrafted_griffin_armor");
    }
    public static CustomArmorRenderer grandmaster_griffin() {
        return new CustomArmorRenderer("griffin_armor", "grandmaster_griffin_armor");
    }
    public static CustomArmorRenderer ursine() {
        return new CustomArmorRenderer("ursine_armor", "ursine_armor");
    }
    public static CustomArmorRenderer enhanced_ursine() {
        return new CustomArmorRenderer("ursine_armor", "enhanced_ursine_armor");
    }
    public static CustomArmorRenderer superior_ursine() {
        return new CustomArmorRenderer("ursine_armor", "superior_ursine_armor");
    }
    public static CustomArmorRenderer mastercrafted_ursine() {
        return new CustomArmorRenderer("ursine_armor", "mastercrafted_ursine_armor");
    }
    public static CustomArmorRenderer grandmaster_ursine() {
        return new CustomArmorRenderer("ursine_armor", "grandmaster_ursine_armor");
    }
    public static CustomArmorRenderer wolven() {
        return new CustomArmorRenderer("wolven_armor", "wolven_armor");
    }
    public static CustomArmorRenderer enhanced_wolven() {
        return new CustomArmorRenderer("wolven_armor", "enhanced_wolven_armor");
    }
    public static CustomArmorRenderer superior_wolven() {
        return new CustomArmorRenderer("wolven_armor", "superior_wolven_armor");
    }
    public static CustomArmorRenderer mastercrafted_wolven() {
        return new CustomArmorRenderer("wolven_armor", "mastercrafted_wolven_armor");
    }
    public static CustomArmorRenderer grandmaster_wolven() {
        return new CustomArmorRenderer("wolven_armor", "grandmaster_wolven_armor");
    }


    public CustomArmorRenderer(String modelName, String textureName) {
        super(AzArmorRendererConfig.builder(
                Identifier.of(MOD_ID, "geo/" + modelName + ".geo.json"),
                Identifier.of(MOD_ID, "textures/armor/" + textureName + ".png")
        ).build());
    }
}
