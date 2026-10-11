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

public class GemCutCraftingEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final EmiStack baseInput;
    private final EmiStack catalystInput;
    private final EmiStack output;

    public GemCutCraftingEmiRecipe(Identifier id, Item baseItem, Item catalyst, RegistryEntry<GemCut> cut) {
        this.id = id;
        this.baseInput = EmiStack.of(baseItem);
        this.catalystInput = EmiStack.of(catalyst);
        var outputStack = new ItemStack(baseItem);
        outputStack.set(GemComponents.CUT, cut);
        this.output = EmiStack.of(outputStack);
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
        return List.of(baseInput, catalystInput);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 77;
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 41, 1);
        widgets.addSlot(baseInput, 0, 0);
        widgets.addSlot(catalystInput, 19, 0);
        widgets.addSlot(output, 59, 0).recipeContext(this);
    }
}
