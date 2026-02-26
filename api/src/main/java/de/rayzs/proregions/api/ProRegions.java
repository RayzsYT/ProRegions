package de.rayzs.proregions.api;

public class ProRegions {

    private static ProRegionsAPI instance;


    /**
     * Sets the API instance.
     * This method should not be called by any other plugin or external code,
     * as it is intended to be used internally by the ProRegion plugin during
     * its initialization.
     *
     * @param api The ProRegionsAPI instance to set.
     */
    public static void set(ProRegionsAPI api) {

        if (instance != null) {
            throw new IllegalStateException("ProRegionsAPI instance is already set and should not be called twice!");
        }

        instance = api;
    }

    /**
     * Returns the initialized ProRegionsAPI instance.
     * In case it's not initialized yet, it will throw an IllegalStateException,
     * indicating that ProRegions is not loaded yet and should only be called once
     * ProRegions is actually loaded.
     *
     * @return The ProRegionsAPI instance.
     */
    public static ProRegionsAPI get() {
        if (instance == null) {
            throw new IllegalStateException("ProRegion is not loaded yet! Please ensure to only call it, once ProRegions is actually loaded.");
        }

        return instance;
    }


    private ProRegions() {}
}
