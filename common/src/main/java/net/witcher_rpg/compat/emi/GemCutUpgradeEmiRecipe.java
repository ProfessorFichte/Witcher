package net.witcher_rpg.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.jewelry.gems.GemComponents;
import net.jewelry.gems.GemCut;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.List;

public class GemCutUpgradeEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final EmiStack input;
    private final EmiStack output;
    private final List<EmiIngredient> catalysts;

    public GemCutUpgradeEmiRecipe(Identifier id, Item fromItem, RegistryEntry<GemCut> fromCut, Item toItem, RegistryEntry<GemCut> toCut) {
        this.id = id;
        var inputStack = new ItemStack(fromItem);
        inputStack.set(GemComponents.CUT, fromCut);
        var outputStack = new ItemStack(toItem);
        outputStack.set(GemComponents.CUT, toCut);
        this.input = EmiStack.of(inputStack);
        this.output = EmiStack.of(outputStack);
        this.catalysts = List.of(EmiStack.of(fromItem));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return WitcherEmiPlugin.CATEGORY;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input, input, input, input);
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return catalysts;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 76;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 10);
        widgets.addSlot(input, 0, 0);
        widgets.addSlot(input, 19, 0);
        widgets.addSlot(input, 0, 19);
        widgets.addSlot(input, 19, 19);
        widgets.addSlot(output, 58, 10).recipeContext(this);
    }
}
