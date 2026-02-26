package de.rayzs.proregions.api.utils;

import org.bukkit.Bukkit;
import java.util.*;

public class VersionHelper {

    private static final Version MIN_SUPPORTED_VERSION = Version.values()[1];
    private static final Version CURRENT_VERSION;

    // An immutable map that maps each version to its sum value for easier comparison.
    private static final Map<Version, Integer> VERSION_MAP = new HashMap<>();

    static {
        final Version[] versions = Version.values();

        // Starts from index 2 to skip the first two versions (UNSUPPORTED and the minimum supported version).
        for (int i = 2; i < versions.length; i++) {
            final Version version = versions[i];
            VERSION_MAP.put(version, convertIntoVal(version.name()));
        }

        // Gets the server version from Bukkit and converts it into the corresponding enum value.
        final String serverVersion = Bukkit.getBukkitVersion()
                .split("-")[0]
                .replace(".", "_");

        Version version;
        try {
            version = Version.valueOf("V" + serverVersion);
        } catch (Exception exception) {
            version = Version.UNSUPPORTED;
        }

        CURRENT_VERSION = version;
    }

    /**
     * Gets the current Minecraft server version that the server is running on.
     *
     * @return the current Minecraft server version that the server is running on.
     */
    public static Version getCurrentVersion() {
        return CURRENT_VERSION;
    }

    /**
     * Gets the minimum supported version of this plugin.
     *
     * @return the minimum supported version of this plugin.
     */
    public static Version getMinSupportedVersion() {
        return MIN_SUPPORTED_VERSION;
    }

    /**
     * Checks if the current version is supported by this plugin.
     * This is determined by if the version is below the minimum support version,
     * or above the maximum supported version.
     *
     * @return true if the current version is supported, false otherwise.
     */
    public static boolean isSupported() {
        return CURRENT_VERSION != Version.UNSUPPORTED;
    }

    /**
     * Checks if the current version is between the given minimum and maximum versions.
     *
     * @param minVersion the minimum version.
     * @param maxVersion the maximum version.
     * @return true if the current version is between the given minimum and maximum versions, false otherwise.
     */
    public static boolean isBetween(final Version minVersion, final Version maxVersion) {
        final int currentVal = VERSION_MAP.get(CURRENT_VERSION);
        return minVersion.toValue() <= currentVal && currentVal <= maxVersion.toValue();
    }

    /**
     * Checks if the current version is below the given version.
     *
     * @param version the version to compare with.
     * @return true if the current version is below the given version, false otherwise.
     */
    public static boolean isBelow(final Version version) {
        return CURRENT_VERSION.toValue() < version.toValue();
    }

    /**
     * Checks if the current version is at least the given version.
     *
     * @param version the version to compare with.
     * @return true if the current version is at least the given version, false otherwise.
     */
    public static boolean isAtLeast(final Version version) {
        return CURRENT_VERSION.toValue() >= version.toValue();
    }

    /**
     * Checks if the current version is higher than the given version.
     *
     * @param version the version to compare with.
     * @return true if the current version is higher than the given version, false otherwise.
     */
    public static boolean isHigher(final Version version) {
        return CURRENT_VERSION.toValue() > version.toValue();
    }

    /**
     * Converts a version name (e.g. "V1_16_5", "1.16.5")
     * into an integer value for easier comparison.
     *
     * @param versionName the version name to convert.
     * @return the integer value of the version name.
     */
    private static int convertIntoVal(String versionName) {
        // Replaces all underscores with dots..
        versionName = versionName.replace("_", ".");

        // Removes the first letter in case it starts with 'V'.
        if (versionName.startsWith("V")) {
            versionName = versionName.substring(1);
        }

        // Split it into parts.
        final String[] parts = versionName.split("\\.");

        // Converts the parts into integers to calculate its sum, if it is present.
        final int major = parts.length < 1 ? 0 : Integer.parseInt(parts[0]) * 10000;
        final int minor = parts.length < 2 ? 0 : Integer.parseInt(parts[1]) * 100;
        final int patch = parts.length < 3 ? 0 : Integer.parseInt(parts[2]);

        return major + minor + patch;
    }

    /**
     * Represents the supported Minecraft versions.
     * The order of the versions is important for the version comparison methods in this class.
     * The second version (index 1) is chosen as the minimum supported version.
     * All versions before that are considered unsupported.
     */
    public enum Version {
        UNSUPPORTED,

        V1_16, V1_16_1, V1_16_2,
        V1_16_3, V1_16_4, V1_16_5,

        V1_17, V1_17_1,

        V1_18, V1_18_1, V1_18_2,

        V1_19, V1_19_1, V1_19_2,
        V1_19_3, V1_19_4,

        V1_20, V1_20_1, V1_20_2,
        V1_20_3, V1_20_4, V1_20_5,
        V1_20_6,

        V1_21, V1_21_1, V1_21_2,
        V1_21_3, V1_21_4, V1_21_5,
        V1_21_6, V1_21_7, V1_21_8,
        V1_21_9, V1_21_10, V1_21_11;

        /**
         * Gets the integer value of this version for easier comparison.
         *
         * @return the integer value of this version.
         */
        public int toValue() {
            return VERSION_MAP.getOrDefault(this, 0);
        }
    }
}