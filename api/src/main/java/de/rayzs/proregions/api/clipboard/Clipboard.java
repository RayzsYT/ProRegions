package de.rayzs.proregions.api.clipboard;

import org.bukkit.Location;
import org.jspecify.annotations.NonNull;

public class Clipboard {

    private Location firstLocation, secondLocation;

    /**
     * Sets the first location in the clipboard.
     *
     * @param location the location to set as the first location in the clipboard.
     */
    public void setFirstLocation(@NonNull final Location location) {
        this.firstLocation = location;
    }

    /**
     * Sets the second location in the clipboard.
     *
     * @param location the location to set as the second location in the clipboard.
     */
    public void setSecondLocation(@NonNull final Location location) {
        this.secondLocation = location;
    }

    /**
     * Resets the clipboard, clearing both locations.
     */
    public void reset() {
        this.firstLocation = null;
        this.secondLocation = null;
    }

    /**
     * Checks if both locations are set.
     *
     * @return true if both locations are set, false otherwise.
     */
    public boolean isAvailable() {
        return firstLocation != null && secondLocation != null;
    }

    /**
     * Returns the first location.
     *
     * @return the first location, or null if it is not set.
     */
    public Location getFirstLocation() {
        return firstLocation;
    }

    /**
     * Returns the second location.
     *
     * @return the second location, or null if it is not set.
     */
    public Location getSecondLocation() {
        return secondLocation;
    }
}
