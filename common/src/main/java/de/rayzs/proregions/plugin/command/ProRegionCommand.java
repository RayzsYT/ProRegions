package de.rayzs.proregions.plugin.command;

import de.rayzs.proregions.api.ProRegionsAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class ProRegionCommand implements CommandExecutor, TabExecutor {

    private final ProRegionsAPI api;

    public ProRegionCommand(final ProRegionsAPI api) {
        this.api = api;
    }

    @Override
    public boolean onCommand(
            @NonNull final CommandSender sender,
            @NonNull final Command commandObj,
            @NonNull final String label,
            @NonNull final String[] args
    ) {
        api.getCommandProvider().handleExecution(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(
            @NonNull final CommandSender sender,
            @NonNull final Command command,
            @NonNull final String label,
            @NonNull final String[] args) {

        final List<String> suggestions = api.getCommandProvider().handleTabCompletion(sender, args);
        final int length = args.length;

        return suggestions.stream().filter(suggestion -> suggestion.toLowerCase().contains(args[Math.max(0, length - 1)].toLowerCase())).collect(Collectors.toList());
    }
}
