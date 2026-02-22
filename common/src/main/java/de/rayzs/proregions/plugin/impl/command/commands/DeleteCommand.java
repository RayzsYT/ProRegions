package de.rayzs.proregions.plugin.impl.command.commands;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.clipboard.Clipboard;
import de.rayzs.proregions.api.command.Command;
import de.rayzs.proregions.api.region.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteCommand extends Command {

    public DeleteCommand(final ProRegionAPI api) {
        super(api,
                "delete",
                "delete",
                "<region>"
        );
    }

    @Override
    public boolean onExecute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {

        if (args.length != 1) {
            return false;
        }

        final String regionName = args[0].toLowerCase();

        if (!api.getRegionProvider().deleteRegion(regionName)) {
            final String doesNotExistMessage = api.getMessageProvider().get(
                    "delete.unknown-region",
                    "&cThere's no region with that name!"
            );

            api.getMessageProvider().send(
                    sender,
                    doesNotExistMessage
            );

            return true;
        }

        final String successMessage = api.getMessageProvider().get(
                "delete.success",
                "&aSuccessfully deleted the region %region%!"
        );

        api.getMessageProvider().send(
                sender, successMessage,
                "%region%", regionName
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull String[] args) {
        if (args.length > 1) {
            return List.of();
        }

        return api.getRegionProvider().getRegions().stream().map(Region::getRegionName).toList();
    }
}
