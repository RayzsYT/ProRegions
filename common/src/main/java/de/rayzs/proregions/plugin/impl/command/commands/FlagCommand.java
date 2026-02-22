package de.rayzs.proregions.plugin.impl.command.commands;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.command.Command;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FlagCommand extends Command {

    public FlagCommand(final ProRegionAPI api) {
        super(api,
                "flag",
                "flag",
                "<region> <flag> <allow/deny> (type)"
        );
    }

    @Override
    public boolean onExecute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {

        if (args.length < 3 || args.length > 4) {
            return false;
        }

        final String regionName = args[0].toLowerCase();
        final Region region = api.getRegionProvider().getRegion(regionName);

        if (region == null) {
            final String doesNotExistMessage = api.getMessageProvider().get(
                    "flag.unknown-region",
                    "&cThere's no region with that name!"
            );

            api.getMessageProvider().send(
                    sender,
                    doesNotExistMessage
            );

            return true;
        }

        RegionEnums.Flags flag;
        try {
            flag = RegionEnums.Flags.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException ignored) {
            final String invalidFlagMessage = api.getMessageProvider().get(
                    "flag.invalid-flag",
                    "&cInvalid flag! (Example: break/place/interact...)"
            );

            api.getMessageProvider().send(sender, invalidFlagMessage);
            return true;
        }

        RegionEnums.FlagState state;
        try {
            state = RegionEnums.FlagState.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException ignored) {
            final String invalidStateMessage = api.getMessageProvider().get(
                    "flag.invalid-state",
                    "&cInvalid state! (Example: allow/deny)"
            );

            api.getMessageProvider().send(sender, invalidStateMessage);
            return true;
        }


        region.setFlag(flag, state);
        api.getRegionProvider().saveRegion(region);

        if (args.length == 3) {
            final String successMessage = api.getMessageProvider().get(
                    "flag.success",
                    "&aSuccessfully set &e%flag% &afor &e%region% &ato &e%state%&a!"
            );

            api.getMessageProvider().send(
                    sender, successMessage,
                    "%region%", regionName,
                    "%flag%", flag.name().toLowerCase(),
                    "%state%", state.name().toLowerCase()
            );

            return true;
        }

        final String doesNotHaveSpecificationMessage = api.getMessageProvider().get(
                "flag.specification.no-specification",
                "&cThis flag does not contain any specification values!"
        );

        if (!flag.isExcludable()) {
            api.getMessageProvider().send(sender, doesNotHaveSpecificationMessage);
            return true;
        }

        String type = null;

        if (flag.getTargetType() == RegionEnums.FlagTargetType.BLOCK) {
            try {
                final Material material = Material.valueOf(args[3].toUpperCase());

                if (flag == RegionEnums.Flags.FLOW && material != Material.LAVA && material != Material.WATER) {
                    final String invalidLiquidMessage = api.getMessageProvider().get(
                            "flag.specification.invalid-liquid",
                            "&cInvalid liquid! (Example: lava/water)"
                    );

                    api.getMessageProvider().send(sender, invalidLiquidMessage);

                    return true;
                }

                if (material.isBlock()) {
                    type = material.name().toLowerCase();
                }
            } catch (IllegalArgumentException ignored) {}

            if (type == null) {
                final String invalidMaterialMessage = api.getMessageProvider().get(
                        "flag.specification.invalid-material",
                        "&cInvalid material!"
                );

                api.getMessageProvider().send(sender, invalidMaterialMessage);
                return true;
            }

        } else if (flag.getTargetType() == RegionEnums.FlagTargetType.ENTITY) {
            try {
                type = EntityType.valueOf(args[3].toUpperCase()).name().toLowerCase();
            } catch (IllegalArgumentException ignored) {}

            if (type == null) {
                final String invalidEntityMessage = api.getMessageProvider().get(
                        "flag.specification.invalid-entity",
                        "&cInvalid entity!"
                );

                api.getMessageProvider().send(sender, invalidEntityMessage);
                return true;
            }
        }

        if (type != null) {

            region.setFlag(flag, state, type);
            api.getRegionProvider().saveRegion(region);


            final String successMessage = api.getMessageProvider().get(
                    "flag.specification.success",
                    "&aSuccessfully set &e%flag% &afor &e%region% &aand the type &e%type% &ato &e%state%&a!"
            );

            api.getMessageProvider().send(
                    sender, successMessage,
                    "%region%", regionName,
                    "%flag%", flag.name().toLowerCase(),
                    "%state%", state.name().toLowerCase(),
                    "%type%", type
            );
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull String[] args) {
        if (args.length == 1) {
            return api.getRegionProvider().getRegions().stream().map(Region::getRegionName).toList();
        }

        if (args.length == 2) {
            return Arrays.stream(RegionEnums.Flags.values()).map(flag -> flag.name().toLowerCase()).toList();
        }

        if (args.length == 3) {
            return Arrays.stream(RegionEnums.FlagState.values()).map(state -> state.name().toLowerCase()).toList();
        }

        if (args.length == 4) {
            try {
                final RegionEnums.Flags flag = RegionEnums.Flags.valueOf(args[1].toUpperCase());

                if (flag.isExcludable()) {

                    if (flag == RegionEnums.Flags.FLOW) {
                        return List.of("lava", "water");
                    }

                    if (flag.getTargetType() == RegionEnums.FlagTargetType.BLOCK) {
                        return Arrays.stream(Material.values()).filter(Material::isBlock).map(type -> type.name().toLowerCase()).toList();
                    } else if (flag.getTargetType() == RegionEnums.FlagTargetType.ENTITY) {
                        return Arrays.stream(EntityType.values()).map(type -> type.name().toLowerCase()).toList();
                    }

                }
            } catch (IllegalArgumentException ignored) {}
        }

        return List.of();
    }
}
