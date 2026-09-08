package net.witcher_rpg.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.spell_engine.rpg_series.item.Armor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/// 1.20.1 stand-in for `net.more_rpg_classes.datagen.SmithingRecipeGenerator`.
///
/// The library class ships on the 1.20.1 line but is still written for the 1.21 datapack format: it
/// resolves into `data/<ns>/recipe/` (singular), emits `"result": {"id": …}` and tags conditions with
/// `neoforge:conditions`. Nothing inside the library exercises it, so the port never caught it. Rather
/// than change a library from a content-mod port, the generator is reproduced here with the 1.20.1
/// spellings — `recipes/`, `"result": {"item": …}` and Forge 47's plain top-level `conditions` array —
/// keeping the exact same call surface and the exact same recipe ids.
///
/// **Delete this class and go back to extending the library once More RPG Library ships `2.7.2.002`.**
public abstract class WitcherSmithingRecipeBase implements DataProvider {

    protected final FabricDataOutput output;
    protected final String modId;
    private final List<RecipeData> recipes = new ArrayList<>();

    public WitcherSmithingRecipeBase(FabricDataOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    public abstract void generate();

    @Override
    public String getName() {
        return "Smithing Recipes (" + modId + ")";
    }

    // MARK: Recipes with mod load conditions

    public void createSmithingTransformRecipe(String name, Item base, Object template, Object addition,
                                              Item result, String requiredMod) {
        createSmithingTransformRecipe(name, base, template, addition, result, new String[]{requiredMod});
    }

    public void createSmithingTransformRecipe(String name, Item base, Object template, Object addition,
                                              Item result, String[] requiredMods) {
        recipes.add(new RecipeData(name, base, template, addition, result, requiredMods, true));
    }

    public void createArmorSetUpgrade(String recipeBaseName, Armor.Set baseSet, Object template, Object addition,
                                      Armor.Set resultSet, String requiredMod) {
        createArmorSetUpgrade(recipeBaseName, baseSet, template, addition, resultSet, new String[]{requiredMod});
    }

    public void createArmorSetUpgrade(String recipeBaseName, Armor.Set baseSet, Object template, Object addition,
                                      Armor.Set resultSet, String[] requiredMods) {
        var resultSetName = extractArmorSetName(resultSet);
        createSmithingTransformRecipe(recipeBaseName + "_" + resultSetName + "_head", (Item) baseSet.head, template, addition, (Item) resultSet.head, requiredMods);
        createSmithingTransformRecipe(recipeBaseName + "_" + resultSetName + "_chest", (Item) baseSet.chest, template, addition, (Item) resultSet.chest, requiredMods);
        createSmithingTransformRecipe(recipeBaseName + "_" + resultSetName + "_legs", (Item) baseSet.legs, template, addition, (Item) resultSet.legs, requiredMods);
        createSmithingTransformRecipe(recipeBaseName + "_" + resultSetName + "_feet", (Item) baseSet.feet, template, addition, (Item) resultSet.feet, requiredMods);
    }

    // MARK: Recipes without mod load conditions

    public void createSimpleSmithingRecipe(String name, Item base, Object template, Object addition, Item result) {
        recipes.add(new RecipeData(name, base, template, addition, result, null, false));
    }

    public void createSimpleArmorSetUpgrade(String recipeBaseName, Armor.Set baseSet, Object template,
                                            Object addition, Armor.Set resultSet) {
        var resultSetName = extractArmorSetName(resultSet);
        createSimpleSmithingRecipe(recipeBaseName + "_" + resultSetName + "_head", (Item) baseSet.head, template, addition, (Item) resultSet.head);
        createSimpleSmithingRecipe(recipeBaseName + "_" + resultSetName + "_chest", (Item) baseSet.chest, template, addition, (Item) resultSet.chest);
        createSimpleSmithingRecipe(recipeBaseName + "_" + resultSetName + "_legs", (Item) baseSet.legs, template, addition, (Item) resultSet.legs);
        createSimpleSmithingRecipe(recipeBaseName + "_" + resultSetName + "_feet", (Item) baseSet.feet, template, addition, (Item) resultSet.feet);
    }

    private String extractArmorSetName(Armor.Set armorSet) {
        var path = Registries.ITEM.getId((Item) armorSet.head).getPath();
        return path.endsWith("_head") ? path.substring(0, path.length() - 5) : path;
    }

    // MARK: Internal

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        generate();
        return CompletableFuture.allOf(recipes.stream().map(recipeData -> {
            JsonObject recipe = buildRecipeJson(recipeData);
            // 1.20.1 datapack directory is plural
            Path path = output.getResolver(DataOutput.OutputType.DATA_PACK, "recipes")
                    .resolveJson(new Identifier(modId, recipeData.name));
            return DataProvider.writeToPath(writer, recipe, path);
        }).toArray(CompletableFuture[]::new));
    }

