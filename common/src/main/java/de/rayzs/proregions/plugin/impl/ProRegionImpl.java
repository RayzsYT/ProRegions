package de.rayzs.proregions.plugin.impl;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.clipboard.ClipboardProvider;
import de.rayzs.proregions.api.command.CommandProvider;
import de.rayzs.proregions.api.configuration.Config;
import de.rayzs.proregions.api.configuration.ConfigProvider;
import de.rayzs.proregions.api.message.MessageProvider;
import de.rayzs.proregions.api.region.RegionProvider;
import de.rayzs.proregions.api.world.TinyLocation;
import de.rayzs.proregions.plugin.impl.clipboard.ClipboardProviderImpl;
import de.rayzs.proregions.plugin.impl.command.CommandProviderImpl;
import de.rayzs.proregions.plugin.impl.configuration.ConfigProviderImpl;
import de.rayzs.proregions.plugin.impl.message.BukkitMessageProviderImpl;
import de.rayzs.proregions.plugin.impl.message.PaperMessageProviderImpl;
import de.rayzs.proregions.plugin.impl.region.RegionProviderImpl;
import de.rayzs.proregions.plugin.impl.world.TinyLocationImpl;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ProRegionImpl implements ProRegionsAPI {

    private final Logger logger;
    private final JavaPlugin plugin;

    private final ConfigProvider configProvider;
    private final MessageProvider messageProvider;
    private final ClipboardProvider clipboardProvider;
    private final RegionProvider regionProvider;
    private final CommandProvider commandProvider;

    public ProRegionImpl(final JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        this.logger = javaPlugin.getLogger();

        this.configProvider = new ConfigProviderImpl();


        final Config regionsConfig = this.configProvider.getOrCreate("regions");
        final Config config = this.configProvider.getOrCreate("config");


        boolean supportMiniMessage = false;
        try {
            Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            supportMiniMessage = true;
        } catch (ClassNotFoundException ignored) { }


        this.messageProvider = supportMiniMessage
                ? new PaperMessageProviderImpl(this, config)
                : new BukkitMessageProviderImpl(this, config);

        this.clipboardProvider = new ClipboardProviderImpl();
        this.commandProvider = new CommandProviderImpl(this, config);
        this.regionProvider = new RegionProviderImpl(this, regionsConfig);
    }

    @Override
    public void reload() {
        this.configProvider.reload();
        this.messageProvider.reload();
        this.regionProvider.reload();
        this.commandProvider.reload();
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
    public ConfigProvider getConfigProvider() {
        return this.configProvider;
    }

    @Override
    public ClipboardProvider getClipboardProvider() {
        return this.clipboardProvider;
    }

    @Override
    public MessageProvider getMessageProvider() {
        return this.messageProvider;
    }

    @Override
    public CommandProvider getCommandProvider() {
        return this.commandProvider;
    }

    @Override
    public RegionProvider getRegionProvider() {
        return this.regionProvider;
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
