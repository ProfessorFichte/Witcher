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
import net.spell_engine.api.effect.Effects;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.blocks.WitcherBlocks;
import net.witcher_rpg.client.particle.WitcherParticles;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.enchantment.WitcherEnchantments;
import net.witcher_rpg.entity.WitcherEntities;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import net.witcher_rpg.item.WitcherArmorDiagrams;
import net.witcher_rpg.item.WitcherGroup;
import net.witcher_rpg.item.WitcherMaterials;
import net.witcher_rpg.item.WitcherTrinkets;
import net.witcher_rpg.item.armor.Armors;
import net.witcher_rpg.item.component.WitcherDataComponents;
import net.witcher_rpg.item.weapon.WeaponsRegister;
import net.witcher_rpg.sounds.Sounds;

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

    /// Forge 47 hands out one registration window per registry, and **only the helper it passes in can
    /// write**: Forge clears the vanilla `NamespacedWrapper`'s own lock only from 47.4.0 onwards, so on
    /// 47.0-47.3 (and NeoForge 1.20.1) a plain `Registry.register` throws *"Can not register to a locked
    /// registry"* even inside the correct window. `mods.toml` declares `loaderVersion = "[47,)"`, so
    /// those are supported configurations and every write below goes through `reg::register`.
    ///
    /// The loops here are **deliberate duplicates** of what `common` runs on Fabric - the workaround is
    /// contained in this file and the Fabric path is untouched. `common`'s `registerX()` methods keep
    /// working; each one that mixed creation with registration gained a creation-only sibling.
    ///
    /// Forge 47 dispatches the windows in this order (verified on 47.4.22): `sound_event`, `fluid`,
    /// `block`, **`attribute`**, `mob_effect`, `particle_type`, `item`, `entity_type`, ..., `enchantment`,
    /// ..., **`creative_mode_tab` (65, second to last)**.
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.ATTRIBUTE, reg -> {
            // Replaces the Fabric-only EntityAttributesMixin (<clinit> TAIL on EntityAttributes).
            WitcherAttributes.attributesToRegister().forEach(reg::register);
            // Spell schools resolve those attributes, so they follow in the same window.
            WitcherClassMod.registerSpellSchools();
        });
        event.register(RegistryKeys.SOUND_EVENT, reg -> Sounds.soundsToRegister().forEach(reg::register));
        event.register(RegistryKeys.PARTICLE_TYPE, reg -> WitcherParticles.particlesToRegister().forEach(reg::register));
        event.register(RegistryKeys.BLOCK, reg -> WitcherBlocks.blocksToRegister().forEach(reg::register));
        event.register(RegistryKeys.ITEM, reg -> {
            // Installs the Curios item factory. It must NOT run from the mod constructor: touching
            // `WitcherTrinkets` there pulls in `WitcherStatusEffects` -> `MRPGCEntityAttributes`, whose
            // static initializer registers into the (still locked) attribute registry.
            CompatFeatures.init();
            WitcherGroup.registerItemGroups();
            WitcherMaterials.itemsToRegister().forEach(reg::register);
            // Chained from WitcherMaterials.registerModItems() on Fabric; explicit here.
            WitcherArmorDiagrams.itemsToRegister().forEach(reg::register);
            WeaponsRegister.itemsToRegister(WitcherClassMod.itemConfig.value.weapons).forEach(reg::register);
            Armors.itemsToRegister(WitcherClassMod.itemConfig.value.armor_sets).forEach(reg::register);
            WitcherTrinkets.itemsToRegister(WitcherClassMod.trinketConfig.value.entries).forEach(reg::register);
            // Block items ride the ITEM window, not the BLOCK one - and stay last so the creative-tab
            // hooks are installed in the same order the Fabric entrypoint installs them.
            WitcherBlocks.blockItemsToRegister().forEach(reg::register);
            // Trailing side effects of common's registerItems(): the configs are seeded by the loops above.
            WitcherClassMod.itemConfig.save();
            WitcherClassMod.trinketConfig.save();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            WitcherStatusEffects.effectsToRegister(WitcherClassMod.effectConfig.value).forEach(reg::register);
            // The helper returns void where Registry.registerReference returned a RegistryEntry.
            Effects.linkEntries(WitcherStatusEffects.entries);
            WitcherClassMod.effectConfig.save();
        });
        event.register(RegistryKeys.ENCHANTMENT, reg -> WitcherEnchantments.enchantmentsToRegister().forEach(reg::register));
        event.register(RegistryKeys.ENTITY_TYPE, reg -> WitcherEntities.entityTypesToRegister().forEach(reg::register));
        // NOT in the ITEM block: `creative_mode_tab` is event 65, `item` is event 7.
        event.register(RegistryKeys.ITEM_GROUP, reg -> reg.register(WitcherGroup.ID, WitcherGroup.create()));
    }
}
