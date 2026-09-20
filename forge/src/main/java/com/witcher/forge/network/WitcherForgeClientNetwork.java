package com.witcher.forge.network;

import net.witcher_rpg.client.WitcherExposedClient;
import net.witcher_rpg.network.ExposedGlowPayload;

public class WitcherForgeClientNetwork {
    public static void handleExposedGlow(ExposedGlowPayload packet) {
        WitcherExposedClient.setActive(packet.entityId(), packet.active());
    }

    private WitcherForgeClientNetwork() { }
}
