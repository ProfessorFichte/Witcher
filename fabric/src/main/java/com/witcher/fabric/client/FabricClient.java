package com.witcher.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.witcher_rpg.client.WitcherClient;

public final class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WitcherClient.init();
        WitcherClient.registerParticleAppearances();
    }
}
