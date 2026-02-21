package de.rayzs.proregions.plugin.impl.world;

import de.rayzs.proregions.api.world.TinyLocation;
import java.util.Map;

public record TinyLocationImpl(String worldName, int x, int y, int z) implements TinyLocation {

    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                "worldName", worldName,
                "x", x,
                "y", y,
                "z", z
        );
    }

    public static TinyLocationImpl deserialize(Map<String, Object> map) {
        final String worldName = (String) map.get("worldName");
        final int x = (int) map.get("x");
        final int y = (int) map.get("y");
        final int z = (int) map.get("z");

        return new TinyLocationImpl(worldName, x, y, z);
    }
}
