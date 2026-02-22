package de.rayzs.proregions.api.command;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.List;

public interface CommandProvider {

    void reload();

    Command getCommand(@NonNull String command);

    void handleExecution(
            @NonNull final CommandSender sender,
            @NonNull final String label,
            @NonNull final String[] args
    );

    List<String> handleTabCompletion(
            @NonNull final CommandSender sender,
            @NonNull final String[] args
    );
}
