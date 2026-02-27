package de.rayzs.proregions.plugin.impl.region;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.clipboard.Clipboard;
import de.rayzs.proregions.api.configuration.Config;
import de.rayzs.proregions.api.events.ProRegionsEvent;
import de.rayzs.proregions.api.events.PlayerEnterRegionEvent;
import de.rayzs.proregions.api.events.PlayerLeaveRegionEvent;
import de.rayzs.proregions.api.region.*;
import de.rayzs.proregions.api.region.chunk.ChunkKeyGenerator;
import de.rayzs.proregions.api.region.context.ContextEval;
import de.rayzs.proregions.api.response.Response;
import de.rayzs.proregions.plugin.impl.response.ResponseImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class RegionProviderImpl implements RegionProvider {

    // Regions are stored based on chunk key.
    private final Map<Long, Map<String, Region>> regions;
    private final Map<UUID, Set<Region>> playerRegions;

    private final ProRegionsAPI api;
    private final Config config;

    public RegionProviderImpl(final ProRegionsAPI api, final Config config) {
        this.api = api;
        this.config = config;

        this.regions = new HashMap<>();
        this.playerRegions = new HashMap<>();

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

            for (long chunkKey : region.getChunkKeys()) {
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
    public Map<String, Region> getRegions(final Location location) {
        final Map<String, Region> result = new HashMap<>(getRegionsOnChunk(location));

        result.entrySet().removeIf(e -> !e.getValue().contains(location));

        return result;
    }

    @Override
    public Map<String, Region> getRegionsOnChunk(final Location location) {
        final long key = ChunkKeyGenerator.getChunkKey(
                location.getBlockX(),
                location.getBlockZ()
        );

        return regions.getOrDefault(key, Map.of());
    }

    @Override
    public Set<Region> getCachedPlayerRegions(final Player player) {
        return playerRegions.getOrDefault(player.getUniqueId(), Set.of());
    }

    @Override
    public void resetPlayerRegionsCache(final Player player) {
        playerRegions.remove(player.getUniqueId());
    }

    @Override
    public void updatePlayerRegionsCache(
            final Player player,
            final Location location,
            final Predicate<Set<Region>> condition
    ) {
        final UUID uuid = player.getUniqueId();

        final Set<Region> set = new HashSet<>();
        for (Region region : getRegionsOnChunk(location).values()) {
            if (region.contains(location)) {
                set.add(region);
            }
        }

        if (condition.test(set)) {
            playerRegions.put(uuid, set);
        }
    }

    @Override
    public <A,B,C,D> boolean isAllowed(
            final Region region,
            final ContextEval<A,B,C,D> context,
            final Location location,
            final RegionEnums.Flags flag,
            final A a, final B b,
            final C c, final D d
    ) {
        final World world = location.getWorld();
        if (world == null || !region.contains(location)) {
            return true;
        }

        return isFlagAllowedForRegion(
                context, location, region,
                flag, a, b, c, d
        );
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

        // I could also use getRegions(location) instead, but since I don't want
        // to create an inner copy of the map, I've decided to instead fetch all
        // regions around the chunk, which are already stored, to narrow them down
        // internally, to avoid memory leaks in the future.
        for (final Region region : getRegionsOnChunk(location).values()) {
            if (!region.contains(location)) {
                continue;
            }

            if (!isFlagAllowedForRegion(
                    context, location, region,
                    flag, a, b, c, d
            )) return false;
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

        for (Map<String, Region> maps : regions.values()) {
            if (maps.containsKey(name)) {
                return false;
            }
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

        for (long chunkKey : region.getChunkKeys()) {
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
        final AtomicBoolean result = new AtomicBoolean(false);

        regions.entrySet().removeIf(entry -> {
            if (entry.getValue().containsKey(name)) {
                result.set(true);
                return true;
            }

            return false;
        });

        config.setAndSave(name, null);

        return result.get();
    }

    private <A,B,C,D> boolean isFlagAllowedForRegion(
            final ContextEval<A,B,C,D> context,
            final Location location,
            final Region region,
            final RegionEnums.Flags flag,
            final A a, final B b,
            final C c, final D d
    ) {
        final RegionEnums.FlagState evaluation = context.evaluate(
                region, flag,
                a, b, c, d
        );

        final ProRegionsEvent event = handleEvent(
                context, region, flag, evaluation, a, b, c, d
        );

        final boolean denied = event != null
                ? event.isCancelled()
                : evaluation == RegionEnums.FlagState.DENY;

        if (denied) {
            if (event == null || event.canSendResponse()) {
                final Response response = region.getResponse(flag);

                if (a instanceof Player player)
                    response.send(player);
            }

            return false;
        }

        return true;
    }

    private <A,B,C,D> ProRegionsEvent handleEvent(
            final ContextEval<A,B,C,D> context,
            final Region region,
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state,
            final A a, final B b,
            final C c, final D d
    ) {

        final Player player = a instanceof Player p ? p : null;

        if (player == null) {
            // Tbh, I only plan to use events for player
            // related actions only anyway. So if there's no
            // player involved, I'll simply ignore the whole thing.
            // Maybe I'll change this in the future, so I'll keep the player
            // field just in case.
            return null;
        }

        switch (flag) {
            case ENTER -> {
                final PlayerEnterRegionEvent event = new PlayerEnterRegionEvent(
                        player,
                        region,
                        state == RegionEnums.FlagState.DENY
                );

                Bukkit.getPluginManager().callEvent(event);
                return event;
            }

            case LEAVE -> {
                final PlayerLeaveRegionEvent event = new PlayerLeaveRegionEvent(
                        player,
                        region,
                        state == RegionEnums.FlagState.DENY
                );

                Bukkit.getPluginManager().callEvent(event);
                return event;
            }
            default -> {}
        }

        return null;
    }
}
