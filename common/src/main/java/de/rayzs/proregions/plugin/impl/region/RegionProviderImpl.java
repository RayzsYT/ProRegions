package de.rayzs.proregions.plugin.impl.region;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.configuration.Config;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import de.rayzs.proregions.api.region.RegionProvider;
import de.rayzs.proregions.api.response.Response;
import de.rayzs.proregions.api.world.Environment;
import de.rayzs.proregions.plugin.impl.response.ResponseImpl;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class RegionProviderImpl implements RegionProvider {

    private final List<Map<String, Region>> regions;

    private final ProRegionAPI api;
    private final Config config;

    public RegionProviderImpl(final ProRegionAPI api, final Config config) {
        this.api = api;
        this.config = config;

        final List<Map<String, Region>> list = new ArrayList<>();

        for (final Environment environment : Environment.values()) {
            if (environment == Environment.INVALID) {
                continue;
            }

            list.add(new HashMap<>());
        }

        this.regions = Collections.unmodifiableList(list);

        reload();
    }

    @Override
    public void reload() {
        for (final Map<String, Region> map : regions) {
            map.clear();
        }

        for (String regionName : this.config.getKeys(false)) {
            final Region region = (RegionImpl) this.config.get(regionName);
            if (region == null) {
                continue;
            }

            final int environmentId = region.getEnvironment().getId();
            regions.get(environmentId).put(regionName, region);
        }
    }

    @Override
    public Region getRegion(final String name) {
        for (Map<String, Region> map : regions) {
            final Region region = map.get(name);

            if (region != null) {
                return region;
            }
        }

        return null;
    }

    @Override
    public Collection<Region> getRegions() {
        return regions.stream()
                .flatMap(map -> map.values().stream())
                .toList();
    }

    @Override
    public Collection<Region> getRegions(final World world) {
        final Environment environment = Environment.getEnvironmentByWorld(world);
        final Map<String, Region> map = this.regions.get(environment.getId());

        if (map == null) {
            return Set.of();
        }

        return map.values();
    }

    @Override
    public boolean isAllowed(
            final Block block,
            final RegionEnums.Flags flag
    ) {
        final Location location = block.getLocation();
        final World world = location.getWorld();

        if (world == null) {
            return true;
        }

        for (final Region region : getRegions(world)) {
            if (region.contains(block) && region.getFlagState(flag) == RegionEnums.FlagState.DENY) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isAllowed(
            final Entity entity,
            final Block block,
            final RegionEnums.Flags flag
    ) {
        final World world = block.getWorld();

        if (world == null) {
            return true;
        }

        final Player player = entity instanceof Player p ? p : null;
        if (player != null && player.hasPermission("proregion.bypass." + flag.name().toLowerCase())) {
            return true;
        }

        for (final Region region : getRegions(world)) {
            if (region.contains(block)) {
                if (region.getFlagState(flag) == RegionEnums.FlagState.DENY) {

                    if (player == null) {
                        return false;
                    }

                    region.getResponse(flag).send(player);
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean isAllowed(
            final Entity entity,
            final RegionEnums.Flags flag
    ) {
        final World world = entity.getLocation().getWorld();

        if (world == null) {
            return true;
        }

        final Player player = entity instanceof Player p ? p : null;
        if (player != null && player.hasPermission("proregion.bypass." + flag.name().toLowerCase())) {
            return true;
        }

        for (final Region region : getRegions(world)) {
            if (region.contains(entity)) {
                if (region.getFlagState(flag) == RegionEnums.FlagState.DENY) {

                    if (player == null) {
                        return false;
                    }

                    region.getResponse(flag).send(player);
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean isAllowed(final Entity executor, final Entity target, final RegionEnums.Flags flag) {
        final World world = target.getLocation().getWorld();

        if (world == null) {
            return true;
        }

        final Player player = executor instanceof Player p ? p : null;
        if (player != null && player.hasPermission("proregion.bypass." + flag.name().toLowerCase())) {
            return true;
        }

        for (final Region region : getRegions(world)) {
            if (region.contains(target)) {
                if (region.getFlagState(flag) == RegionEnums.FlagState.DENY) {

                    if (player == null) {
                        return false;
                    }

                    region.getResponse(flag).send(player);
                    return false;
                }
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
            final Location firstLocation,
            final Location secondLocation
    ) {
        final World world = firstLocation.getWorld();
        final int environmentId = Environment.getEnvironmentByWorld(world).getId();

        if (regions.get(environmentId).containsKey(name)) {
            return false;
        }

        final Map<RegionEnums.Flags, RegionEnums.FlagState> flags = new HashMap<>();
        for (RegionEnums.Flags value : RegionEnums.Flags.values()) {
            flags.put(value, RegionEnums.FlagState.DENY);
        }

        final Region region = new RegionImpl(
                name,
                world.getName(),
                ignoreY,
                new ResponseImpl(),
                new HashMap<>(),
                RegionEnums.FlagState.DENY,
                flags,
                api.toTinyLocation(firstLocation),
                api.toTinyLocation(secondLocation)
        );

        regions.get(environmentId).put(name, region);
        saveRegion(region);
        return true;
    }

    @Override
    public void saveAllRegions() {
        for (Map<String, Region> map : regions) {
            for (Region region : map.values()) {
                saveRegion(region);
            }
        }
    }

    @Override
    public boolean deleteRegion(String name) {
        for (Map<String, Region> map : regions) {
            if (map.remove(name) != null) {
                config.setAndSave(name, null);
                return true;
            }
        }

        return false;
    }
}
