package de.rayzs.proregions.api.region.chunk;

import de.rayzs.proregions.api.world.TinyLocation;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record ChunkKey(String worldName, int x, int z) implements ConfigurationSerializable {

    public static ChunkKey from(final String worldName, final int x, final int z) {
        return new ChunkKey(
                worldName,
                x << 4,
                z << 4
        );
    }

    public static ChunkKey from(final Location location) {
        return new ChunkKey(
                location.getWorld().getName(),
                location.getBlockX() << 4,
                location.getBlockZ() << 4
        );
    }

    public static ChunkKey from(final TinyLocation location) {
        return new ChunkKey(
                location.worldName(),
                location.x() << 4,
                location.z() << 4
        );
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "world", worldName,
                "x", x,
                "z", z
        );
    }

    public static ChunkKey deserialize(final Map<String, Object> map) {
        final String worldName = (String) map.get("world");

        final int x = (int) map.get("x");
        final int z = (int) map.get("z");

        return new ChunkKey(worldName, x, z);
    }
}
