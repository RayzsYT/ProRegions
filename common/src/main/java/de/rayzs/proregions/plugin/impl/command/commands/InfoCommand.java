package de.rayzs.proregions.plugin.impl.command.commands;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.command.Command;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InfoCommand extends Command {

    public InfoCommand(final ProRegionAPI api) {
        super(api,
                "info",
                "info",
                "<region>"
        );
    }

    @Override
    public boolean onExecute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {

        if (args.length != 1) {
            return false;
        }

        final String regionName = args[0].toLowerCase();
        final Region region = api.getRegionProvider().getRegion(regionName);

        if (region == null) {
            final String doesNotExistMessage = api.getMessageProvider().get(
                    "info.unknown-region",
                    "&cThere's no region with that name!"
            );

            api.getMessageProvider().send(
                    sender,
                    doesNotExistMessage
            );

            return true;
        }

        final String world = region.getWorldName();

        final List<RegionEnums.Flags> allowedFlags = new ArrayList<>();
        final List<RegionEnums.Flags> disallowedFlags = new ArrayList<>();

        for (final RegionEnums.Flags flag : RegionEnums.Flags.values()) {
            final RegionEnums.FlagState state = region.getFlagState(flag);

            if (state == RegionEnums.FlagState.ALLOW) {
                allowedFlags.add(flag);
            } else if (state == RegionEnums.FlagState.DENY) {
                disallowedFlags.add(flag);
            }
        }

        final Iterator<RegionEnums.Flags> allowedFlagsIterator = allowedFlags.iterator();
        final StringBuilder allowedFlagsText = new StringBuilder();

        final Iterator<RegionEnums.Flags> disallowedFlagsIterator = disallowedFlags.iterator();
        final StringBuilder disallowedFlagsText = new StringBuilder();

        while (allowedFlagsIterator.hasNext()) {
            final RegionEnums.Flags flag = allowedFlagsIterator.next();

            allowedFlagsText.append(flag.name().toLowerCase());

            if (allowedFlagsIterator.hasNext()) {
                allowedFlagsText.append(", ");
            }
        }

        while (disallowedFlagsIterator.hasNext()) {
            final RegionEnums.Flags flag = disallowedFlagsIterator.next();

            disallowedFlagsText.append(flag.name().toLowerCase());

            if (disallowedFlagsIterator.hasNext()) {
                disallowedFlagsText.append(", ");
            }
        }

        final String successMessage = api.getMessageProvider().get(
                "messages.info.message",
                "&7Name: %region%"
        );

        api.getMessageProvider().send(
                sender, successMessage,
                "%region%", regionName,
                "%allowed%", allowedFlagsText.toString(),
                "%denied%", disallowedFlagsText.toString(),
                "%world%", world
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
