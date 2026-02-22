package de.rayzs.proregions.api.clipboard;

import org.bukkit.Location;
import org.jspecify.annotations.NonNull;

public class Clipboard {

    private Location firstLocation, secondLocation;

    public void setFirstLocation(@NonNull final Location location) {
        this.firstLocation = location;
    }

    public void setSecondLocation(@NonNull final Location location) {
        this.secondLocation = location;
    }

    public void reset() {
        this.firstLocation = null;
        this.secondLocation = null;
    }

    public boolean isAvailable() {
        return firstLocation != null && secondLocation != null;
    }

    public Location getFirstLocation() {
        return firstLocation;
    }

    public Location getSecondLocation() {
        return secondLocation;
    }
}
