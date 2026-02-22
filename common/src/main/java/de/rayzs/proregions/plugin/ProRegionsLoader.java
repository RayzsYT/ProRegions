package de.rayzs.proregions.plugin;

import de.rayzs.proregions.api.ProRegion;
import de.rayzs.proregions.api.response.Response;
import de.rayzs.proregions.api.utils.ExpireCache;
import de.rayzs.proregions.api.world.TinyLocation;
import de.rayzs.proregions.plugin.commands.ProRegionCommand;
import de.rayzs.proregions.plugin.impl.ProRegionImpl;
import de.rayzs.proregions.plugin.impl.region.RegionImpl;
import de.rayzs.proregions.plugin.impl.response.ResponseImpl;
import de.rayzs.proregions.plugin.impl.world.TinyLocationImpl;
import de.rayzs.proregions.plugin.listeners.RegionListener;
import de.rayzs.proregions.plugin.listeners.WandListener;
import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ProRegionsLoader extends JavaPlugin {

    @Override
    public void onEnable() {
        final long startTime = System.currentTimeMillis();


        if (!getDataFolder().exists()) {
            saveResource("config.yml", false);
        }

        ConfigurationSerialization.registerClass(TinyLocationImpl.class);
        ConfigurationSerialization.registerClass(RegionImpl.class);
        ConfigurationSerialization.registerClass(ResponseImpl.class);


        final ProRegionImpl api = new ProRegionImpl(this);
        ProRegion.set(api);


        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new RegionListener(api), this);
        pluginManager.registerEvents(new WandListener(api), this);


        final ProRegionCommand proRegionCommand = new ProRegionCommand(api);
        final PluginCommand pluginCommand = getCommand("proregions");

        pluginCommand.setExecutor(proRegionCommand);
        pluginCommand.setTabCompleter(proRegionCommand);


        final long endTime = System.currentTimeMillis() - startTime;
        getLogger().info("Loaded " + api.getRegionProvider().getRegions().size() + " in " + endTime + "ms.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
