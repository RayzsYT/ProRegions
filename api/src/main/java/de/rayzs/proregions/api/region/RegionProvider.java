package de.rayzs.proregions.api.region;

import de.rayzs.proregions.api.clipboard.Clipboard;
import de.rayzs.proregions.api.region.context.ContextEval;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.Collection;

public interface RegionProvider {

    void reload();

    Region getRegion(final String name);
    Region getRegion(final Location location);

    Collection<Region> getRegions();
    Collection<Region> getRegions(World world);


    <A,B,C,D> boolean isAllowed(
            final ContextEval<A,B,C,D> context,
            final Location location,
            final RegionEnums.Flags flag,
            final A a,
            final B b,
            final C c,
            final D d
    );

    void saveAllRegions();

    void saveRegion(final Region region);

    boolean createRegion(
            final String name,
            final boolean ignoreY,
            final Clipboard clipboard
    );

    boolean deleteRegion(final String name);
}
