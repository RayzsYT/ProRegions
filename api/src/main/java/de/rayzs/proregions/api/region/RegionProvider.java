package de.rayzs.proregions.api.region;

import de.rayzs.proregions.api.clipboard.Clipboard;
import de.rayzs.proregions.api.region.chunk.ChunkKey;
import de.rayzs.proregions.api.region.context.ContextEval;
import org.bukkit.Location;
import org.bukkit.World;
import java.util.Collection;
import java.util.Map;

public interface RegionProvider {

    /**
     * Reloads all regions from the regions.yml file.
     */
    void reload();

    /**
     * Get a region by its name.
     *
     * @param name the name of the region to get.
     * @return the region with the given name, or null if no region with that name exists.
     */
    Region getRegion(final String name);

    /**
     * Creates and returns a copy of all regions stored with
     * their name as key and their object as value.
     *
     * @return map of all regions.
     */
    Map<String, Region> getRegions();

    /**
     * Returns an already created and stored map containing regions with their
     * name as key and object as value on a certain location.
     *
     * @param location the location to get the regions from.
     * @return an immutable collection of all regions at the specified location.
     */
    Map<String, Region> getRegions(Location location);

    /**
     * Checks if action is allowed or not.
     *
     * @param context the context to check. See {@link de.rayzs.proregions.api.region.context.Contexts} for predefined contexts.
     * @param location the location where the action is performed.
     * @param flag the flag to check.
     * @param a first context parameter.
     * @param b second context parameter.
     * @param c third context parameter.
     * @param d fourth context parameter.
     * @return true if the action is allowed, false otherwise.
     * @param <A> the type of the first context parameter.
     * @param <B> the type of the second context parameter.
     * @param <C> the type of the third context parameter.
     * @param <D> the type of the fourth context parameter.
     *
     * <pre>
     * {@code
     *  // Example use case:
     *
     *  final Entity entity = ...;
     *  final Material itemMaterial = ...;
     *
     *  if (!provider.isAllowed(
     *      Contexts.ENTITY_ITEM,       // Context which fills the type parameters.
     *      entity.getLocation(),       // Location
     *      RegionEnums.Flags.PICKUP,   // Flag
     *      entity,                     // Entity picking up the item.
     *      itemMaterial,               // Material of the item being picked up.
     *      null,                       // Not required in in provided context.
     *      null                        // Not required in in provided context.
     *  )) {
     *      // Blocked
     *  } else {
     *      // Not blocked
     *  }
     * }
     * </pre>
     */
    <A,B,C,D> boolean isAllowed(
            final ContextEval<A,B,C,D> context,
            final Location location,
            final RegionEnums.Flags flag,
            final A a,
            final B b,
            final C c,
            final D d
    );

    /**
     * Saves all regions.
     */
    void saveAllRegions();

    /**
     * Saves a specific region.
     *
     * @param region the region to save.
     */
    void saveRegion(final Region region);

    /**
     * Creates a new region with the given name and clipboard selection.
     *
     * @param name the name of the region to create.
     * @param ignoreY whether to ignore the Y-axis when creating the region. If true, the region will extend from bedrock to sky.
     * @param clipboard the clipboard containing the selection to create the region from. The clipboard must contain a valid selection (e.g. a cuboid or polygon selection).
     * @return true if the region was created successfully, false otherwise (e.g. if a region with the same name already exists or if the clipboard selection is invalid).
     */
    boolean createRegion(
            final String name,
            final boolean ignoreY,
            final Clipboard clipboard
    );

    /**
     * Deletes the region with the given name.
     *
     * @param name the name of the region to delete.
     * @return true if the region was deleted successfully, false otherwise (e.g. if no region with the given name exists).
     */
    boolean deleteRegion(final String name);
}
