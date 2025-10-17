package net.witcher_rpg.compat;

import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class WitcherAccessoriesItem extends AccessoryItem {
    public WitcherAccessoriesItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canUnequip(ItemStack stack, SlotReference reference) {
        var entity = reference.entity();
        var isOnCooldown = false;
        if (entity instanceof PlayerEntity player) {
            isOnCooldown = !player.isCreative() && player.getItemCooldownManager().isCoolingDown(stack.getItem());
        }
        return super.canUnequip(stack, reference) && !isOnCooldown;
    }
}
