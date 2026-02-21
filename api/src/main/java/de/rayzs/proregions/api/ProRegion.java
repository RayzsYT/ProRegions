package de.rayzs.proregions.api;

public class ProRegion {

    private static ProRegionAPI instance;

    public static void set(ProRegionAPI api) {

        if (instance != null) {
            throw new IllegalStateException("API instance is already set!");
        }

        instance = api;
    }

    public static ProRegionAPI get() {
        if (instance == null) {
            throw new IllegalStateException("Plugin is not loaded yet!");
        }

        return instance;
    }


    private ProRegion() {}
}
