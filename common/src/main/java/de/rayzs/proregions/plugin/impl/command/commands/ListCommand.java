package de.rayzs.proregions.plugin.impl.command.commands;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.clipboard.Clipboard;
import de.rayzs.proregions.api.command.Command;
import de.rayzs.proregions.api.region.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ListCommand extends Command {

    public ListCommand(final ProRegionAPI api) {
        super(api,
                "list",
                "list",
                ""
        );
    }

    @Override
    public boolean onExecute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {
        final Iterator<Region> iterator = api.getRegionProvider().getRegions().iterator();

        if (!iterator.hasNext()) {
            final String emptyListMessage = api.getMessageProvider().get(
                    "list.empty",
                    "&cNo regions!"
            );

            api.getMessageProvider().send(
                    sender,
                    emptyListMessage
            );

            return true;
        }

        final String listMessage = api.getMessageProvider().get(
                "list.message",
                "&7Regions: &e%regions%"
        );

        final StringBuilder listBuilder = new StringBuilder();

        while (iterator.hasNext()) {
            final Region region = iterator.next();

            listBuilder.append(region.getRegionName());

            if (iterator.hasNext()) {
                listBuilder.append(", ");
            }
        }

        api.getMessageProvider().send(
                sender, listMessage,
                "%regions%", listBuilder.toString()
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull String[] args) {
        return List.of();
    }
}
