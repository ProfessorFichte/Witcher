package com.witcher.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.client.WitcherClient;

@EventBusSubscriber(modid = WitcherClassMod.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        WitcherClient.init();
    }
}
