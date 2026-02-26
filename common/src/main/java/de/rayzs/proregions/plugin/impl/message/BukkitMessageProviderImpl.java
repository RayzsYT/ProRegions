package de.rayzs.proregions.plugin.impl.message;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.configuration.Config;
import de.rayzs.proregions.api.message.MessageProvider;
import de.rayzs.proregions.plugin.hook.PluginHooks;
import de.rayzs.proregions.plugin.hook.hooks.PlaceholderAPIHook;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;

public class BukkitMessageProviderImpl implements MessageProvider {

    private final ProRegionsAPI api;
    private final Config config;

    public BukkitMessageProviderImpl(final ProRegionsAPI api, final Config config) {
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
    public void sendMessage(final CommandSender sender, final String message) {
        sender.sendMessage(
                modify(sender, message)
        );
    }

    @Override
    public void sendMessage(final CommandSender sender, String message, final String... replacements) {
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

        sendMessage(sender, message);
    }

    @Override
    public void sendActionbar(final Player player, final String message) {
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(
                        modify(player, message)
                )
        );
    }

    @Override
    public void sendTitle(final Player player, final String title, final String subtitle) {
        player.sendTitle(
                modify(player, title),
                modify(player, subtitle)
        );
    }

    private String modify(final CommandSender sender, String text) {
        if (sender instanceof Player player) {
            text = PluginHooks.PLACEHOLDERAPI.modifyIfExist(text, (PlaceholderAPIHook hook, String input) ->
                    hook.replacePlaceholders(player, input)
            );
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
