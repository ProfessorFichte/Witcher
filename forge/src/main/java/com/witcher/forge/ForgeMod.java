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

    @SuppressWarnings("removal")
    public ForgeMod() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        WitcherForgeNetwork.register();

        WitcherDataComponents.register();
        WitcherClassMod.init();


        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.witcher.forge.client.ForgeClient.register(modBus);
        }
    }

    // Goes through the helper on purpose, on Forge 47.0-47.3 a plain Registry.register throws "Can not register to a locked registry".
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.ATTRIBUTE, reg -> {
            WitcherAttributes.attributesToRegister().forEach(reg::register);
            WitcherClassMod.registerSpellSchools();
        });
        event.register(RegistryKeys.SOUND_EVENT, reg -> Sounds.soundsToRegister().forEach(reg::register));
        event.register(RegistryKeys.PARTICLE_TYPE, reg -> WitcherParticles.particlesToRegister().forEach(reg::register));
        event.register(RegistryKeys.BLOCK, reg -> WitcherBlocks.blocksToRegister().forEach(reg::register));
        event.register(RegistryKeys.ITEM, reg -> {
            CompatFeatures.init();
            WitcherGroup.registerItemGroups();
            WitcherMaterials.itemsToRegister().forEach(reg::register);
            WitcherArmorDiagrams.itemsToRegister().forEach(reg::register);
            WeaponsRegister.itemsToRegister(WitcherClassMod.itemConfig.value.weapons).forEach(reg::register);
            Armors.itemsToRegister(WitcherClassMod.itemConfig.value.armor_sets).forEach(reg::register);
            WitcherTrinkets.itemsToRegister(WitcherClassMod.trinketConfig.value.entries).forEach(reg::register);
            WitcherBlocks.blockItemsToRegister().forEach(reg::register);
            WitcherClassMod.itemConfig.save();
            WitcherClassMod.trinketConfig.save();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            WitcherStatusEffects.effectsToRegister(WitcherClassMod.effectConfig.value).forEach(reg::register);
            Effects.linkEntries(WitcherStatusEffects.entries);
            WitcherClassMod.effectConfig.save();
        });
        event.register(RegistryKeys.ENCHANTMENT, reg -> WitcherEnchantments.enchantmentsToRegister().forEach(reg::register));
        event.register(RegistryKeys.ENTITY_TYPE, reg -> WitcherEntities.entityTypesToRegister().forEach(reg::register));
        event.register(RegistryKeys.ITEM_GROUP, reg -> reg.register(WitcherGroup.ID, WitcherGroup.create()));
    }
}
