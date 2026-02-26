package de.rayzs.proregions.api.command;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.List;

public interface CommandProvider {

    /**
     * Reloads the command provider.
     */
    void reload();

    /**
     * Returns the command with the given name.
     *
     * @param command the name of the command to get.
     * @return the command with the given name, or null if no such command exists.
     */
    Command getCommand(@NonNull String command);

    /**
     * Handles the execution of a command.
     *
     * @param sender the sender of the command.
     * @param label the label of the command.
     * @param args the arguments of the command.
     */
    void handleExecution(
            @NonNull final CommandSender sender,
            @NonNull final String label,
            @NonNull final String[] args
    );

    /**
     * Handles the tab completion of a command.
     *
     * @param sender the sender of the command.
     * @param args the arguments of the command.
     * @return a list of possible completions for the command, or an empty list if no completions are available.
     */
    List<String> handleTabCompletion(
            @NonNull final CommandSender sender,
            @NonNull final String[] args
    );
}
