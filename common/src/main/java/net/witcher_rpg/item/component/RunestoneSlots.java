package net.witcher_rpg.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record RunestoneSlots(int maxSlots, List<ItemStack> attachedRunestones) {
    public static final Codec<RunestoneSlots> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.fieldOf("max_slots").forGetter(RunestoneSlots::maxSlots),
            ItemStack.CODEC.listOf().fieldOf("runestones").forGetter(RunestoneSlots::attachedRunestones)
        ).apply(instance, RunestoneSlots::new)
    );

    public static final RunestoneSlots EMPTY = new RunestoneSlots(0, List.of());

    public boolean canAttachRunestone() {
        return attachedRunestones.size() < maxSlots;
    }

    public RunestoneSlots withRunestone(ItemStack runestone) {
        List<ItemStack> newRunestones = new ArrayList<>(attachedRunestones);
        newRunestones.add(runestone.copy());
        return new RunestoneSlots(maxSlots, newRunestones);
    }

    public RunestoneSlots removeAllRunestones() {
        return new RunestoneSlots(maxSlots, List.of());
    }
}
