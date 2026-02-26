package de.rayzs.proregions.api.world;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Simplified version of a Location without the need of a World object.
 * This way, a location can be stored in a simplified way without the
 * need to load the world of the location first.
 */
public interface TinyLocation extends ConfigurationSerializable {

    /**
     * Returns location's world name
     *
     * @return the name of the word.
     */
    String worldName();

    /**
     * Returns the x coordinate of the location.
     *
     * @return the x coordinate of the location.
     */
    int x();

    /**
     * Returns the y coordinate of the location.
     *
     * @return the y coordinate of the location.
     */
    int y();

    /**
     * Returns the z coordinate of the location.
     *
     * @return the z coordinate of the location.
     */
    int z();

}
