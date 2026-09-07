package net.witcher_rpg.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherEntities {
    public static void register() {
        YrdenEntity.ENTITY_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(MOD_ID, "yrden"),
                EntityType.Builder.<YrdenEntity>create(YrdenEntity::new, SpawnGroup.MISC)
                        .dimensions(6F, 0.5F)
                        .makeFireImmune()
                        .maxTrackingRange(128)
                        .trackingTickInterval(20)
                        .build("yrden")
        );
        YrdenMagicTrapEntity.ENTITY_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(MOD_ID, "yrden_magical_trap"),
                EntityType.Builder.<YrdenMagicTrapEntity>create(YrdenMagicTrapEntity::new, SpawnGroup.MISC)
                        .dimensions(6F, 0.5F)
                        .makeFireImmune()
                        .maxTrackingRange(128)
                        .trackingTickInterval(20)
                        .build("yrden_magical_trap")
        );
    }
}
