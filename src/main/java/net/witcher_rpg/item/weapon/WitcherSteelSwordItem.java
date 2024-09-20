package net.witcher_rpg.item.weapon;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.more_rpg_classes.effect.MRPGCEffects;

import java.util.List;

public class WitcherSteelSwordItem extends WitcherSword {
    public WitcherSteelSwordItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }
    //10% chance to bleed non-undead
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        EntityType<?> type = ((Entity) target).getType();
        if(!type.isIn(EntityTypeTags.UNDEAD)){
            int bleed_duration = 200;
            int bleed_chance = 10;
            int randomrange_bleed = (int) ((Math.random() * (1 + bleed_chance)) + 1);

            if (randomrange_bleed >= bleed_chance ) {
                target.addStatusEffect(new StatusEffectInstance(MRPGCEffects.BLEEDING.registryEntry,bleed_duration,0,false,false,true));
            }
        }
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.witcher_rpg.steel_witcher_swords.description_1").formatted(Formatting.DARK_RED));
        }else {
            tooltip.add(Text.translatable("tooltip.witcher_rpg.shift_down"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
