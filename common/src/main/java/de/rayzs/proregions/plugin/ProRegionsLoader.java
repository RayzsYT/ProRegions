package de.rayzs.proregions.plugin;

import de.rayzs.proregions.api.ProRegion;
import de.rayzs.proregions.api.utils.VersionHelper;
import de.rayzs.proregions.plugin.commands.ProRegionCommand;
import de.rayzs.proregions.plugin.hook.PluginHooks;
import de.rayzs.proregions.plugin.impl.ProRegionImpl;
import de.rayzs.proregions.plugin.impl.region.RegionImpl;
import de.rayzs.proregions.plugin.impl.response.ResponseImpl;
import de.rayzs.proregions.plugin.impl.world.TinyLocationImpl;
import de.rayzs.proregions.plugin.listeners.RegionListener;
import de.rayzs.proregions.plugin.listeners.WandListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ProRegionsLoader extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!VersionHelper.isSupported()) {
            getLogger().info("Plugin does not support this version of Minecraft! Please upgrade your server to at least " + VersionHelper.getMinSupportedVersion() + "!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        final long startTime = System.currentTimeMillis();


        // Creating default configuration file if there's none.
        createDefaultConfig();


        // Registers serializable class-objects for easier work around.
        ConfigurationSerialization.registerClass(TinyLocationImpl.class);
        ConfigurationSerialization.registerClass(RegionImpl.class);
        ConfigurationSerialization.registerClass(ResponseImpl.class);


        // Sets API implementation.
        final ProRegionImpl api = new ProRegionImpl(this);
        ProRegion.set(api);


        // Implements all hooks automatically.
        PluginHooks.values();


        // Registers listeners.
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new RegionListener(api), this);
        pluginManager.registerEvents(new WandListener(api), this);


        // Registers main command.
        final ProRegionCommand proRegionCommand = new ProRegionCommand(api);
        final PluginCommand pluginCommand = getCommand("proregions");

        pluginCommand.setExecutor(proRegionCommand);
        pluginCommand.setTabCompleter(proRegionCommand);


        final long endTime = System.currentTimeMillis() - startTime;
        final int loadedRegions = api.getRegionProvider().getRegions().size();
        getLogger().info("Loaded " + loadedRegions + " region" + ( loadedRegions > 1 ? "s" : "") + " in " + endTime + "ms.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    private void createDefaultConfig() {
        final File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveResource(configFile.getName(), false);
            getLogger().info("Successfully created default config.yml file!");
        }
    }
}
