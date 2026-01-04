package net.witcher_rpg.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.witcher_rpg.anvil.GlyphAnvilHandler;
import net.witcher_rpg.anvil.RunestoneAnvilHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow @Final private Property levelCost;

    private GlyphAnvilHandler.AnvilResult witcherResult = null;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void onUpdateResult(CallbackInfo ci) {
        ItemStack left = this.input.getStack(0);
        ItemStack right = this.input.getStack(1);

        if (left.isEmpty()) {
            this.witcherResult = null;
            return;
        }

        GlyphAnvilHandler.AnvilResult result = GlyphAnvilHandler.handleAnvilUpdate(left, right, null);

        if (!result.shouldHandle()) {
            result = RunestoneAnvilHandler.handleAnvilUpdate(left, right, null);
        }

        if (result.shouldHandle()) {
            this.witcherResult = result;
            this.output.setStack(0, result.output);
            this.levelCost.set(result.xpCost);
            this.sendContentUpdates();
            ci.cancel();
        } else {
            this.witcherResult = null;
        }
    }

    @Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
    private void onCanTakeOutput(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {
        if (this.witcherResult != null && this.witcherResult.shouldHandle()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "onTakeOutput", at = @At("HEAD"), cancellable = true)
    private void onTakeOutput(net.minecraft.entity.player.PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        if (this.witcherResult != null && this.witcherResult.shouldHandle()) {
            this.context.run((world, pos) -> {
                if (!player.getAbilities().creativeMode) {
                    player.addExperienceLevels(-this.witcherResult.xpCost);
                }

                ItemStack rightStack = this.input.getStack(1);
                if (!rightStack.isEmpty() && this.witcherResult.materialCost > 0) {
                    rightStack.decrement(this.witcherResult.materialCost);
                }

                this.input.setStack(0, ItemStack.EMPTY);
                this.input.setStack(1, rightStack);
                this.output.setStack(0, ItemStack.EMPTY);

                world.playSound(null, pos, net.minecraft.sound.SoundEvents.BLOCK_ANVIL_USE,
                    net.minecraft.sound.SoundCategory.BLOCKS, 1.0F, 1.0F);

                this.sendContentUpdates();
            });

            this.witcherResult = null;
            ci.cancel();
        }
    }
}
