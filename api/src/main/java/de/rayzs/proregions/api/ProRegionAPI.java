package de.rayzs.proregions.api;

import de.rayzs.proregions.api.configuration.ConfigProvider;
import de.rayzs.proregions.api.region.RegionProvider;
import de.rayzs.proregions.api.world.TinyLocation;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public interface ProRegionAPI {

    JavaPlugin getPlugin();
    Logger getLogger();

    RegionProvider getRegionProvider();
    ConfigProvider getConfigProvider();

    TinyLocation toTinyLocation(Location location);
}
