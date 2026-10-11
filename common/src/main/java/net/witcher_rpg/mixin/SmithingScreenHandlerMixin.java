package net.witcher_rpg.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.witcher_rpg.compat.jewelry.JewelrySmithingSupport;
import net.witcher_rpg.compat.jewelry.JewelrySocketFeature;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {

    public SmithingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @ModifyVariable(method = "updateResult", at = @At(value = "STORE"), ordinal = 0)
    private ItemStack modifySmithingResult(ItemStack result) {
        if (result.isEmpty() || !JewelrySocketFeature.isJewelryPresent()) return result;

        ItemStack inputItem = this.input.getStack(1);
        if (inputItem.isEmpty()) return result;

        int socketCount = JewelrySmithingSupport.socketCount(result);
        if (socketCount == 0) return result;

        ItemStack modifiedResult = result.copy();
        boolean modified = JewelrySmithingSupport.preserveGlyphSockets(inputItem, modifiedResult, socketCount);
        modified |= JewelrySmithingSupport.preserveRunestoneSockets(inputItem, modifiedResult, socketCount);
        return modified ? modifiedResult : result;
    }
}
