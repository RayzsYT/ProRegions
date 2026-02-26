package de.rayzs.proregions.api.command;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.utils.Permissions;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.List;

public abstract class Command {

    protected final ProRegionsAPI api;

    private final String command;
    private final String permission;
    private final String usage;

    public Command(
            final ProRegionsAPI api,
            final String command,
            final String usage
    ) {
        this.api = api;
        this.command = command;
        this.permission = Permissions.COMMAND.getPermission(command);
        this.usage = usage;
    }

    /**
     * Handles the execution of this command.
     * Called from {@link de.rayzs.proregions.api.command.CommandProvider#handleExecution(CommandSender, String, String[])}.
     *
     * @param sender the sender of the command.
     * @param label the label of the command.
     * @param args the arguments of the command.
     * @return true if the command was executed successfully, false if the usage message should be sent to the sender.
     */
    public abstract boolean onExecute(
            @NonNull final CommandSender sender,
            @NonNull final String label,
            @NonNull final String[] args
    );

    /**
     * Handles the tab completion of this command.
     * Called from {@link de.rayzs.proregions.api.command.CommandProvider#handleTabCompletion(CommandSender, String[])}.
     *
     * @param sender the sender of the command.
     * @param args the arguments of the command.
     * @return a list of possible completions for the given arguments.
     */
    public abstract List<String> onTabComplete(
            @NonNull final CommandSender sender,
            @NonNull final String[] args
    );

    /**
     * Gets the name of this command.
     *
     * @return The name of the command.
     */
    public String getName() {
        return command;
    }

    /**
     * Gets the usage of this command.
     *
     * @return The usage of the command.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Checks if the given command matches this command.
     *
     * @param command the command to check.
     * @return true if the given command matches this command, false otherwise.
     */
    public boolean isCommand(String command) {

        // Get the first argument of the command.
        // For example, from "region create test" we get "region"
        command = command.split(" ", 1)[0];

        // Checks if the command matches.
        return this.command.equalsIgnoreCase(command);
    }

    /**
     * Gets the permission required to execute this command.
     *
     * @return The permission required to execute this command.
     */
    public String getPermission() {
        return this.permission;
    }
}
