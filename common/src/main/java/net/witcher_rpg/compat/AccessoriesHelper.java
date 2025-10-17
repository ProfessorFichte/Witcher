package net.witcher_rpg.compat;

import io.wispforest.accessories.api.components.AccessoriesDataComponents;
import io.wispforest.accessories.api.components.AccessoryItemAttributeModifiers;
import net.witcher_rpg.item.WitcherFactory;

public class AccessoriesHelper {
    public static void registerFactory() {
        WitcherFactory.factory = args -> {
            var settings = args.settings();
            var attributes = args.attributes();
            var slot = args.slot() != null ? args.slot() : "spell_trinket";

            if (attributes != null) {
                var builder = AccessoryItemAttributeModifiers.builder();
                for (var bonus : attributes.modifiers()) {
                    builder = builder.addForSlot(bonus.attribute(), bonus.modifier(), slot, true);
                }
                settings = settings.component(AccessoriesDataComponents.ATTRIBUTES, builder.build());
            }
            return new WitcherAccessoriesItem(settings);
        };
    }
}
