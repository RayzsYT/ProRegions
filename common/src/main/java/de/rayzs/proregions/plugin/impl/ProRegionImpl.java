package de.rayzs.proregions.plugin.impl;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.configuration.ConfigProvider;
import de.rayzs.proregions.api.region.RegionProvider;
import de.rayzs.proregions.api.world.TinyLocation;
import de.rayzs.proregions.plugin.impl.configuration.ConfigProviderImpl;
import de.rayzs.proregions.plugin.impl.region.RegionProviderImpl;
import de.rayzs.proregions.plugin.impl.world.TinyLocationImpl;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ProRegionImpl implements ProRegionAPI {

    private final Logger logger;
    private final JavaPlugin plugin;

    private final RegionProvider regionProvider;
    private final ConfigProvider configProvider;

    public ProRegionImpl(final JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        this.logger = javaPlugin.getLogger();

        this.configProvider = new ConfigProviderImpl();
        this.regionProvider = new RegionProviderImpl(
                this,
                this.configProvider.getOrCreate("regions")
        );
    }

    @Override
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public RegionProvider getRegionProvider() {
        return this.regionProvider;
    }

    @Override
    public ConfigProvider getConfigProvider() {
        return this.configProvider;
    }

    @Override
    public TinyLocation toTinyLocation(Location location) {
        return new TinyLocationImpl(
                location.getWorld() != null ? location.getWorld().getName() : "world",
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }
}
