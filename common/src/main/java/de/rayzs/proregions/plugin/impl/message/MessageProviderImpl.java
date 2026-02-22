package de.rayzs.proregions.plugin.impl.message;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.configuration.Config;
import de.rayzs.proregions.api.message.MessageProvider;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class MessageProviderImpl implements MessageProvider {

    private final ProRegionAPI api;
    private final Config config;

    public MessageProviderImpl(final ProRegionAPI api, final Config config) {
        this.api = api;
        this.config = config;
    }

    // Path, Message
    private final HashMap<String, String> hashedMessages = new HashMap<>();

    // Path, Default message
    private final HashMap<String, String> hashedDefaultMessages = new HashMap<>();


    private final String defaultPath = "messages.";


    @Override
    public void reload() {
        config.reload();

        final Set<String> keys = hashedMessages.keySet();
        hashedMessages.clear();

        keys.forEach(path -> get(path.substring(defaultPath.length()), hashedDefaultMessages.get(path)));
    }

    @Override
    public String get(String path, final String defaultMessage) {
        path = defaultPath + path;

        String message = hashedMessages.get(path);

        if (message != null) {
            return message;
        }

        message = config.getOrSet(path, defaultMessage);

        hashedDefaultMessages.putIfAbsent(path, defaultMessage);
        hashedMessages.putIfAbsent(path, message);

        return message;
    }

    @Override
    public void send(final CommandSender sender, final String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @Override
    public void send(final CommandSender sender, String message, final String... replacements) {
        String key = null, val;

        for (String replacement : replacements) {

            if (key == null) {
                key = replacement;
                continue;
            }

            val = replacement;

            message = message.replace(key, val);
            key = null;
        }

        send(sender, message);
    }
}
