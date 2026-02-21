package de.rayzs.proregions.api.region;

import de.rayzs.proregions.api.response.Response;
import de.rayzs.proregions.api.world.Environment;
import de.rayzs.proregions.api.world.TinyLocation;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface Region extends ConfigurationSerializable {

    boolean contains(final Entity entity);
    
    boolean contains(final Block block);

    boolean contains(final Location location);

    Response getDefaultResponse();
    Response getResponse(final RegionEnums.Flags flag);

    void unsetFlag(final RegionEnums.Flags flag);
    void setFlag(final RegionEnums.Flags flag, final RegionEnums.FlagState state);

    void setDefaultFlagState(final RegionEnums.FlagState defaultFlagState);

    RegionEnums.FlagState getFlagState(final RegionEnums.Flags flag);

    void setIgnoreY(final boolean ignoreY);

    boolean doesIgnoreY();

    TinyLocation getCenter();

    String getRegionName();

    Environment getEnvironment();
}
