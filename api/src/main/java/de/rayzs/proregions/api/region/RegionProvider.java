package de.rayzs.proregions.api.region;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.Collection;

public interface RegionProvider {

    void reload();

    Region getRegion(final String name);

    Collection<Region> getRegions();
    Collection<Region> getRegions(World world);

    boolean isAllowed(
            final Block block,
            final RegionEnums.Flags flag
    );

    boolean isAllowed(
            final Entity entity,
            final RegionEnums.Flags flag
    );

    boolean isAllowed(
            final Entity executor,
            final Entity target,
            final RegionEnums.Flags flag
    );

    boolean isAllowed(
            final Entity entity,
            final Block block,
            final RegionEnums.Flags flag
    );

    void saveAllRegions();

    void saveRegion(final Region region);

    boolean createRegion(
            final String name,
            final boolean ignoreY,
            final Location firstLocation,
            final Location sectionLocation
    );

    boolean deleteRegion(final String name);
}
