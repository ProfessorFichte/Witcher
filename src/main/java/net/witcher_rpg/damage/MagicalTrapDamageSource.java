package net.witcher_rpg.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

public class MagicalTrapDamageSource extends DamageSource {
    public MagicalTrapDamageSource(RegistryEntry<DamageType> type) {
        super(type);
    }

    public Text getDeathMessage(LivingEntity killed) {
        return Text.of(killed.getEntityName() + " died in a magical trap");
    }
}

