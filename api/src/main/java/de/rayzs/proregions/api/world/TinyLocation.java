package de.rayzs.proregions.api.world;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface TinyLocation extends ConfigurationSerializable {

    String worldName();

    int x();
    int y();
    int z();

}
