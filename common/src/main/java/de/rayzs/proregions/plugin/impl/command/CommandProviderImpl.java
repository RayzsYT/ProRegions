package de.rayzs.proregions.plugin.impl.command;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.command.Command;
import de.rayzs.proregions.api.command.CommandProvider;
import de.rayzs.proregions.api.configuration.Config;
import de.rayzs.proregions.api.message.MessageProvider;
import de.rayzs.proregions.plugin.impl.command.commands.*;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class CommandProviderImpl implements CommandProvider {

    private final Collection<Command> commands;

    private final ProRegionAPI api;
    private final Config config;

    private final MessageProvider messageProvider;

    public CommandProviderImpl(final ProRegionAPI api, final Config config) {
        this.api = api;
        this.config = config;

        this.messageProvider = api.getMessageProvider();

        this.commands = List.of(
                new CreateCommand(api),
                new DeleteCommand(api),
                new FlagCommand(api),
                new ListCommand(api),
                new InfoCommand(api),
                new ReloadCommand(api),
                new IgnoreYCommand(api)
        );
    }

    @Override
    public void reload() { }

    @Override
    public Command getCommand(@NonNull String commandStr) {
        Optional<Command> command = commands.stream().filter(cmd -> cmd.isCommand(commandStr)).findFirst();
        return command.orElse(null);
    }

    @Override
    public void handleExecution(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {
        final int length = args.length;

        final String unknownCommandMessage = this.messageProvider.get(
                "unknown-command",
                "&cCommand not found! Please use '/%label%' to receive a list of all available commands."
        );

        final String helpMessage = this.messageProvider.get(
                "help",
                "&cAvailable commands: /%label% create/delete/flag/response/ignore-y/exclude"
        );

        final String wrongUsage = this.messageProvider.get(
                "wrong-usage",
                "&cWronge usage! /%label% %usage%"
        );

        final String missingPermission = this.messageProvider.get(
                "missing-permission",
                "&cMissing permission %permission%!"
        );

        if (length == 0) {
            this.messageProvider.send(
                    sender, helpMessage,
                    "%label%", label
            );

            return;
        }

        final String commandStr = args[0];
        final Command command = getCommand(commandStr);

        if (command == null) {
            this.messageProvider.send(
                    sender, unknownCommandMessage,
                    "%label%", label
            );

            return;
        }

        final String permission = command.getPermission();
        if (!sender.hasPermission(permission)) {
            this.messageProvider.send(
                    sender, missingPermission,
                    "%permission%", permission
            );

            return;
        }

        final String[] newArgs = new String[length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);

        if (!command.onExecute(sender, label, newArgs)) {
            this.messageProvider.send(
                    sender, wrongUsage,
                    "%label%", label,
                    "%usage%", commandStr + (commandStr.isEmpty() ? "" : " " + command.getUsage())
            );
        }
    }

    @Override
    public List<String> handleTabCompletion(@NonNull CommandSender sender, @NonNull String[] args) {
        final int length = args.length;

        if (length == 1) {
            return commands.stream()
                    .filter(command -> sender.hasPermission(command.getPermission()))
                    .map(Command::getName).collect(Collectors.toList());
        }

        final String commandStr = args[0];
        final Command command = getCommand(commandStr);

        if (command == null || !sender.hasPermission(command.getPermission())) {
            return Collections.emptyList();
        }

        final String[] newArgs = new String[length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);

        return command.onTabComplete(sender, newArgs);
    }
}
