package de.rayzs.proregions.plugin.impl.region;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.clipboard.Clipboard;
import de.rayzs.proregions.api.configuration.Config;
import de.rayzs.proregions.api.region.*;
import de.rayzs.proregions.api.region.chunk.ChunkKey;
import de.rayzs.proregions.api.region.context.ContextEval;
import de.rayzs.proregions.api.response.Response;
import de.rayzs.proregions.api.world.Environment;
import de.rayzs.proregions.plugin.impl.response.ResponseImpl;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class RegionProviderImpl implements RegionProvider {

    // Regions are stored based on environment as index.
    private final List<Map<String, Region>> dimensionsRegions;

    // Regions are stored based on chunk key.
    private final Map<ChunkKey, Map<String, Region>> chunkRegionCache = new HashMap<>();

    private final ProRegionsAPI api;
    private final Config config;

    public RegionProviderImpl(final ProRegionsAPI api, final Config config) {
        this.api = api;
        this.config = config;

        final List<Map<String, Region>> list = new ArrayList<>();

        for (final Environment environment : Environment.values()) {
            if (environment == Environment.INVALID) {
                continue;
            }

            list.add(new HashMap<>());
        }

        this.dimensionsRegions = Collections.unmodifiableList(list);

        reload();
    }

    @Override
    public void reload() {
        for (final Map<String, Region> map : dimensionsRegions) {
            map.clear();
        }

        for (String regionName : this.config.getKeys(false)) {
            final Region region = (RegionImpl) this.config.get(regionName);
            if (region == null) {
                continue;
            }

            final int environmentId = region.getEnvironment().getId();
            dimensionsRegions.get(environmentId).put(regionName, region);
        }
    }

    @Override
    public Region getRegion(final String name) {
        for (Map<String, Region> map : dimensionsRegions) {
            final Region region = map.get(name);

            if (region != null) {
                return region;
            }
        }

        return null;
    }

    @Override
    public Collection<Region> getRegions() {
        return dimensionsRegions.stream()
                .flatMap(map -> map.values().stream())
                .toList();
    }

    @Override
    public Collection<Region> getRegions(final World world) {
        final Environment environment = Environment.getEnvironmentByWorld(world);
        final Map<String, Region> map = dimensionsRegions.get(environment.getId());

        if (map == null) {
            return Set.of();
        }

        return map.values();
    }

    @Override
    public Collection<Region> getRegions(Location location) {
        final ChunkKey chunkKey = ChunkKey.from(location);
        return chunkRegionCache.getOrDefault(chunkKey, Map.of()).values();
    }

    @Override
    public Region getRegion(Location location) {
        final World world = location.getWorld();

        if (world == null) {
            return null;
        }

        for (final Region region : getRegions(world)) {
            if (region.contains(location)) {
                return region;
            }
        }

        return null;
    }

    @Override
    public <A,B,C,D> boolean isAllowed(
            final ContextEval<A,B,C,D> context,
            final Location location,
            final RegionEnums.Flags flag,
            final A a, final B b,
            final C c, final D d
    ) {
        final World world = location.getWorld();
        if (world == null) {
            return true;
        }

        for (final Region region : getRegions(world)) {
            if (!region.contains(location)) {
                continue;
            }

            final RegionEnums.FlagState evaluation = context.evaluate(
                    region, flag,
                    a, b, c, d
            );

            if (evaluation == RegionEnums.FlagState.DENY) {
                final Response response = region.getResponse(flag);

                if (a instanceof Player player)
                    response.send(player);

                return false;
            }
        }

        return true;
    }

    @Override
    public void saveRegion(final Region region) {
        config.setAndSave(region.getRegionName(), region);
    }

    @Override
    public boolean createRegion(
            final String name,
            final boolean ignoreY,
            final Clipboard clipboard
    ) {
        final World world = clipboard.getFirstLocation().getWorld();
        final int environmentId = Environment.getEnvironmentByWorld(world).getId();

        if (dimensionsRegions.get(environmentId).containsKey(name) || !clipboard.isAvailable()) {
            return false;
        }

        final Map<RegionEnums.Flags, RegionEnums.FlagState> flags = new HashMap<>();
        for (RegionEnums.Flags value : RegionEnums.Flags.values()) {
            flags.put(value, value.getDefaultState());
        }

        final Region region = new RegionImpl(
                new HashSet<>(),
                name,
                world.getName(),
                ignoreY,
                new ResponseImpl(),
                new HashMap<>(),
                flags,
                new HashMap<>(),
                api.toTinyLocation(clipboard.getFirstLocation()),
                api.toTinyLocation(clipboard.getSecondLocation())
        );

        dimensionsRegions.get(environmentId).put(name, region);

        for (ChunkKey chunkKey : region.getChunkKeys()) {
            chunkRegionCache
                    .computeIfAbsent(chunkKey, k -> new HashMap<>())
                    .put(name, region);
        }

        saveRegion(region);
        return true;
    }

    @Override
    public void saveAllRegions() {
        for (Map<String, Region> map : dimensionsRegions) {
            for (Region region : map.values()) {
                saveRegion(region);
            }
        }
    }

    @Override
    public boolean deleteRegion(String name) {
        for (Map<String, Region> map : dimensionsRegions) {
            if (map.remove(name) != null) {
                config.setAndSave(name, null);
                return true;
            }
        }

        return false;
    }
}
