package net.witcher_rpg.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.witcher_rpg.item.component.GlyphSlots;
import net.witcher_rpg.item.component.RunestoneSlots;
import net.witcher_rpg.item.component.WitcherDataComponents;
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
        if (result.isEmpty()) return result;

        ItemStack inputItem = this.input.getStack(1);
        if (inputItem.isEmpty()) return result;

        GlyphSlots oldGlyphSlots = WitcherDataComponents.getGlyphSlots(inputItem);
        GlyphSlots newGlyphSlots = WitcherDataComponents.getGlyphSlots(result);

        if (oldGlyphSlots != null && newGlyphSlots != null) {
            if (!oldGlyphSlots.attachedGlyphs().isEmpty() ||
                oldGlyphSlots.maxSlots() != newGlyphSlots.maxSlots()) {
                ItemStack modifiedResult = result.copy();
                GlyphSlots mergedSlots = new GlyphSlots(newGlyphSlots.maxSlots(), oldGlyphSlots.attachedGlyphs());
                WitcherDataComponents.setGlyphSlots(modifiedResult, mergedSlots);
                return modifiedResult;
            }
        }

        RunestoneSlots oldRunestoneSlots = WitcherDataComponents.getRunestoneSlots(inputItem);
        RunestoneSlots newRunestoneSlots = WitcherDataComponents.getRunestoneSlots(result);

        if (oldRunestoneSlots != null && newRunestoneSlots != null) {
            if (!oldRunestoneSlots.attachedRunestones().isEmpty() ||
                oldRunestoneSlots.maxSlots() != newRunestoneSlots.maxSlots()) {
                ItemStack modifiedResult = result.copy();
                RunestoneSlots mergedSlots = new RunestoneSlots(newRunestoneSlots.maxSlots(), oldRunestoneSlots.attachedRunestones());
                WitcherDataComponents.setRunestoneSlots(modifiedResult, mergedSlots);
                return modifiedResult;
            }
        }

        return result;
    }
}
