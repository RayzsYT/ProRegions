package de.rayzs.proregions.api.region.context;

import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Contexts {

    private Contexts() {}


    private static boolean hasBypassPermission(final Player player) {
        return player.hasPermission("proregions.bypass");
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

    public static ContextEval<Block, Object, Object, Object> BLOCK_CHANGE = (
            region, flag,
            block,
            a, b, c) -> {

        return region.getFlagState(flag, block.getType().name());
    };

    public static ContextEval<Player, Material, Object, Object> PLAYER_ITEM = (
            region, flag,
            player, material,
            a, b) -> {

        if (hasBypassPermission(player)) {
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

    public static ContextEval<Player, Block, Object, Object> PLAYER_BLOCK = (
            region, flag,
            player, block,
            a, b) -> {

        if (hasBypassPermission(player)) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, block.getType().name());
    };

    public static ContextEval<Player, Entity, Object, Object> PLAYER_ENTITY = (
            region, flag,
            player, entity,
            a, b) -> {

        if (hasBypassPermission(player)) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, entity.getType().name());
    };

    public static ContextEval<Entity, Player, Object, Object> ENTITY_PLAYER = (
            region, flag,
            entity, player,
            a, b) -> {

        return region.getFlagState(flag, entity.getType().name());
    };

    public static ContextEval<Player, Entity, Object, Object> PLAYER_PLAYER = (
            region, flag,
            player, entity,
            a, b) -> {

        if (hasBypassPermission(player)) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, entity.getType().name());
    };

    public static ContextEval<Player, Material, Object, Object> PLAYER_BUCKET_BLOCK = (
            region, flag,
            player, material,
            a, b) -> {

        if (hasBypassPermission(player)) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, material.name());
    };

    public static ContextEval<Player, Entity, Object, Object> PLAYER_BUCKET_ENTITY  = (
            region, flag,
            player, entity,
            a, b) -> {

        if (hasBypassPermission(player)) {
            return RegionEnums.FlagState.ALLOW;
        }

        return region.getFlagState(flag, entity.getType().name());
    };

}
