package com.witcher.forge;

import com.witcher.forge.compat.CompatFeatures;
import com.witcher.forge.network.WitcherForgeNetwork;
import net.minecraft.registry.RegistryKeys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.client.particle.WitcherParticles;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import net.witcher_rpg.item.component.WitcherDataComponents;

@Mod(WitcherClassMod.MOD_ID)
public final class ForgeMod {

    // FMLJavaModLoadingContext.get() is flagged for removal by late 47.x builds, but the
    // constructor-injected replacement doesn't exist on early 47.x; get() works on all of [47,).
    @SuppressWarnings("removal")
    public ForgeMod() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Networking first: common init may already want to send.
        WitcherForgeNetwork.register();

        WitcherDataComponents.register();
        // Registers nothing into Forge-locked registries (that happens in RegisterEvent below).
        WitcherClassMod.init();


        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.witcher.forge.client.ForgeClient.register(modBus);
        }
    }

    /// Forge 47 hands out one registration window per registry; each `register` call must stay inside
    /// the window for the registry it touches (the registries are locked outside it).
    /// Forge 47 dispatches the windows in this order (verified on 47.4.22): `sound_event`, `fluid`,
    /// `block`, **`attribute`**, `mob_effect`, `particle_type`, `item`, `entity_type`, ..., `enchantment`.
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.ATTRIBUTE, reg -> {
            // Replaces the Fabric-only EntityAttributesMixin (<clinit> TAIL on EntityAttributes).
            WitcherAttributes.registerAttributes();
            // Spell schools resolve those attributes, so they follow in the same window.
            WitcherClassMod.registerSpellSchools();
        });
        event.register(RegistryKeys.SOUND_EVENT, reg -> WitcherClassMod.registerSounds());
        event.register(RegistryKeys.PARTICLE_TYPE, reg -> WitcherParticles.register());
        event.register(RegistryKeys.BLOCK, reg -> WitcherClassMod.registerBlocks());
        event.register(RegistryKeys.ITEM, reg -> {
            // Installs the Curios item factory. It must NOT run from the mod constructor: touching
            // `WitcherTrinkets` there pulls in `WitcherStatusEffects` -> `MRPGCEntityAttributes`, whose
            // static initializer registers into the (still locked) attribute registry.
            CompatFeatures.init();
            WitcherClassMod.registerItems();
            WitcherClassMod.registerBlockItems();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> WitcherClassMod.registerEffects());
        event.register(RegistryKeys.ENCHANTMENT, reg -> WitcherClassMod.registerEnchantments());
        event.register(RegistryKeys.ENTITY_TYPE, reg -> WitcherClassMod.registerEntities());
    }
}
