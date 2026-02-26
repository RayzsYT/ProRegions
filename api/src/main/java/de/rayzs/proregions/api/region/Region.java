package de.rayzs.proregions.api.region;

import de.rayzs.proregions.api.response.Response;
import de.rayzs.proregions.api.world.Environment;
import de.rayzs.proregions.api.world.TinyLocation;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface Region extends ConfigurationSerializable {

    /**
     * Checks if the given entity is within the region.
     *
     * @param entity the entity to check.
     * @return true if the entity is within the region, false otherwise.
     */
    boolean contains(final Entity entity);

    /**
     * Checks if the given block is within the region.
     *
     * @param block the block to check.
     * @return true if the block is within the region, false otherwise.
     */
    boolean contains(final Block block);

    /**
     * Checks if the given location is within the region.
     *
     * @param location the location to check.
     * @return true if the location is within the region, false otherwise.
     */
    boolean contains(final Location location);

    /**
     * Returns the default response for this region.
     * @return the default response for this region.
     */
    Response getDefaultResponse();

    /**
     * Returns the response for the given flag. If the flag is not set, it returns the default response.
     *
     * @param flag the flag to get the response for.
     * @return the response for the given flag, or the default response if the flag is not set.
     */
    Response getResponse(final RegionEnums.Flags flag);

    /**
     * Unsets the flag state for the given flag.
     *
     * @param flag the flag to unset the state for.
     */
    void unsetFlag(final RegionEnums.Flags flag);

    /**
     * Unsets the flag state for the given flag and specification.
     *
     * @param flag the flag to unset the state for.
     * @param specification the specification to unset the state for.
     */
    void unsetFlag(final RegionEnums.Flags flag, final String specification);

    /**
     * Sets the flag state for the given flag.
     *
     * @param flag the flag to set the state for.
     * @param state the state to set for the flag.
     */
    void setFlag(
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state
    );

    /**
     * Sets the flag state for the given flag and specification.
     *
     * @param flag the flag to set the state for.
     * @param state the state to set for the flag.
     * @param specification the specification to set the state for.
     */
    void setFlag(
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state,
            final String specification
    );

    /**
     * Returns the flag state for the given flag.
     *
     * @param flag the flag to get the state for.
     * @return the flag state for the given flag.
     */
    RegionEnums.FlagState getFlagState(final RegionEnums.Flags flag);

    /**
     * Returns the flag state for the given flag and specification.
     * If no state with this flag and specification does exist,
     * it recalls and returns the flag state from the {@link #getFlagState(RegionEnums.Flags)}
     * method which does not use any specification.
     *
     * @param flag the flag to get the state for.
     * @param specification the type name to get the state for.
     * @return the flag state for the given flag and specification.
     */
    RegionEnums.FlagState getFlagState(final RegionEnums.Flags flag, final String specification);

    /**
     * Sets whether the region should ignore the Y coordinate when checking if a location is within the region.
     * @param ignoreY true if the region should ignore the Y coordinate, false otherwise.
     */
    void setIgnoreY(final boolean ignoreY);

    /**
     * Returns whether the region ignores the Y coordinate when checking if a location is within the region.
     * @return true if the region ignores the Y coordinate, false otherwise.
     */
    boolean doesIgnoreY();

    /**
     * Returns the center of the region.
     * @return the center of the region.
     */
    TinyLocation getCenter();

    /**
     * Returns region name.
     * @return the region name.
     */
    String getRegionName();

    /**
     * Returns region world name.
     * @return the world name.
     */
    String getWorldName();

    /**
     * Returns region world environment.
     * @return the world environment.
     */
    Environment getEnvironment();
}
