package de.rayzs.proregions.plugin.commands;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import de.rayzs.proregions.api.region.RegionProvider;
import de.rayzs.proregions.api.utils.ExpireCache;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.OldEnum;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class ProRegionCommand implements CommandExecutor, TabExecutor {

    private final ProRegionAPI api;

    public ProRegionCommand(final ProRegionAPI api) {
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
        /*
        for (int i = 0; i < args.length; i++)
            args[i] = args[i].toLowerCase();

        final int length = args.length;


        if (length > 0) {
            final String command = args[0];


            if (command.equalsIgnoreCase("reload")) {
                provider.reload();
                sender.sendMessage("Done!");
                return true;
            }


            if (command.equalsIgnoreCase("list")) {
                final Collection<Region> regions = provider.getRegions();

                if (regions.isEmpty()) {
                    sender.sendMessage("No regions.");
                    return true;
                }


                final Iterator<Region> iterator = provider.getRegions().iterator();
                final StringBuilder message = new StringBuilder("Regions: ");

                while (iterator.hasNext()) {
                    final Region region = iterator.next();

                    message.append(region.getRegionName());
                    if (iterator.hasNext()) {
                        message.append(", ");
                    }
                }

                sender.sendMessage(message.toString());
                return true;
            }


            if (command.equalsIgnoreCase("create")) {
                if (length != 2) {
                    sender.sendMessage("Usage: /" + label + " create <name>");
                    return true;
                }

                if (! (sender instanceof Player player)) {
                    sender.sendMessage("Only players can use this command.");
                    return true;
                }

                final Location firstLocation = storedFirstPositions.get(player.getUniqueId());
                final Location secondLocation = storedSecondPositions.get(player.getUniqueId());

                if (firstLocation == null || secondLocation == null) {
                    sender.sendMessage("You need to select two locations first. Use a stone axe to select the area.");
                    return true;
                }

                if (!provider.createRegion(args[1], false, firstLocation, secondLocation)) {
                    sender.sendMessage("A region with this name already exist.");
                    return true;
                }

                storedFirstPositions.remove(player.getUniqueId());
                storedSecondPositions.remove(player.getUniqueId());

                sender.sendMessage("Region created.");
                return true;
            }


            if (command.equalsIgnoreCase("delete")) {
                if (length != 2) {
                    sender.sendMessage("Usage: /" + label + " delete <name>");
                    return true;
                }

                if (!provider.deleteRegion(args[1])) {
                    sender.sendMessage("Region not found.");
                    return true;
                }

                sender.sendMessage("Region deleted.");
                return true;
            }


            if (command.equalsIgnoreCase("defaultstate")) {
                if (length != 3) {
                    sender.sendMessage("Usage: /" + label + " defaultstate <region> <state>");
                    return true;
                }

                final Region region = provider.getRegion(args[1]);
                if (region == null) {
                    sender.sendMessage("Region not found.");
                    return true;
                }

                RegionEnums.FlagState state = null;
                try {
                    state = RegionEnums.FlagState.valueOf(args[2].toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    sender.sendMessage("Invalid flag state. (possible flag states: allow/deny)");
                    return true;
                }

                region.setDefaultFlagState(state);
                provider.saveRegion(region);

                sender.sendMessage("Set default flag state for region " + region.getRegionName() + " to \"" + state.name().toLowerCase() + "\".");
                return true;
            }


            if (command.equalsIgnoreCase("ignore-y")) {
                if (length != 3) {
                    sender.sendMessage("Usage: /" + label + " ignorey <region> true/false");
                    return true;
                }

                final Region region = provider.getRegion(args[1]);
                if (region == null) {
                    sender.sendMessage("Region not found.");
                    return true;
                }

                boolean ignoreY;
                if (args[2].equalsIgnoreCase("true")) {
                    ignoreY = true;
                } else if (args[2].equalsIgnoreCase("false")) {
                    ignoreY = false;
                } else {
                    sender.sendMessage("Invalid value for ignoreY. (possible values: true/false)");
                    return true;
                }

                region.setIgnoreY(ignoreY);
                provider.saveRegion(region);

                if (ignoreY) {
                    sender.sendMessage("Region " + region.getRegionName() + " will now ignore the Y-axis for its area.");
                } else {
                    sender.sendMessage("Region " + region.getRegionName() + " will now include the Y-axis for its area.");
                }

                return true;
            }


            if (command.equalsIgnoreCase("flag")) {
                if (length != 4) {
                    sender.sendMessage("Usage: /" + label + " flag <region> <flag> allow/deny/default");
                    return true;
                }

                final Region region = provider.getRegion(args[1]);
                if (region == null) {
                    sender.sendMessage("Region not found.");
                    return true;
                }

                RegionEnums.Flags flag = null;
                try {
                    flag = RegionEnums.Flags.valueOf(args[2].toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    sender.sendMessage("Invalid flag. (possible flag states: allow/deny/default)");
                    return true;
                }

                if (args[3].equalsIgnoreCase("default")) {
                    region.unsetFlag(flag);

                    sender.sendMessage("Flag " + flag.name().toLowerCase() + " for region " + region.getRegionName() + " has been reset.");
                    return true;
                }

                RegionEnums.FlagState state = null;
                try {
                    state = RegionEnums.FlagState.valueOf(args[3].toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    sender.sendMessage("Invalid flag state. (possible flag states: allow/deny/default)");
                    return true;
                }

                region.setFlag(flag, state);
                provider.saveRegion(region);

                sender.sendMessage("Flag " + flag.name().toLowerCase() + " for region " + region.getRegionName() + " has been " + (state == RegionEnums.FlagState.ALLOW ? "allowed" : "denied") + ".");
                return true;
            }


            if (command.equalsIgnoreCase("response")) {

                if (length < 4) {
                    sender.sendMessage("Usage: /" + label + " response <region> <flag/default> <type> <message>");
                    return true;
                }

                final RegionImpl region = (RegionImpl) provider.getRegion(args[1]);
                if (region == null) {
                    sender.sendMessage("Region not found.");
                    return true;
                }


                RegionEnums.Flags flag = null;
                try {
                    if (!args[2].equalsIgnoreCase("default")) {
                        flag = RegionEnums.Flags.valueOf(args[2].toUpperCase());
                    }
                } catch (IllegalArgumentException ignored) {
                    sender.sendMessage("Invalid flag.");
                    return true;
                }


                final String responseType = args[3];

                if (responseType.equalsIgnoreCase("chat")) {
                    if (length == 4) {
                        sender.sendMessage("Usage: /" + label + " response <region> <flag/default> chat <message>");
                        return true;
                    }

                    StringBuilder message = new StringBuilder();
                    for (int i = 4; i < length; i++) {
                        message.append(args[i]).append(" ");
                    }

                    (flag != null ? region.getOrCreateResponse(flag) : region.getDefaultResponse()).setChatMessage(message.toString().trim());
                    provider.saveRegion(region);

                    sender.sendMessage("Set chat message for " + (flag != null ? "flag " + flag.name().toLowerCase() : "default") + " of region " + region.getRegionName() + ".");
                    return true;
                }

                if (responseType.equalsIgnoreCase("actionbar")) {
                    if (length == 4) {
                        sender.sendMessage("Usage: /" + label + " response <region> <flag/default> actionbar <message>");
                        return true;
                    }

                    StringBuilder message = new StringBuilder();
                    for (int i = 4; i < length; i++) {
                        message.append(args[i]).append(" ");
                    }

                    (flag != null ? region.getOrCreateResponse(flag) : region.getDefaultResponse()).setActionbarMessage(message.toString().trim());
                    provider.saveRegion(region);

                    sender.sendMessage("Set actionbar message for " + (flag != null ? "flag " + flag.name().toLowerCase() : "default") + " of region " + region.getRegionName() + ".");
                    return true;
                }

                if (responseType.equalsIgnoreCase("title")) {
                    if (length == 4) {
                        sender.sendMessage("Usage: /" + label + " response <region> <flag/default> title <title-text>");
                        return true;
                    }

                    StringBuilder message = new StringBuilder();
                    for (int i = 4; i < length; i++) {
                        message.append(args[i]).append(" ");
                    }

                    (flag != null ? region.getOrCreateResponse(flag) : region.getDefaultResponse()).setTitle(message.toString().trim());
                    provider.saveRegion(region);

                    sender.sendMessage("Set title for " + (flag != null ? "flag " + flag.name().toLowerCase() : "default") + " of region " + region.getRegionName() + ".");
                    return true;
                }

                if (responseType.equalsIgnoreCase("subtitle")) {
                    if (length == 4) {
                        sender.sendMessage("Usage: /" + label + " response <region> <flag/default> subtitle <subtitle-text>");
                        return true;
                    }

                    StringBuilder message = new StringBuilder();
                    for (int i = 4; i < length; i++) {
                        message.append(args[i]).append(" ");
                    }

                    (flag != null ? region.getOrCreateResponse(flag) : region.getDefaultResponse()).setChatMessage(message.toString().trim());
                    provider.saveRegion(region);

                    sender.sendMessage("Set subtitle for " + (flag != null ? "flag " + flag.name().toLowerCase() : "default") + " of region " + region.getRegionName() + ".");
                    return true;
                }

                if (responseType.equalsIgnoreCase("sound")) {
                    if (length < 6) {
                        sender.sendMessage("Usage: /" + label + " response <region> <flag/default> sound <value> <pitch>");
                        return true;
                    }

                    Sound sound = null;
                    try {
                        sound = Sound.valueOf(args[4].toUpperCase());
                    } catch (IllegalArgumentException ignored) {
                        sender.sendMessage("Invalid sound.");
                        return true;
                    }

                    double volume = 0;
                    try {
                        volume = Double.parseDouble(args[5]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("Invalid volume. Volume must be a number. (0.0 - 10.0)");
                        return true;
                    }

                    double pitch = 0;
                    try {
                        pitch = Double.parseDouble(args[6]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("Invalid pitch. Pitch must be a number. (0.0 - 2.0)");
                        return true;
                    }

                    (flag != null ? region.getOrCreateResponse(flag) : region.getDefaultResponse()).setSound(sound, volume, pitch);
                    provider.saveRegion(region);

                    sender.sendMessage("Set sound for " + (flag != null ? "flag " + flag.name().toLowerCase() : "default") + " of region " + region.getRegionName() + ".");
                    return true;
                }

                sender.sendMessage("Invalid response type. (possible types: chat/actionbar/title/subtitle/sound)");

            }
        }


        sender.sendMessage("Available commands:");
        sender.sendMessage("/" + label + " reload");
        sender.sendMessage("/" + label + " list");
        sender.sendMessage("/" + label + " create <name>");
        sender.sendMessage("/" + label + " delete <name>");
        sender.sendMessage("/" + label + " defaultstate <region> <state>");
        sender.sendMessage("/" + label + " ignorey <region> <bool>");
        sender.sendMessage("/" + label + " flag <region> <flag> <state>");
        sender.sendMessage("/" + label + " response <region> <flag/default> <type> <value>");

        return true;

         */
    }

    @Override
    public List<String> onTabComplete(
            @NonNull final CommandSender sender,
            @NonNull final Command command,
            @NonNull final String label,
            @NonNull final String[] args) {

        final List<String> suggestions = api.getCommandProvider().handleTabCompletion(sender, args);
        final int length = args.length;

        /*
        if (length <= 1) {
            suggestions.add("reload");
            suggestions.add("list");
            suggestions.add("create");
            suggestions.add("delete");
            suggestions.add("response");
            suggestions.add("defaultstate");
            suggestions.add("ignore-y");
            suggestions.add("flag");

        } else if (length == 2) {
            if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("defaultstate") || args[0].equalsIgnoreCase("ignore-y") || args[0].equalsIgnoreCase("flag")) {
                suggestions.addAll(provider.getRegions().stream().map(Region::getRegionName).toList());
            } else if (args[0].equalsIgnoreCase("response")) {
                suggestions.addAll(provider.getRegions().stream().map(Region::getRegionName).toList());
            }

        } else if (length == 3) {
            if (args[0].equalsIgnoreCase("defaultstate")) {
                suggestions.add("allow");
                suggestions.add("deny");
            } else if (args[0].equalsIgnoreCase("ignore-y")) {
                suggestions.add("true");
                suggestions.add("false");
            } else if (args[0].equalsIgnoreCase("flag")) {
                for (RegionEnums.Flags flag : RegionEnums.Flags.values()) {
                    suggestions.add(flag.name().toLowerCase());
                }
            } else if (args[0].equalsIgnoreCase("response")) {
                suggestions.add("default");
                for (RegionEnums.Flags flag : RegionEnums.Flags.values()) {
                    suggestions.add(flag.name().toLowerCase());
                }
            }

        } else if (length == 4) {
            if (args[0].equalsIgnoreCase("flag")) {
                suggestions.add("allow");
                suggestions.add("deny");
            } else if (args[0].equalsIgnoreCase("response")) {
                suggestions.add("chat");
                suggestions.add("actionbar");
                suggestions.add("title");
                suggestions.add("subtitle");
                suggestions.add("sound");
            }

        } else if (length == 5) {
            if (args[0].equalsIgnoreCase("response") && args[3].equalsIgnoreCase("sound")) {
                suggestions.addAll(List.of(Sound.values()).stream().map(OldEnum::name).map(String::toLowerCase).toList());
            }

        } else if (length == 6) {
            if (args[0].equalsIgnoreCase("response") && args[3].equalsIgnoreCase("sound")) {
                suggestions.add("1.0");
            }

        } else if (length == 7) {
            if (args[0].equalsIgnoreCase("response") && args[3].equalsIgnoreCase("sound")) {
                suggestions.add("1.0");
            }
        }
        */

        return suggestions.stream().filter(suggestion -> suggestion.toLowerCase().contains(args[Math.max(0, length - 1)].toLowerCase())).collect(Collectors.toList());
    }
}