    private JsonObject buildRecipeJson(RecipeData data) {
        JsonObject recipe = new JsonObject();

        if (data.withLoadConditions && data.requiredMods != null && data.requiredMods.length > 0) {
            JsonArray fabricLoadConditions = new JsonArray();
            JsonObject fabricCondition = new JsonObject();
            fabricCondition.addProperty("condition", "fabric:all_mods_loaded");
            JsonArray modValues = new JsonArray();
            for (String mod : data.requiredMods) {
                modValues.add(mod);
            }
            fabricCondition.add("values", modValues);
            fabricLoadConditions.add(fabricCondition);
            recipe.add("fabric:load_conditions", fabricLoadConditions);

            JsonArray forgeConditions = new JsonArray();
            if (data.requiredMods.length == 1) {
                JsonObject condition = new JsonObject();
                condition.addProperty("type", "forge:mod_loaded");
                condition.addProperty("modid", data.requiredMods[0]);
                forgeConditions.add(condition);
            } else {
                JsonObject andCondition = new JsonObject();
                andCondition.addProperty("type", "forge:and");
                JsonArray innerConditions = new JsonArray();
                for (String mod : data.requiredMods) {
                    JsonObject modCondition = new JsonObject();
                    modCondition.addProperty("type", "forge:mod_loaded");
                    modCondition.addProperty("modid", mod);
                    innerConditions.add(modCondition);
                }
                andCondition.add("conditions", innerConditions);
                forgeConditions.add(andCondition);
            }
            // Forge 47 reads a plain top-level `conditions` array; `neoforge:conditions` is NeoForge-only.
            recipe.add("conditions", forgeConditions);
        }

        recipe.addProperty("type", "minecraft:smithing_transform");

        JsonObject templateObj = new JsonObject();
        templateObj.addProperty("item", getItemId(data.template));
        recipe.add("template", templateObj);

        JsonObject baseObj = new JsonObject();
        baseObj.addProperty("item", Registries.ITEM.getId(data.base).toString());
        recipe.add("base", baseObj);

        JsonObject additionObj = new JsonObject();
        additionObj.addProperty("item", getItemId(data.addition));
        recipe.add("addition", additionObj);

        JsonObject resultObj = new JsonObject();
        // 1.20.1's SmithingTransformRecipe.Serializer reads "item", not "id"
        resultObj.addProperty("item", Registries.ITEM.getId(data.result).toString());
        resultObj.addProperty("count", 1);
        recipe.add("result", resultObj);

        return recipe;
    }

    private String getItemId(Object itemOrId) {
        if (itemOrId instanceof Identifier id) {
            return id.toString();
        } else if (itemOrId instanceof String str) {
            return str;
        } else if (itemOrId instanceof Item item) {
            Identifier id = Registries.ITEM.getId(item);
            if (id.equals(Registries.ITEM.getId(Items.AIR))) {
                throw new IllegalStateException("Item resolved to minecraft:air - use Identifier instead of Item for cross-mod items!");
            }
            return id.toString();
        }
        throw new IllegalArgumentException("Template/Addition must be Item, Identifier, or String, got: " + itemOrId.getClass());
    }

    private record RecipeData(String name, Item base, Object template, Object addition, Item result,
                              String[] requiredMods, boolean withLoadConditions) { }
}
