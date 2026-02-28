package de.rayzs.proregions.plugin.impl.region;

import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import de.rayzs.proregions.api.region.chunk.ChunkKeyGenerator;
import de.rayzs.proregions.api.response.Response;
import de.rayzs.proregions.api.world.TinyLocation;
import de.rayzs.proregions.plugin.impl.response.ResponseImpl;
import de.rayzs.proregions.plugin.impl.world.TinyLocationImpl;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.*;

public class RegionImpl implements Region {

    private final String name, worldName;

    private final Map<RegionEnums.Flags, RegionEnums.FlagState> flags;
    private final Map<RegionEnums.Flags, Response> responses;

    private final Map<RegionEnums.Flags, Map<String, RegionEnums.FlagState>> specificFlags;

    private final Response defaultResponse;

    private final TinyLocation centerLocation;

    private final ArrayList<Long> chunkKeys;

    private final int minX, minY, minZ;
    private final int maxX, maxY, maxZ;

    private boolean ignoreY;

    public RegionImpl(
            final ArrayList<Long> chunkKeys,
            final String name,
            final String worldName,
            final boolean ignoreY,
            final Response defaultResponse,
            final Map<RegionEnums.Flags, Response> responses,
            final Map<RegionEnums.Flags, RegionEnums.FlagState> flags,
            final Map<RegionEnums.Flags, Map<String, RegionEnums.FlagState>> specificFlags,
            final TinyLocation firstLocation,
            final TinyLocation secondLocation
    ) {
        this.name = name;
        this.worldName = worldName;

        this.ignoreY = ignoreY;

        this.flags = flags;

        this.defaultResponse = defaultResponse;
        this.responses = responses;
        this.specificFlags = specificFlags;

        this.minX = Math.min(firstLocation.x(), secondLocation.x());
        this.minY = Math.min(firstLocation.y(), secondLocation.y());
        this.minZ = Math.min(firstLocation.z(), secondLocation.z());

        this.maxX = Math.max(firstLocation.x(), secondLocation.x());
        this.maxY = Math.max(firstLocation.y(), secondLocation.y());
        this.maxZ = Math.max(firstLocation.z(), secondLocation.z());

        if (chunkKeys.isEmpty()) {
            for (int x = minX; x < maxX + 1; x++) {
                for (int z = minZ; z < maxZ + 1; z++) {
                    final long key = ChunkKeyGenerator.getChunkKey(x, z);

                    if (chunkKeys.contains(key)) {
                        continue;
                    }

                    chunkKeys.add(key);
                }
            }
        }

        this.chunkKeys = chunkKeys;

        final int halfX = (maxX - minX) / 2;
        final int halfY = (maxY - minY) / 2;
        final int halfZ = (maxZ - minZ) / 2;


        this.centerLocation = new TinyLocationImpl(
                name,
                minX + halfX,
                minY + halfY,
                minZ + halfZ
        );
    }

    @Override
    public boolean contains(final Block block) {
        return contains(block.getLocation());
    }

    @Override
    public boolean contains(final Entity entity) {
        return !entity.isDead() && contains(entity.getLocation());
    }

    @Override
    public boolean contains(final Location location) {
        final World world = location.getWorld();

        if (world == null) {
            return false;
        }

        if (!this.worldName.equalsIgnoreCase(world.getName())) {
            return false;
        }

        final int x = location.getBlockX();
        final int y = location.getBlockY();
        final int z = location.getBlockZ();

        return x >= minX && x <= maxX
                && (ignoreY || y >= minY && y <= maxY)
                && z >= minZ && z <= maxZ;
    }

    @Override
    public Response getDefaultResponse() {
        return this.defaultResponse;
    }

    @Override
    public Response getResponse(RegionEnums.Flags flag) {
        return this.responses.getOrDefault(flag, this.defaultResponse);
    }

    public Response getOrCreateResponse(RegionEnums.Flags flag) {
        Response response = this.responses.get(flag);

        if (response == null) {
            response = new ResponseImpl();
            responses.put(flag, response);
        }

        return response;
    }

    public Response removeResponse(RegionEnums.Flags flag) {
        return this.responses.remove(flag);
    }

    @Override
    public void unsetFlag(RegionEnums.Flags flag) {
        this.flags.remove(flag);
    }

    @Override
    public void unsetFlag(RegionEnums.Flags flag, String entityType) {
        final Map<String, RegionEnums.FlagState> flagStates = specificFlags.getOrDefault(flag, new HashMap<>());
        flagStates.remove(entityType.toUpperCase());

        if (flagStates.isEmpty()) {
            specificFlags.remove(flag);
        } else {
            specificFlags.put(flag, flagStates);
        }
    }

    @Override
    public void setFlag(RegionEnums.Flags flag, RegionEnums.FlagState state) {
        this.flags.put(flag, state);
    }

    @Override
    public void setFlag(RegionEnums.Flags flag, RegionEnums.FlagState state, String specification) {
        final Map<String, RegionEnums.FlagState> flagStates = specificFlags.getOrDefault(flag, new HashMap<>());
        flagStates.put(specification.toUpperCase(), state);

        specificFlags.put(flag, flagStates);
    }

    @Override
    public RegionEnums.FlagState getFlagState(RegionEnums.Flags flag) {
        return this.flags.getOrDefault(flag, RegionEnums.FlagState.DENY);
    }

