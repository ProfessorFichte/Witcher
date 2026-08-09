package net.witcher_rpg.effect;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WitcherExposed {
    private static final Map<UUID, UUID> SOURCE_BY_TARGET = new ConcurrentHashMap<>();

    public static void set(UUID target, UUID source) {
        SOURCE_BY_TARGET.put(target, source);
    }

    public static UUID get(UUID target) {
        return SOURCE_BY_TARGET.get(target);
    }

    public static UUID remove(UUID target) {
        return SOURCE_BY_TARGET.remove(target);
    }
}
