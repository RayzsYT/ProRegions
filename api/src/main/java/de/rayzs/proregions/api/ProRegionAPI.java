package de.rayzs.proregions.api;

import de.rayzs.proregions.api.clipboard.Clipboard;
import de.rayzs.proregions.api.clipboard.ClipboardProvider;
import de.rayzs.proregions.api.command.CommandProvider;
import de.rayzs.proregions.api.configuration.ConfigProvider;
import de.rayzs.proregions.api.message.MessageProvider;
import de.rayzs.proregions.api.region.RegionProvider;
import de.rayzs.proregions.api.world.TinyLocation;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public interface ProRegionAPI {

    JavaPlugin getPlugin();
    Logger getLogger();

    void reload();

    ConfigProvider getConfigProvider();
    MessageProvider getMessageProvider();
    ClipboardProvider getClipboardProvider();
    RegionProvider getRegionProvider();
    CommandProvider getCommandProvider();

    TinyLocation toTinyLocation(Location location);
}
