package de.rayzs.proregions.api.message;

import org.bukkit.command.CommandSender;

public interface MessageProvider {

    void reload();

    String get(String path, final String defaultMessage);

    void send(
            final CommandSender sender,
            final String message
    );

    void send(
            final CommandSender sender,
            String message,
            final String... replacements
    );
}
