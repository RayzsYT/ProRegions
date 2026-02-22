package de.rayzs.proregions.api.region;

import de.rayzs.proregions.api.response.Response;
import de.rayzs.proregions.api.world.Environment;
import de.rayzs.proregions.api.world.TinyLocation;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public interface Region extends ConfigurationSerializable {

    boolean contains(final Entity entity);
    
    boolean contains(final Block block);

    boolean contains(final Location location);

    Response getDefaultResponse();
    Response getResponse(final RegionEnums.Flags flag);

    void unsetFlag(final RegionEnums.Flags flag);

    void unsetFlag(final RegionEnums.Flags flag, final String typeName);

    void setFlag(
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state
    );

    void setFlag(
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state,
            final String typeName
    );

    void setDefaultFlagState(final RegionEnums.FlagState defaultFlagState);

    RegionEnums.FlagState getFlagState(final RegionEnums.Flags flag);

    RegionEnums.FlagState getFlagState(final RegionEnums.Flags flag, final String typeName);

    void setIgnoreY(final boolean ignoreY);

    boolean doesIgnoreY();

    TinyLocation getCenter();

    String getRegionName();

    String getWorldName();

    Environment getEnvironment();
}
