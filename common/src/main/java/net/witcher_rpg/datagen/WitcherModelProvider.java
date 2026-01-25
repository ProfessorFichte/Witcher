package net.witcher_rpg.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.witcher_rpg.item.WitcherArmorDiagrams;
import net.witcher_rpg.item.WitcherTrinkets;
import net.witcher_rpg.item.armor.Armors;
import net.witcher_rpg.item.weapon.WeaponsRegister;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherModelProvider extends FabricModelProvider {
    public WitcherModelProvider(FabricDataOutput output) {
        super(output);
    }
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        WeaponsRegister.entries.forEach(entry -> {
            Item item = entry.item();
            if (item == null) return;
            Identifier itemId = Registries.ITEM.getId(item);
            String name = itemId.getPath();
            generateSwordModel(itemModelGenerator, itemId, name);
        });

        generateChargedSwordModel(itemModelGenerator, "aerondight_sword_charged");
        generateChargedSwordModel(itemModelGenerator, "iris_sword_charged");

        WitcherArmorDiagrams.ENTRIES.forEach(entry -> {
            Item item = entry.item();
            Identifier itemId = Registries.ITEM.getId(item);
            Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());
            JsonObject json = new JsonObject();
            json.addProperty("parent", "item/generated");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", "witcher_rpg:item/misc/witcher_armor_diagram");
            json.add("textures", textures);
            itemModelGenerator.writer.accept(modelId, () -> json);
        });
        WitcherTrinkets.entries.forEach(entry -> {
            Item item = entry.item().get();
            Identifier itemId = Registries.ITEM.getId(item);
            Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());
            JsonObject json = new JsonObject();
            json.addProperty("parent", "item/generated");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", "witcher_rpg:item/trinkets/" + entry.name());
            json.add("textures", textures);
            itemModelGenerator.writer.accept(modelId, () -> json);
        });
        Armors.entries.forEach(entry -> {
            Item item = entry.armorSet().head;
            Identifier itemId = Registries.ITEM.getId(item);
            Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());
            JsonObject json = new JsonObject();
            json.addProperty("parent", "item/generated");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", "witcher_rpg:item/armor/" + entry.name() + "_head");
            json.add("textures", textures);
            itemModelGenerator.writer.accept(modelId, () -> json);
        });
        Armors.entries.forEach(entry -> {
            Item item = entry.armorSet().chest;
            Identifier itemId = Registries.ITEM.getId(item);
            Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());
            JsonObject json = new JsonObject();
            json.addProperty("parent", "item/generated");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", "witcher_rpg:item/armor/" + entry.name() + "_chest");
            json.add("textures", textures);
            itemModelGenerator.writer.accept(modelId, () -> json);
        });
        Armors.entries.forEach(entry -> {
            Item item = entry.armorSet().feet;
            Identifier itemId = Registries.ITEM.getId(item);
            Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());
            JsonObject json = new JsonObject();
            json.addProperty("parent", "item/generated");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", "witcher_rpg:item/armor/" + entry.name() + "_feet");
            json.add("textures", textures);
            itemModelGenerator.writer.accept(modelId, () -> json);
        });
        Armors.entries.forEach(entry -> {
            Item item = entry.armorSet().legs;
            Identifier itemId = Registries.ITEM.getId(item);
            Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());
            JsonObject json = new JsonObject();
            json.addProperty("parent", "item/generated");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", "witcher_rpg:item/armor/" + entry.name() + "_legs");
            json.add("textures", textures);
            itemModelGenerator.writer.accept(modelId, () -> json);
        });
    }

    private void generateSwordModel(ItemModelGenerator gen, Identifier itemId, String name) {
        Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name);

        JsonObject json = new JsonObject();
        json.addProperty("parent", MOD_ID + ":item/witcher_sword_model");

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD_ID + ":item/weapons/" + name);
        json.add("textures", textures);

        // Add overrides for swords with charged variants
        if (name.equals("aerondight_sword")) {
            JsonArray overrides = new JsonArray();
            JsonObject override = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.addProperty(MOD_ID + ":aerondight_charged", 1);
            override.add("predicate", predicate);
            override.addProperty("model", MOD_ID + ":item/aerondight_sword_charged");
            overrides.add(override);
            json.add("overrides", overrides);
        } else if (name.equals("iris_sword")) {
            JsonArray overrides = new JsonArray();
            JsonObject override = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.addProperty(MOD_ID + ":iris_charged", 1);
            override.add("predicate", predicate);
            override.addProperty("model", MOD_ID + ":item/iris_sword_charged");
            overrides.add(override);
            json.add("overrides", overrides);
        }

        gen.writer.accept(modelId, () -> json);
    }

    private void generateChargedSwordModel(ItemModelGenerator gen, String name) {
        Identifier modelId = Identifier.of(MOD_ID, "item/" + name);

        JsonObject json = new JsonObject();
        json.addProperty("parent", MOD_ID + ":item/witcher_sword_model");

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD_ID + ":item/weapons/" + name);
        json.add("textures", textures);

        gen.writer.accept(modelId, () -> json);
    }
}
