package de.rayzs.proregions.plugin.impl.command.commands;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.command.Command;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrowableProjectile;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.List;

public class FlagCommand extends Command {

    public FlagCommand(final ProRegionAPI api) {
        super(api,
                "flag",
                "flag",
                "<region> <flag> <allow/deny> (specification)"
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

            api.getMessageProvider().sendMessage(
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

            api.getMessageProvider().sendMessage(sender, invalidFlagMessage);
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

            api.getMessageProvider().sendMessage(sender, invalidStateMessage);
            return true;
        }


        region.setFlag(flag, state);
        api.getRegionProvider().saveRegion(region);

        if (args.length == 3) {
            final String successMessage = api.getMessageProvider().get(
                    "flag.success",
                    "&aSuccessfully set &e%flag% &afor &e%region% &ato &e%state%&a!"
            );

            api.getMessageProvider().sendMessage(
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

        if (!flag.isSpecifiable()) {
            api.getMessageProvider().sendMessage(sender, doesNotHaveSpecificationMessage);
            return true;
        }

        String type = null;

        if (flag.getTargetType() == RegionEnums.FlagTargetType.LIQUID) {
            try {
                final Material material = Material.valueOf(args[3].toUpperCase());

                if (material != Material.LAVA && material != Material.WATER) {
                    final String invalidLiquidMessage = api.getMessageProvider().get(
                            "flag.specification.invalid-liquid",
                            "&cInvalid liquid! (Example: lava/water)"
                    );

                    api.getMessageProvider().sendMessage(sender, invalidLiquidMessage);
                    return true;
                }

                type = material.name().toLowerCase();
            } catch (IllegalArgumentException ignored) {}

        } else if (flag.getTargetType() == RegionEnums.FlagTargetType.ITEM) {
            try {
                final Material material = Material.valueOf(args[3].toUpperCase());

                if (material.isItem()) {
                    type = material.name().toLowerCase();
                }
            } catch (IllegalArgumentException ignored) {}

            if (type == null) {
                final String invalidMaterialMessage = api.getMessageProvider().get(
                        "flag.specification.invalid-item",
                        "&cInvalid item!"
                );

                api.getMessageProvider().sendMessage(sender, invalidMaterialMessage);
                return true;
            }

        } else if (flag.getTargetType() == RegionEnums.FlagTargetType.BLOCK) {
            try {
                final Material material = Material.valueOf(args[3].toUpperCase());

                if (material.isBlock()) {
                    type = material.name().toLowerCase();
                }
            } catch (IllegalArgumentException ignored) {}

            if (type == null) {
                final String invalidMaterialMessage = api.getMessageProvider().get(
                        "flag.specification.invalid-material",
                        "&cInvalid material!"
                );

                api.getMessageProvider().sendMessage(sender, invalidMaterialMessage);
                return true;
            }
        } else if (flag.getTargetType() == RegionEnums.FlagTargetType.PROJECTILE) {
            try {
                final EntityType projectile = EntityType.valueOf(args[3].toUpperCase());

                if (flag == RegionEnums.Flags.PROJECTILE) {

                    if (projectile == null || Arrays.stream(projectile.getEntityClass().getInterfaces()).anyMatch(i ->
                            !(i.equals(Projectile.class)
                                    || i.equals(ThrowableProjectile.class)
                                    || i.equals(AbstractArrow.class)
                            )
                    )){

                        final String invalidProjectileMessage = api.getMessageProvider().get(
                                "flag.specification.invalid-projectile",
                                "&cInvalid projectile! (Example: arrow)"
                        );

                        api.getMessageProvider().sendMessage(sender, invalidProjectileMessage);
                        return true;
                    }
                }

                type = projectile.name().toLowerCase();
            } catch (IllegalArgumentException ignored) {}

        } else if (flag.getTargetType() == RegionEnums.FlagTargetType.ENTITY) {
            try {
                type = EntityType.valueOf(args[3].toUpperCase()).name().toLowerCase();
            } catch (IllegalArgumentException ignored) {}

            if (type == null) {
                final String invalidEntityMessage = api.getMessageProvider().get(
                        "flag.specification.invalid-entity",
                        "&cInvalid entity!"
                );

                api.getMessageProvider().sendMessage(sender, invalidEntityMessage);
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

            api.getMessageProvider().sendMessage(
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

                if (flag.isSpecifiable()) {

                    if (flag == RegionEnums.Flags.FLOW) {
                        return List.of("lava", "water");
                    }

                    if (flag == RegionEnums.Flags.BUCKET_EMPTY || flag == RegionEnums.Flags.BUCKET_FILL) {
                        return List.of("powder_snow", "lava", "water");
                    }

                    if (flag.getTargetType() == RegionEnums.FlagTargetType.BLOCK) {
                        return Arrays.stream(Material.values()).filter(Material::isBlock)
                                .map(type -> type.name().toLowerCase()).toList();
                    }

                    if (flag.getTargetType() == RegionEnums.FlagTargetType.ITEM) {
                        return Arrays.stream(Material.values()).filter(Material::isItem)
                                .map(type -> type.name().toLowerCase()).toList();
                    }

                    if (flag.getTargetType() == RegionEnums.FlagTargetType.ENTITY) {
                        return Arrays.stream(EntityType.values())
                                .map(type -> type.name().toLowerCase()).toList();
                    }

                    if (flag.getTargetType() == RegionEnums.FlagTargetType.PROJECTILE) {
                        return Arrays.stream(EntityType.values())
                                .filter(type -> {
                                    if (type.getEntityClass() == null) {
                                        return false;
                                    }

                                    return Arrays.stream(type.getEntityClass().getInterfaces()).anyMatch(i ->
                                            i.equals(Projectile.class)
                                                    || i.equals(ThrowableProjectile.class)
                                                    || i.equals(AbstractArrow.class)
                                    );

                                }).map(type -> type.name().toLowerCase()).toList();
                    }

                }
            } catch (IllegalArgumentException ignored) {}
        }

        return List.of();
    }
}
