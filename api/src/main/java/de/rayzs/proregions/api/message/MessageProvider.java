package de.rayzs.proregions.api.message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface MessageProvider {

    void reload();

    String get(String path, final String defaultMessage);

    void sendTitle(
            final Player player,
            final String title,
            final String subtitle
    );

    void sendActionbar(
            final Player player,
            final String message
    );

    void sendMessage(
            final CommandSender sender,
            final String message
    );

    void sendMessage(
            final CommandSender sender,
            String message,
            final String... replacements
    );
}
