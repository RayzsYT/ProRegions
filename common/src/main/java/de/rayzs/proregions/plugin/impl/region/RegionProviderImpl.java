package de.rayzs.proregions.plugin.impl.region;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.clipboard.Clipboard;
import de.rayzs.proregions.api.configuration.Config;
import de.rayzs.proregions.api.region.*;
import de.rayzs.proregions.api.region.chunk.ChunkKey;
import de.rayzs.proregions.api.region.context.ContextEval;
import de.rayzs.proregions.api.response.Response;
import de.rayzs.proregions.plugin.impl.response.ResponseImpl;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class RegionProviderImpl implements RegionProvider {

    // Regions are stored based on chunk key.
    private final Map<ChunkKey, Map<String, Region>> regions;

    private final ProRegionsAPI api;
    private final Config config;

    public RegionProviderImpl(final ProRegionsAPI api, final Config config) {
        this.api = api;
        this.config = config;
        this.regions = new HashMap<>();

        reload();
    }

    @Override
    public void reload() {
        regions.clear();

        for (String regionName : this.config.getKeys(false)) {
            final Region region = (RegionImpl) this.config.get(regionName);
            if (region == null) {
                continue;
            }

            for (ChunkKey chunkKey : region.getChunkKeys()) {
                regions.computeIfAbsent(chunkKey, k -> new HashMap<>())
                        .put(regionName, region);
            }
        }
    }

    @Override
    public Region getRegion(final String name) {
        for (Map<String, Region> map : regions.values()) {
            final Region region = map.get(name);
            if (region != null) {
                return region;
            }
        }

        return null;
    }

    @Override
    public Map<String, Region> getRegions() {
        final Map<String, Region> result = new HashMap<>();

        for (Map<String, Region> map : regions.values()) {
            result.putAll(map);
        }

        return result;
    }

    @Override
    public Map<String, Region> getRegions(Location location) {
        final ChunkKey chunkKey = ChunkKey.from(location);
        return regions.getOrDefault(chunkKey, Map.of());
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

        for (final Region region : getRegions(location).values()) {
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

        if (!clipboard.isAvailable()) {
            return false;
        }

        final Map<RegionEnums.Flags, RegionEnums.FlagState> flags = new HashMap<>();
        for (RegionEnums.Flags value : RegionEnums.Flags.values()) {
            flags.put(value, value.getDefaultState());
        }

        final Region region = new RegionImpl(
                new ArrayList<>(),
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

        for (ChunkKey chunkKey : region.getChunkKeys()) {
            regions.computeIfAbsent(chunkKey, k -> new HashMap<>())
                    .put(name, region);
        }

        saveRegion(region);
        return true;
    }

    @Override
    public void saveAllRegions() {
        for (Region value : getRegions().values()) {
            saveRegion(value);
        }
    }

    @Override
    public boolean deleteRegion(String name) {
        regions.entrySet().removeIf(entry -> entry.getValue().containsKey(name));
        config.setAndSave(name, null);

        return false;
    }
}
