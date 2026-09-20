package com.witcher.forge.compat.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.spell_engine.api.item.ItemAttributeModifiers;
import net.witcher_rpg.compat.WitcherModifierIds;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class WitcherCurioItem extends Item implements ICurioItem {
    private ItemAttributeModifiers customAttributes = ItemAttributeModifiers.builder().build();

    public WitcherCurioItem(Item.Settings settings, @Nullable ItemAttributeModifiers customAttributes) {
        super(settings);
        if (customAttributes != null) {
            this.customAttributes = customAttributes;
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = LinkedHashMultimap.create();
        modifiers.putAll(ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack));
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
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        var isOnCooldown = false;
        if (slotContext.entity() instanceof PlayerEntity player) {
            isOnCooldown = !player.isCreative() && player.getItemCooldownManager().isCoolingDown(stack.getItem());
        }
        return ICurioItem.super.canUnequip(slotContext, stack) && !isOnCooldown;
    }
}
