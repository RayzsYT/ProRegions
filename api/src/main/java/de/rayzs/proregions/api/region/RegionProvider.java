package de.rayzs.proregions.api.region;

import de.rayzs.proregions.api.clipboard.Clipboard;
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

    /**
     * Can Block run through the event.
     *
     * @param block The block.
     * @param flag The flag.
     * @return True if allowed, otherwise false.
     */
    boolean isAllowed(
            final Block block,
            final RegionEnums.Flags flag
    );

    /**
     * Can Entity run through the event.
     *
     * @param entity The entity.
     * @param flag The flag.
     * @return True if allowed, otherwise false.
     */
    boolean isAllowed(
            final Entity entity,
            final RegionEnums.Flags flag
    );

    /**
     * Can Entity who interacts with another entity can
     * run though the event.
     *
     * @param interactor Entity interacting with the target.
     * @param target Entity getting interacted with.
     * @param flag The flag.
     * @return True if allowed, otherwise false.
     */
    boolean isAllowed(
            final Entity interactor,
            final Entity target,
            final RegionEnums.Flags flag
    );

    /**
     * Can Entity who interact with Block can
     * run though the event.
     *
     * @param entity Entity interacting with the block.
     * @param block Interacted Block.
     * @param flag The flag.
     * @return True if allowed, otherwise false.
     */
    boolean isAllowed(
            final Entity entity,
            final Block block,
            final RegionEnums.Flags flag
    );

    /**
     * Can Entity place a Material on a Block.
     *
     * @param entity Entity who places Material on Block.
     * @param block Block.
     * @param material Material getting placed on Block.
     * @param flag The flag.
     * @return True if allowed, otherwise false.
     */
    boolean isAllowed(
            final Entity entity,
            final Block block,
            final Material material,
            final RegionEnums.Flags flag
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
