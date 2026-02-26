package de.rayzs.proregions.api.region.context;

import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.jspecify.annotations.Nullable;

public class Contexts {

    private Contexts() {}


    private static final String BYPASS_PERMISSION = "proregions.bypass";
    private static final String BYPASS_PERMISSION_FLAG = "proregions.bypass.%s";
    private static final String BYPASS_PERMISSION_FLAG_SPEC = "proregions.bypass.%s.%s";


    private static boolean hasBypassPermission(
            final Player player,
            final RegionEnums.Flags flag,
            final @Nullable String specification
            ) {

        if (player.hasPermission(BYPASS_PERMISSION)) {
            return true;
        }

        final String flagName = flag.name().toLowerCase();

        if (specification != null) {
            if (player.hasPermission(
                    String.format(
                            BYPASS_PERMISSION_FLAG_SPEC,
                            flagName,
                            specification.toLowerCase()
                    )
            )) {
                return true;
            }
        }

        return player.hasPermission(
                String.format(
                        BYPASS_PERMISSION_FLAG,
                        flagName
                )
        );
    }

    public static ContextEval<Player, Object, Object, Object> PLAYER = (
            region, flag,
            player,
            a, b, c) -> {

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

    public static ContextEval<Player, Material, Object, Object> PLAYER_ITEM = (
            region, flag,
            player, material,
            a, b) -> {

        if (hasBypassPermission(player, flag, material.name())) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, material.name());
    };

    public static ContextEval<Entity, Material, Object, Object> ENTITY_ITEM = (
            region, flag,
            entity, material,
            a, b) -> {

        return region.getFlagState(flag, material.name());
    };

    public static ContextEval<Player, Material, Object, Object> PLAYER_MATERIAL = (
            region, flag,
            player, material,
            a, b) -> {

        if (hasBypassPermission(player, flag, material.name())) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, material.name());
    };

    public static ContextEval<Player, Projectile, Object, Object> PLAYER_PROJECTILE = (
            region, flag,
            player, projectile,
            a, b) -> {

        if (hasBypassPermission(player, flag, projectile.getType().name())) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, projectile.getType().name());
    };

    public static ContextEval<@Nullable ProjectileSource, Projectile, Object, Object> PROJECTILE = (
            region, flag,
            player, projectile,
            a, b) -> {

        return region.getFlagState(flag, projectile.getType().name());
    };

    public static ContextEval<Player, Entity, Object, Object> PLAYER_ENTITY = (
            region, flag,
            player, entity,
            a, b) -> {

        if (hasBypassPermission(player, flag, entity.getType().name())) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, entity.getType().name());
    };

    public static ContextEval<Player, Entity, Object, Object> PLAYER_PLAYER = (
            region, flag,
            player, entity,
            a, b) -> {

        if (hasBypassPermission(player, flag, entity.getType().name())) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, entity.getType().name());
    };


}
