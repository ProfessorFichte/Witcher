package net.witcher_rpg.datagen;

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

public class WitcherModelProvider extends FabricModelProvider {
    public WitcherModelProvider(FabricDataOutput output) {
        super(output);
    }
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
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
}
