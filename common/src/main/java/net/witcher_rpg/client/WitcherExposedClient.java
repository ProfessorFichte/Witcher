package net.witcher_rpg.client;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WitcherExposedClient {
    public static final Set<Integer> EXPOSED_BY_ME = ConcurrentHashMap.newKeySet();

    public static void setActive(int entityId, boolean active) {
        if (active) {
            EXPOSED_BY_ME.add(entityId);
        } else {
            EXPOSED_BY_ME.remove(entityId);
        }
    }
}
