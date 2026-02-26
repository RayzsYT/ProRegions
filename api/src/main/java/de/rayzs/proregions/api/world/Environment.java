package de.rayzs.proregions.api.world;

import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Simplified version of the Bukkit world environment,
 * designed to the environment ids by order starting with 0
 * with some additional utility methods.
 */
public enum Environment {

    NORMAL      (0, World.Environment.NORMAL),
    NETHER      (1, World.Environment.NETHER),
    END         (2, World.Environment.THE_END),
    CUSTOM      (3, World.Environment.CUSTOM),
    INVALID     (999, null);

    private final int id;
    private final World.Environment bukkitEnvironment;

    Environment(
            final int id,
            final World.Environment bukkitEnvironment
    ) {
        this.id = id;
        this.bukkitEnvironment = bukkitEnvironment;
    }

    /**
     * Returns the id of the environment.
     *
     * @return the environment id.
     */
    public int getId() {
        return id;
    }

    /**
     * Checks if the given world is in the same environment as this environment.
     *
     * @param world the world to check.
     * @return true if the world is in the same environment as this environment, false otherwise.
     */
    public boolean isSame(final World world) {
        return getEnvironmentByWorld(world) == this;
    }

    /**
     * Checks if the given environment is the same as this environment.
     *
     * @param environment the environment to check.
     * @return true if the given environment is the same as this environment, false otherwise.
     */
    public boolean isSame(final Environment environment) {
        return environment == this;
    }



    // ID -> Environment
    private static final Map<Integer, Environment> ID_MAP = new HashMap<>();

    // Bukkit environment -> Environment
    private static final Map<World.Environment, Environment> ENV_MAP = new HashMap<>();

    static {
        for (Environment environment : Environment.values()) {
            Environment.ID_MAP.put(environment.getId(), environment);

            if (environment.bukkitEnvironment != null) {
                Environment.ENV_MAP.put(environment.bukkitEnvironment, environment);
            }
        }
    }

    /**
     * Returns the environment of the given world.
     *
     * @param world the world to get the environment from.
     * @return the environment of the given world.
     */
    public static Environment getEnvironmentByWorld(final World world) {
        if (world == null) {
            return Environment.INVALID;
        }

        return Environment.ENV_MAP.getOrDefault(world.getEnvironment(), Environment.INVALID);
    }

    /**
     * Returns the environment with the given id.
     *
     * @param id the id of the environment to get.
     * @return the environment with the given id.
     */
    public static Environment getEnvironmentById(final int id) {
        return Environment.ID_MAP.getOrDefault(id, Environment.INVALID);
    }
}
