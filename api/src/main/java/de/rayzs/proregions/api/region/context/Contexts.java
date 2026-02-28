package de.rayzs.proregions.api.region.context;

import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import de.rayzs.proregions.api.utils.Permissions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.jspecify.annotations.Nullable;

public class Contexts {

    private Contexts() {}


    private static boolean hasBypassPermission(
            final Player player,
            final Region region,
            final RegionEnums.Flags flag,
            final @Nullable String specification
            ) {

        final String regionName = region.getRegionName().toLowerCase();

        if (Permissions.BYPASS_PERMISSION.hasPermission(player)) {
            return true;
        }


        final String flagName = flag.name().toLowerCase();

        if (Permissions.BYPASS_PERMISSION_REGION.hasPermission(
                player,
                regionName
        )) {
            return true;
        }


        if (specification != null) {
            if (Permissions.BYPASS_PERMISSION_REGION_FLAG_SPEC.hasPermission(
                    player,
                    regionName,
                    flagName,
                    specification.toLowerCase()
            )) {
                return true;
            }
        }

        return Permissions.BYPASS_PERMISSION_REGION_FLAG.hasPermission(
                player,
                regionName,
                flagName
        );
    }

    public static ContextEval<Player, Object, Object, Object> PLAYER = (
            region, flag,
            player,
            a, b, c) -> {

        if (hasBypassPermission(player, region, flag, null)) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag);
    };

    public static ContextEval<Entity, Object, Object, Object> ENTITY = (
            region, flag,
            entity,
            a, b, c) -> {

        return region.getFlagState(flag, entity.getType().name());
    };

    public static ContextEval<Block, Object, Object, Object> BLOCK = (
            region, flag,
            block,
            a, b, c) -> {

        return region.getFlagState(flag, block.getType().name());
    };

    public static ContextEval<Entity, Material, Object, Object> ENTITY_MATERIAL = (
            region, flag,
            entity, material,
            a, b) -> {

        return region.getFlagState(flag, material.name());
    };

    public static ContextEval<Player, Material, Object, Object> PLAYER_MATERIAL = (
            region, flag,
            player, material,
            a, b) -> {

        if (hasBypassPermission(player, region, flag, material.name())) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, material.name());
    };

    public static ContextEval<Player, Projectile, Object, Object> PLAYER_PROJECTILE = (
            region, flag,
            player, projectile,
            a, b) -> {

        if (hasBypassPermission(player, region, flag, projectile.getType().name())) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, projectile.getType().name());
    };

    public static ContextEval<@Nullable ProjectileSource, Projectile, Object, Object> PROJECTILE = (
            region, flag,
            source, projectile,
            a, b) -> {

        if (source instanceof Player player) {
            if (hasBypassPermission(player, region, flag, projectile.getType().name())) {
                return RegionEnums.FlagState.ALLOW;
            }
        }

        return region.getFlagState(flag, projectile.getType().name());
    };

    public static ContextEval<Player, Entity, Object, Object> PLAYER_ENTITY = (
            region, flag,
            player, entity,
            a, b) -> {

        if (hasBypassPermission(player, region, flag, entity.getType().name())) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, entity.getType().name());
    };

    public static ContextEval<Player, Entity, Object, Object> PLAYER_PLAYER = (
            region, flag,
            player, entity,
            a, b) -> {

        if (hasBypassPermission(player, region, flag, entity.getType().name())) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, entity.getType().name());
    };


}
