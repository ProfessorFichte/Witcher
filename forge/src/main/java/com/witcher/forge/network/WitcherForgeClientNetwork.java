package com.witcher.forge.network;

import net.witcher_rpg.client.WitcherExposedClient;
import net.witcher_rpg.network.ExposedGlowPayload;

/// Client-side handlers, resolved only from inside a handler body so a dedicated server never
/// classloads them.
public class WitcherForgeClientNetwork {
    public static void handleExposedGlow(ExposedGlowPayload packet) {
        WitcherExposedClient.setActive(packet.entityId(), packet.active());
    }

    private WitcherForgeClientNetwork() { }
}
