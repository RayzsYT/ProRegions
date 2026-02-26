package de.rayzs.proregions.api;

import de.rayzs.proregions.api.clipboard.ClipboardProvider;
import de.rayzs.proregions.api.command.CommandProvider;
import de.rayzs.proregions.api.configuration.ConfigProvider;
import de.rayzs.proregions.api.message.MessageProvider;
import de.rayzs.proregions.api.region.RegionProvider;
import de.rayzs.proregions.api.world.TinyLocation;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public interface ProRegionsAPI {

    /**
     * Returns the plugin instance of ProRegions.
     *
     * @return the plugin instance of ProRegions.
     */
    JavaPlugin getPlugin();

    /**
     * Returns the logger of ProRegions.
     *
     * @return the logger of ProRegions.
     */
    Logger getLogger();

    /**
     * Reloads the API, which means it reloads all providers and their data.
     */
    void reload();

    /**
     * Returns the config provider.
     *
     * @return the config provider.
     */
    ConfigProvider getConfigProvider();

    /**
     * Returns the message provider.
     * In there you can get messages from the config and send them to players.
     * You can also send titles and actionbar messages to players.
     *
     * @return the message provider.
     */
    MessageProvider getMessageProvider();

    /**
     * Returns the clipboard provider to manage player clipboards for their region area selection.
     *
     * @return the clipboard provider.
     */
    ClipboardProvider getClipboardProvider();

    /**
     * Returns the region provider to manage regions.
     * In there you can create, delete, save, manage regions.
     *
     * @return the region provider.
     */
    RegionProvider getRegionProvider();

    /**
     * Returns the command provider.
     * Not relevant for others.
     *
     * @return the command provider.
     */
    CommandProvider getCommandProvider();

    /**
     * Converts a Bukkit Location to a TinyLocation.
     *
     * @param location the location to convert.
     * @return the converted TinyLocation.
     */
    TinyLocation toTinyLocation(Location location);
}
