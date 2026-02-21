package de.rayzs.proregions.api.world;

import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public enum Environment {

    NORMAL      (0, World.Environment.NORMAL),
    NETHER      (1, World.Environment.NETHER),
    END         (2, World.Environment.THE_END),
    CUSTOM      (3, World.Environment.CUSTOM),
    INVALID     (999, null);

    private final int id;
    private final World.Environment bukkitEnvironment;

    Environment(final int id, final World.Environment bukkitEnvironment) {
        this.id = id;
        this.bukkitEnvironment = bukkitEnvironment;
    }

    public int getId() {
        return id;
    }

    public boolean isSame(final World world) {
        return getEnvironmentByWorld(world) == this;
    }

    public boolean isSame(final Environment environment) {
        return environment == this;
    }


    private static final Map<Integer, Environment> ID_MAP = new HashMap<>();
    private static final Map<World.Environment, Environment> ENV_MAP = new HashMap<>();

    static {
        for (Environment environment : Environment.values()) {
            Environment.ID_MAP.put(environment.getId(), environment);

            if (environment.bukkitEnvironment != null) {
                Environment.ENV_MAP.put(environment.bukkitEnvironment, environment);
            }
        }
    }

    public static Environment getEnvironmentByWorld(final World world) {
        if (world == null) {
            return Environment.INVALID;
        }

        return Environment.ENV_MAP.getOrDefault(world.getEnvironment(), Environment.INVALID);
    }

    public static Environment getEnvironmentById(final int id) {
        return Environment.ID_MAP.getOrDefault(id, Environment.INVALID);
    }
}
