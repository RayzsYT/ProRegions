package de.rayzs.proregions.plugin.impl.command.commands;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.command.Command;
import de.rayzs.proregions.api.region.Region;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class IgnoreYCommand extends Command {

    public IgnoreYCommand(final ProRegionsAPI api) {
        super(api,
                "ignore-y",
                "<region> <true/false>"
        );
    }

    @Override
    public boolean onExecute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {

        if (args.length != 2) {
            return false;
        }

        final String regionName = args[0].toLowerCase();
        final Region region = api.getRegionProvider().getRegion(regionName);

        if (region == null) {
            final String doesNotExistMessage = api.getMessageProvider().get(
                    "ignore-y.unknown-region",
                    "&cThere's no region with that name!"
            );

            api.getMessageProvider().sendMessage(
                    sender,
                    doesNotExistMessage
            );

            return true;
        }


        boolean ignoreY;
        if (args[1].equalsIgnoreCase("true")) {
            ignoreY = true;
        } else if (args[1].equalsIgnoreCase("false")) {
            ignoreY = false;
        } else {
            final String invalidValueMessage = api.getMessageProvider().get(
                    "ignore-y.invalid-bool",
                    "&cInvalid boolean for ignoreY. (possible: true/false)"
            );

            api.getMessageProvider().sendMessage(sender, invalidValueMessage);
            return true;
        }

        region.setIgnoreY(ignoreY);
        api.getRegionProvider().saveRegion(region);

        final String enabledMessage = api.getMessageProvider().get(
                "ignore-y.enabled",
                "&aRegion &e%region% &awill now ignore the Y-axis for its area."
        );

        final String disabledMessage = api.getMessageProvider().get(
                "ignore-y.disabled",
                "&aRegion 6e%region% &awill now include the Y-axis for its area."
        );

        api.getMessageProvider().sendMessage(
                sender, ignoreY ? enabledMessage : disabledMessage,
                "%region%", regionName
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull String[] args) {
        if (args.length == 1) {
            return api.getRegionProvider().getRegions().stream().map(Region::getRegionName).toList();
        }

        if (args.length == 2) {
            return List.of("true", "false");
        }

        return List.of();
    }
}
