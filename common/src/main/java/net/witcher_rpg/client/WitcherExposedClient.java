package net.witcher_rpg.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.witcher_rpg.network.ExposedGlowPayload;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WitcherExposedClient {
    public static final Set<Integer> EXPOSED_BY_ME = ConcurrentHashMap.newKeySet();

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ExposedGlowPayload.ID, (payload, context) -> {
            int id = payload.entityId();
            boolean active = payload.active();
            context.client().execute(() -> {
                if (active) {
                    EXPOSED_BY_ME.add(id);
                } else {
                    EXPOSED_BY_ME.remove(id);
                }
            });
        });
    }
}
