package net.witcher_rpg.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
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
                FabricEntityTypeBuilder.<YrdenEntity>create(SpawnGroup.MISC, YrdenEntity::new)
                        .dimensions(EntityDimensions.changing(6F, 0.5F))
                        .fireImmune()
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(20)
                        .build()
        );
        YrdenMagicTrapEntity.ENTITY_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(MOD_ID, "yrden_magical_trap"),
                FabricEntityTypeBuilder.<YrdenMagicTrapEntity>create(SpawnGroup.MISC, YrdenMagicTrapEntity::new)
                        .dimensions(EntityDimensions.changing(6F, 0.5F))
                        .fireImmune()
                        .trackRangeBlocks(128)
                        .trackedUpdateRate(20)
                        .build()
        );
    }
}
