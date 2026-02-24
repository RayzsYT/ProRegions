package de.rayzs.proregions.plugin.impl.command.commands;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.clipboard.Clipboard;
import de.rayzs.proregions.api.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class CreateCommand extends Command {

    public CreateCommand(final ProRegionAPI api) {
        super(api,
                "create",
                "create",
                "<region>"
        );
    }

    @Override
    public boolean onExecute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {

        if (args.length != 1) {
            return false;
        }

        if (! (sender instanceof Player player)) {
            final String onlyPlayersMessage = api.getMessageProvider().get(
                    "messages.create.only-players",
                    "&cOnly players can execute this command!"
            );

            api.getMessageProvider().sendMessage(
                    sender,
                    onlyPlayersMessage
            );

            return true;
        }

        final Clipboard clipboard = api.getClipboardProvider().getClipboard(player.getUniqueId());

        if (!clipboard.isAvailable()) {
            final String noClipboardMessage = api.getMessageProvider().get(
                    " create.no-clipboard",
                    "&cYou need to select an area first! Use a stone axe to select an area."
            );

            api.getMessageProvider().sendMessage(
                    sender,
                    noClipboardMessage
            );

            return true;
        }

        final String regionName = args[0].toLowerCase();

        if (!api.getRegionProvider().createRegion(
                regionName,
                false,
                clipboard
        )) {
            final String alreadyExistMessage = api.getMessageProvider().get(
                    "create.already-exists",
                    "&cThere's already a region with that name!"
            );

            api.getMessageProvider().sendMessage(
                    sender,
                    alreadyExistMessage
            );

            return true;
        }

        clipboard.reset();

        final String successMessage = api.getMessageProvider().get(
                "create.success",
                "&aSuccessfully created the region %region%!"
        );

        api.getMessageProvider().sendMessage(
                sender, successMessage,
                "%region%", regionName
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull String[] args) {
        return List.of();
    }
}
