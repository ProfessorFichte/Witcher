package net.witcher_rpg.item;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.spell_engine.api.item.ItemAttributeModifiers;
import net.spell_engine.utils.AttributeModifierUtil;
import net.minecraft.item.Item;

import java.util.function.Function;

public class WitcherFactory {
    public record ItemArgs(Item.Settings settings, @Nullable ItemAttributeModifiers attributes, @Nullable String lore, @Nullable String slot) { }

    public static Function<ItemArgs, Item> factory = args -> {
        var item = new Item(args.settings);
        if (args.attributes != null) {
            AttributeModifierUtil.setItemModifiers(item, args.attributes);
        }
        return item;
    };
    public static Function<ItemArgs, Item> getFactory() { return factory; }
}