    @Override
    public RegionEnums.FlagState getFlagState(RegionEnums.Flags flag, String specification) {
        final Map<String, RegionEnums.FlagState> flagStates = specificFlags.get(flag);

        if (flagStates != null) {
            final RegionEnums.FlagState specificState = flagStates.get(
                    specification.toUpperCase()
            );

            if (specificState != null) {
                return specificState;
            }
        }

        return getFlagState(flag);
    }

    @Override
    public void setIgnoreY(boolean ignoreY) {
        this.ignoreY = ignoreY;
    }

    @Override
    public boolean doesIgnoreY() {
        return this.ignoreY;
    }

    @Override
    public List<Long> getChunkKeys() {
        return this.chunkKeys;
    }

    @Override
    public TinyLocation getCenter() {
        return this.centerLocation;
    }

    @Override
    public Map<String, Object> serialize() {

        final Map<String, Object> map = new HashMap<>();

        map.put("name", name);
        map.put("chunk-keys", chunkKeys);
        map.put("ignore-y", ignoreY);

        map.put("area-world", worldName);

        map.put("area-min-x", minX);
        map.put("area-min-y", minY);
        map.put("area-min-z", minZ);

        map.put("area-max-x", maxX);
        map.put("area-max-y", maxY);
        map.put("area-max-z", maxZ);

        map.put("flags", flags.entrySet().stream().collect(
                java.util.stream.Collectors.toMap(
                        entry -> entry.getKey().name(),
                        entry -> entry.getValue().name()
                )
        ));


        map.put("specific-flags",
                specificFlags.entrySet().stream().collect(
                        java.util.stream.Collectors.toMap(
                                outer -> outer.getKey().name(),
                                outer -> outer.getValue().entrySet().stream().collect(
                                        java.util.stream.Collectors.toMap(
                                                inner -> inner.getKey(),
                                                inner -> inner.getValue().name()
                                        )
                                )
                        )
                )
        );

        map.put("default-response", defaultResponse);
        map.put("responses", responses.entrySet().stream().collect(
                java.util.stream.Collectors.toMap(
                        entry -> entry.getKey().name(),
                        entry -> entry.getValue()
                )
        ));

        return map;
    }

    public static RegionImpl deserialize(Map<String, Object> map) {
        final String name = (String) map.get("name");
        final ArrayList<Long> key = (ArrayList<Long>) map.get("chunk-keys");

        final boolean ignoreY = (boolean) map.get("ignore-y");

        final String worldName = (String) map.get("area-world");
        final int minX = (int) map.get("area-min-x");
        final int minY = (int) map.get("area-min-y");
        final int minZ = (int) map.get("area-min-z");
        
        final int maxX = (int) map.get("area-max-x");
        final int maxY = (int) map.get("area-max-y");
        final int maxZ = (int) map.get("area-max-z");

        final Map<RegionEnums.Flags, RegionEnums.FlagState> flags = new HashMap<>();
        final Map<String, String> flagsMap = (Map<String, String>) map.get("flags");

        for (Map.Entry<String, String> entry : flagsMap.entrySet()) {

            // In case a flag does not exist anymore or is invalid.
            // Instead of throwing an exception, it will just skip that flag.
            try {
                final RegionEnums.Flags flag = RegionEnums.Flags.valueOf(entry.getKey());
                final RegionEnums.FlagState state = RegionEnums.FlagState.valueOf(entry.getValue());

                flags.put(flag, state);
            } catch (IllegalArgumentException ignored) { }
        }

        final Response defaultResponse = (Response) map.get("default-response");

        final Map<RegionEnums.Flags, Response> responses = new HashMap<>();
        final Map<String, Response> responsesMap = (Map<String, Response>) map.get("responses");

        for (Map.Entry<String, Response> entry : responsesMap.entrySet()) {
            final RegionEnums.Flags flag = RegionEnums.Flags.valueOf(entry.getKey());
            final Response response = entry.getValue();

            responses.put(flag, response);
        }

        final Map<RegionEnums.Flags, Map<String, RegionEnums.FlagState>> specificFlags = new HashMap<>();
        final Map<String, Map<String, String>> specificFlagsMap = (Map<String, Map<String, String>>) map.get("specific-flags");

        for (Map.Entry<String, Map<String, String>> outerEntry : specificFlagsMap.entrySet()) {
            final RegionEnums.Flags flag = RegionEnums.Flags.valueOf(outerEntry.getKey());
            final Map<String, RegionEnums.FlagState> innerMap = new HashMap<>();

            for (Map.Entry<String, String> innerEntry : outerEntry.getValue().entrySet()) {
                final String type = innerEntry.getKey();
                final RegionEnums.FlagState state = RegionEnums.FlagState.valueOf(innerEntry.getValue());

                innerMap.put(type, state);
            }

            specificFlags.put(flag, innerMap);
        }

        return new RegionImpl(
                key,
                name, worldName, ignoreY,
                defaultResponse, responses,
                flags, specificFlags,
                new TinyLocationImpl(name, minX, minY, minZ),
                new TinyLocationImpl(name, maxX, maxY, maxZ)
        );
    }

    @Override
    public String getRegionName() {
        return this.name;
    }

    @Override
    public String getWorldName() {
        return this.worldName;
    }

}
