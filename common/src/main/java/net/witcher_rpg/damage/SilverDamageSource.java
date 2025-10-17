package net.witcher_rpg.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

public class SilverDamageSource extends DamageSource {
    public SilverDamageSource(RegistryEntry<DamageType> type) {
        super(type);
    }

    @Override
    public Text getDeathMessage(LivingEntity killed) {
        return Text.of(killed.getName() + " died from silver.");
    }
}
