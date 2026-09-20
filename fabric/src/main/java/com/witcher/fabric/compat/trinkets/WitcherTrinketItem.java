package com.witcher.fabric.compat.trinkets;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.spell_engine.api.item.ItemAttributeModifiers;
import net.witcher_rpg.compat.WitcherModifierIds;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WitcherTrinketItem extends TrinketItem {
    private ItemAttributeModifiers customAttributes = ItemAttributeModifiers.builder().build();

    public WitcherTrinketItem(Settings settings, @Nullable ItemAttributeModifiers customAttributes) {
        super(settings);
        if (customAttributes != null) {
            this.customAttributes = customAttributes;
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        var itemPath = Registries.ITEM.getId(stack.getItem()).getPath();
        var modifierUuid = WitcherModifierIds.perSlotAndItem(uuid, itemPath);
        var modifierName = WitcherModifierIds.name(itemPath);
        for (var entry : this.customAttributes.modifiers()) {
            var attribute = entry.attributeValue();
            if (attribute == null) {
                continue;
            }
            modifiers.put(attribute,
                    new EntityAttributeModifier(modifierUuid, modifierName,
                            entry.modifier().getValue(), entry.modifier().getOperation()));
        }
        return modifiers;
    }

    public void setConfigurableModifiers(ItemAttributeModifiers modifiers) {
        this.customAttributes = modifiers;
    }

    @Override
    public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var isOnCooldown = false;
        if (entity instanceof PlayerEntity player) {
            isOnCooldown = !player.isCreative() && player.getItemCooldownManager().isCoolingDown(stack.getItem());
        }
        return super.canUnequip(stack, slot, entity) && !isOnCooldown;
    }
}
