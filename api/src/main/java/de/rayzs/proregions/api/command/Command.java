package de.rayzs.proregions.api.command;

import de.rayzs.proregions.api.ProRegionAPI;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.List;

public abstract class Command {

    protected final ProRegionAPI api;

    private final String command;
    private final String permission;
    private final String usage;

    public Command(final ProRegionAPI api, final String command, final String permission, final String usage) {
        this.api = api;
        this.command = command;
        this.permission = permission;
        this.usage = usage;
    }

    public abstract boolean onExecute(
            @NonNull final CommandSender sender,
            @NonNull final String label,
            @NonNull final String[] args
    );

    public abstract List<String> onTabComplete(
            @NonNull final CommandSender sender,
            @NonNull final String[] args
    );

    public String getName() {
        return command;
    }

    public String getUsage() {
        return usage;
    }

    public boolean isCommand(String command) {
        command = command.split(" ", 1)[0];
        return this.command.equalsIgnoreCase(command);
    }

    public String getPermission() {
        return this.permission;
    }
}
