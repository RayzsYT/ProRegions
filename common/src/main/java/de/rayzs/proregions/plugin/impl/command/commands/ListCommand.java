package de.rayzs.proregions.plugin.impl.command.commands;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.command.Command;
import de.rayzs.proregions.api.region.Region;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.Iterator;
import java.util.List;

public class ListCommand extends Command {

    public ListCommand(final ProRegionsAPI api) {
        super(api,
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

            api.getMessageProvider().sendMessage(
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

        api.getMessageProvider().sendMessage(
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
