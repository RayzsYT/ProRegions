package de.rayzs.proregions.plugin.impl.command.commands;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.command.Command;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import de.rayzs.proregions.api.response.Response;
import de.rayzs.proregions.api.response.ResponseType;
import de.rayzs.proregions.plugin.impl.region.RegionImpl;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResponseCommand extends Command {

    public ResponseCommand(final ProRegionsAPI api) {
        super(api,
                "response",
                "response",
                "<region> <set/unset> <default/flag> <type> <message>"
        );
    }

    @Override
    public boolean onExecute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {
        if (args.length < 4) {
            return false;
        }

        final String regionName = args[0].toLowerCase();
        final RegionImpl region = (RegionImpl) api.getRegionProvider().getRegion(regionName);

        if (region == null) {
            final String doesNotExistMessage = api.getMessageProvider().get(
                    "response.unknown-region",
                    "&cThere's no region with that name!"
            );

            api.getMessageProvider().sendMessage(
                    sender,
                    doesNotExistMessage
            );

            return true;
        }

        final boolean set = args[1].equalsIgnoreCase("set");
        if (!set && !args[1].equalsIgnoreCase("unset")) {
            return false;
        }

        RegionEnums.Flags flag = null;
        try {
            if (!args[2].equalsIgnoreCase("default")) {
                flag = RegionEnums.Flags.valueOf(args[2].toUpperCase());
            }
        } catch (IllegalArgumentException ignored) {
            final String invalidFlagMessage = api.getMessageProvider().get(
                    "response.invalid-flag",
                    "&cInvalid flag! (Example: break/place/interact...)"
            );

            api.getMessageProvider().sendMessage(sender, invalidFlagMessage);
            return true;
        }


        ResponseType type = null;
        try {
            type = ResponseType.valueOf(args[3].toUpperCase());
        } catch (IllegalArgumentException ignored) {
            final String invalidTypeMessage = api.getMessageProvider().get(
                    "response.invalid-type",
                    "&cInvalid response type! (Example: chat/title...)"
            );

            api.getMessageProvider().sendMessage(sender, invalidTypeMessage);
            return true;
        }

        final Response response = flag == null ? region.getDefaultResponse() : region.getOrCreateResponse(flag);

        if (!set) {
            switch (type) {
                case CHAT -> {
                    response.setChatMessage(null);
                }
                case TITLE -> {
                    response.setTitle(null);
                }
                case SUBTITLE -> {
                    response.setSubtitle(null);
                }
                case ACTIONBAR -> {
                    response.setActionbarMessage(null);
                }
                case SOUND -> {
                    response.setSound(null, 1.0, 1.0);
                }
            }

            api.getRegionProvider().saveRegion(region);

            final String successMessage = api.getMessageProvider().get(
                    "response.reset",
                    "&aReset %response% response for &e%flag% &aof region &e%region%&a."
            );

            api.getMessageProvider().sendMessage(sender, successMessage,
                    "%region%", regionName,
                    "%flag%", flag != null ? flag.name().toLowerCase() : "default",
                    "%response%", type.name().toLowerCase()
            );

            return true;
        }

        if (type == ResponseType.SOUND) {

            if (args.length != 7) {
                return false;
            }

            Sound sound;
            try {
                sound = Sound.valueOf(args[4].toUpperCase());
            } catch (IllegalArgumentException ignored) {
                final String invalidMessage = api.getMessageProvider().get(
                        "response.sound.invalid-sound",
                        "&cInvalid sound!"
                );

                api.getMessageProvider().sendMessage(sender, invalidMessage);
                return true;
            }

            double volume;
            try {
                volume = Double.parseDouble(args[5]);
            } catch (NumberFormatException e) {
                final String invalidMessage = api.getMessageProvider().get(
                        "response.sound.invalid-volume",
                        "&cInvalid volume! (0.0 - 10.0)"
                );

                api.getMessageProvider().sendMessage(sender, invalidMessage);
                return true;
            }

            double pitch;
            try {
                pitch = Double.parseDouble(args[6]);
            } catch (NumberFormatException e) {
                final String invalidMessage = api.getMessageProvider().get(
                        "response.sound.invalid-pitch",
                        "&cInvalid pitch! (0.0 - 2.0)"
                );

                api.getMessageProvider().sendMessage(sender, invalidMessage);
                return true;
            }

            response.setSound(sound, volume, pitch);
            api.getRegionProvider().saveRegion(region);

            final String successMessage = api.getMessageProvider().get(
                    "response.sound.success",
                    "&aSet sound for &e%flag% &aof region &e%region% &awith volume &e%volume% and pitch &e%pitch%&a."
            );

            api.getMessageProvider().sendMessage(sender, successMessage,
                    "%region%", regionName,
                    "%flag%", flag != null ? flag.name().toLowerCase() : "default",
                    "%volume%", String.valueOf(volume),
                    "%pitch%", String.valueOf(pitch)
            );

            return true;
        }

        final StringBuilder messageBuilder = new StringBuilder();
        for (int i = 4; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");
        }


        final String message = messageBuilder.toString().trim();

        switch (type) {
            case CHAT -> {
                response.setChatMessage(message);
            }
            case TITLE -> {
                response.setTitle(message);
            }
            case SUBTITLE -> {
                response.setSubtitle(message);
            }
            case ACTIONBAR -> {
                response.setActionbarMessage(message);
            }
        }

        api.getRegionProvider().saveRegion(region);

        final String successMessage = api.getMessageProvider().get(
                "response.success",
                "&aSet %response% for &e%flag% &aof region &e%region%&a."
        );

        api.getMessageProvider().sendMessage(sender, successMessage,
                "%region%", regionName,
                "%flag%", flag != null ? flag.name().toLowerCase() : "default",
                "%response%", type != null ? type.name().toLowerCase() : "default"
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull String[] args) {
        if (args.length == 1) {
            return api.getRegionProvider().getRegions().stream().map(Region::getRegionName).toList();
        }

        if (args.length == 2) {
            return List.of("set", "unset");
        }

        if (args.length == 3) {
            final List<String> list = new ArrayList<>(Arrays.stream(RegionEnums.Flags.values()).map(flag -> flag.name().toLowerCase()).toList());
            list.add("default");

            return list;
        }

        if (args.length == 4) {
            return List.of("chat", "actionbar", "title", "subtitle", "sound");
        }

        if (args.length > 4) {
            final String set = args[1];
            final String type = args[3];

            if (set.equalsIgnoreCase("set") && type.equalsIgnoreCase("sound")) {
                if (args.length == 5) {
                    return Arrays.stream(Sound.values()).map(sound -> sound.name().toLowerCase()).toList();
                }

                if (args.length == 6) {
                    return List.of("1.0", "10.0");
                }

                if (args.length == 7) {
                    return List.of("1.0", "2.0");
                }
            }

        }

        return List.of();
    }
}
