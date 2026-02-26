package de.rayzs.proregions.plugin.listeners;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import de.rayzs.proregions.api.region.RegionProvider;
import de.rayzs.proregions.api.region.context.Contexts;
import de.rayzs.proregions.api.utils.Permissions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

import java.util.*;

public class RegionListener implements Listener {

    private final ProRegionsAPI api;
    private final RegionProvider provider;

    public RegionListener(final ProRegionsAPI api) {
        this.api = api;
        this.provider = api.getRegionProvider();
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        api.getRegionProvider().updatePlayerRegionsCache(
                player,
                player.getLocation(),
                regions -> true
        );
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        api.getRegionProvider().resetPlayerRegionsCache(player);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        final Location from = event.getFrom();
        final Location to = event.getTo();

        // Ignore, since player did not move a block further.
        if (from.getBlockX() == to.getBlockX()
                && from.getBlockZ() == to.getBlockZ()
                && from.getBlockY() == to.getBlockY()) {
            return;
        }

        final boolean bypassPermission = Permissions.BYPASS_PERMISSION.hasPermission(player);


        // Checking if player can actually enter or leave a region.
        // YES I KNOW that this entire implementation might not be the best way,
        // but I'm honestly not sure what would be the best approach atm. :c
        final Set<Region> currentCached = api.getRegionProvider().getCachedPlayerRegions(player);

        if (!bypassPermission) {
            // Player is registered inside a region but is attempting to leave it.
            for (final Region region : currentCached) {
                if (!region.contains(to) && !provider.isAllowed(
                        region,
                        Contexts.PLAYER,
                        from,
                        RegionEnums.Flags.LEAVE,
                        player,
                        null, null, null
                )) {
                    player.teleport(from);
                    return;
                }
            }
        }

        api.getRegionProvider().updatePlayerRegionsCache(player, to, currentRegions -> {
            if (bypassPermission) {
                return true;
            }

            for (final Region region : currentRegions) {
                // Player trying to enter a new region.
                if (!currentCached.contains(region) && !provider.isAllowed(
                        region,
                        Contexts.PLAYER,
                        to,
                        RegionEnums.Flags.ENTER,
                        player,
                        null, null, null
                )) {
                    player.teleport(from);
                    return false;
                }
            }

            return true;
        });
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onHunger(final FoodLevelChangeEvent event) {
        if (! (event.getEntity() instanceof Player player)) {
            return;
        }

        if (!provider.isAllowed(
                Contexts.PLAYER,
                player.getLocation(),
                RegionEnums.Flags.PLACE,
                player,
                null, null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        if (Permissions.BYPASS_PERMISSION.hasPermission(player)) {
            return;
        }


        final Block block = event.getBlock();

        if (!provider.isAllowed(
                Contexts.PLAYER_MATERIAL,
                block.getLocation(),
                RegionEnums.Flags.PLACE,
                player,
                block.getType(),
                null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();

        if (Permissions.BYPASS_PERMISSION.hasPermission(player)) {
            return;
        }


        final Block block = event.getBlock();

        if (!provider.isAllowed(
                Contexts.PLAYER_MATERIAL,
                block.getLocation(),
                RegionEnums.Flags.BREAK,
                player,
                block.getType(),
                null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onBlockFlow(final BlockFromToEvent event) {
        final Block block = event.getBlock();
        final Block toBlock = event.getToBlock();

        if (!event.getBlock().isLiquid() && !event.getToBlock().isLiquid()) {
            return;
        }

        if (!provider.isAllowed(
                Contexts.BLOCK,
                block.getLocation(),
                RegionEnums.Flags.FLOW,
                block,
                null, null, null
        )) {
            event.setCancelled(true);
            return;
        }

        if (!provider.isAllowed(
                Contexts.BLOCK,
                toBlock.getLocation(),
                RegionEnums.Flags.FLOW,
                toBlock,
                null, null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onFireSpread(final BlockSpreadEvent event) {
        final Block fireBlock = event.getSource();

        if (fireBlock.getType() != org.bukkit.Material.FIRE) {
            return;
        }


        final Block blockBelow = fireBlock.getRelative(BlockFace.DOWN);

        if (!provider.isAllowed(
                Contexts.BLOCK,
                fireBlock.getLocation(),
                RegionEnums.Flags.FIRE_SPREAD,
                blockBelow,
                null, null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onExplosion(final EntityExplodeEvent event) {
        if (!provider.isAllowed(
                Contexts.BLOCK,
                event.getLocation(),
                RegionEnums.Flags.EXPLODE_BLOCKS,
                event.getLocation().getBlock(),
                null, null, null
        )) {
            event.setCancelled(true);
            return;
        }

        event.blockList().removeIf(block -> !provider.isAllowed(
                Contexts.BLOCK,
                block.getLocation(),
                RegionEnums.Flags.EXPLODE_BLOCKS,
                block,
                null, null, null
        ));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();

        if (block != null) {
            if (block.getType().isInteractable()) {
                if (!provider.isAllowed(
                        Contexts.PLAYER_MATERIAL,
                        block.getLocation(),
                        RegionEnums.Flags.INTERACT_BLOCK,
                        event.getPlayer(),
                        block.getType(),
                        null, null
                )) {
                    event.setCancelled(true);
                }

                return;
            }

            if (event.getAction() == Action.PHYSICAL) {
                if (!provider.isAllowed(
                        Contexts.PLAYER_MATERIAL,
                        block.getLocation(),
                        RegionEnums.Flags.TRAMPLE_CROPS,
                        event.getPlayer(),
                        block.getType(),
                        null, null
                )) {
                    event.setCancelled(true);
                }

                return;
            }
        }

        if (event.getItem() != null) {
            if (!provider.isAllowed(
                    Contexts.PLAYER_MATERIAL,
                    player.getLocation(),
                    RegionEnums.Flags.INTERACT_ITEM,
                    event.getPlayer(),
                    event.getItem().getType(),
                    null, null
            )) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event) {
        final Material item = event.getBucket();

        if (item != Material.LAVA_BUCKET && item != Material.WATER_BUCKET) {
            return;
        }

    
        final Material material = item == Material.LAVA_BUCKET ? Material.LAVA : Material.WATER;

        if (!provider.isAllowed(
                Contexts.PLAYER_MATERIAL,
                event.getBlock().getLocation(),
                RegionEnums.Flags.BUCKET_EMPTY,
                event.getPlayer(),
                material,
                null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onPlayerBucketFill(final PlayerBucketFillEvent event) {

        if (!provider.isAllowed(
                Contexts.PLAYER_MATERIAL,
                event.getBlockClicked().getLocation(),
                RegionEnums.Flags.BUCKET_FILL,
                event.getPlayer(),
                event.getBlockClicked().getType(),
                null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractEntity(final PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();

        if (Permissions.BYPASS_PERMISSION.hasPermission(player)) {
            return;
        }


        final Entity entity = event.getRightClicked();
        final Material itemInHand = player.getInventory().getItemInMainHand().getType();

        if (itemInHand == Material.BUCKET && (entity.getType() == EntityType.GOAT || entity.getType() == EntityType.COW)) {
            if (!provider.isAllowed(
                    Contexts.PLAYER_ENTITY,
                    entity.getLocation(),
                    RegionEnums.Flags.MILK_ENTITY,
                    player, entity,
                    null, null
            )) {
                event.setCancelled(true);
            }

            return;
        }

        if (!provider.isAllowed(
                Contexts.PLAYER_ENTITY,
                entity.getLocation(),
                RegionEnums.Flags.INTERACT_ENTITY,
                event.getPlayer(),
                entity,
                null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager) {
            final Player player = event.getEntity() instanceof Player p ? p : null;

            if (player != null) {
                if (!provider.isAllowed(
                        Contexts.PLAYER_PLAYER,
                        event.getEntity().getLocation(),
                        RegionEnums.Flags.PVP,
                        damager, player,
                        null, null
                )) {
                    event.setCancelled(true);
                }
            } else {
                if (!provider.isAllowed(
                        Contexts.PLAYER_ENTITY,
                        event.getEntity().getLocation(),
                        RegionEnums.Flags.PVE,
                        damager, event.getEntity(),
                        null, null
                )) {
                    event.setCancelled(true);
                }
            }

            return;
        }

        if (event.getEntity() instanceof Player victim) {
            if (!provider.isAllowed(
                    Contexts.PLAYER_ENTITY,
                    event.getEntity().getLocation(),
                    RegionEnums.Flags.PVE,
                    victim, event.getDamager(),
                    null, null
            )) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamage(final EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        final Location location = entity.getLocation();

        switch (event.getCause()) {
            case LAVA: case FIRE: case FIRE_TICK: case CAMPFIRE:
                if (!provider.isAllowed(
                        Contexts.ENTITY,
                        location,
                        RegionEnums.Flags.BURN_DAMAGE,
                        entity,
                        null, null, null
                )) {
                    event.setCancelled(true);
                }

                return;

            case FALL:
                if (!provider.isAllowed(
                        Contexts.ENTITY,
                        location,
                        RegionEnums.Flags.FALL_DAMAGE,
                        entity,
                        null, null, null
                )) {
                    event.setCancelled(true);
                }

                return;

                case FALLING_BLOCK:
                    if (!provider.isAllowed(
                            Contexts.ENTITY,
                            location,
                            RegionEnums.Flags.FALLING_BLOCK_DAMAGE,
                            entity,
                            null, null, null
                    )) {
                        event.setCancelled(true);
                    }

                return;

            case DROWNING:
                if (!provider.isAllowed(
                        Contexts.ENTITY,
                        location,
                        RegionEnums.Flags.DROWNING_DAMAGE,
                        entity,
                        null, null, null
                )) {
                    event.setCancelled(true);
                }

                return;

            default: break;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onProjectileHit(final ProjectileHitEvent event) {
        final Projectile projectile = event.getEntity();

        if (projectile.getShooter() instanceof Player player) {

            if (projectile.getType() == EntityType.FISHING_BOBBER) {
                if (!provider.isAllowed(
                        Contexts.PLAYER,
                        projectile.getLocation(),
                        RegionEnums.Flags.FISHING,
                        player,
                        null, null, null
                )) {
                    projectile.remove();
                    event.setCancelled(true);
                }
            } else {
                if (!provider.isAllowed(
                        Contexts.PLAYER_PROJECTILE,
                        projectile.getLocation(),
                        RegionEnums.Flags.PROJECTILE,
                        player, projectile,
                        null, null
                )) {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }

            return;
        }

        if (!provider.isAllowed(
                Contexts.PROJECTILE,
                projectile.getLocation(),
                RegionEnums.Flags.PROJECTILE,
                projectile.getShooter(), projectile,
                null, null
        )) {
            event.setCancelled(true);
            event.getEntity().remove();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        final Projectile projectile = event.getEntity();

        if (projectile.getShooter() instanceof Player player) {
            if (projectile.getType() == EntityType.FISHING_BOBBER) {
                if (!provider.isAllowed(
                        Contexts.PLAYER,
                        projectile.getLocation(),
                        RegionEnums.Flags.FISHING,
                        player,
                        null, null, null
                )) {
                    projectile.remove();
                    event.setCancelled(true);
                }
            } else {
                if (!provider.isAllowed(
                        Contexts.PLAYER_PROJECTILE,
                        projectile.getLocation(),
                        RegionEnums.Flags.PROJECTILE,
                        player, projectile,
                        null, null
                )) {
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }

            return;
        }

        if (!provider.isAllowed(
                Contexts.PROJECTILE,
                projectile.getLocation(),
                RegionEnums.Flags.PROJECTILE,
                projectile.getShooter(), projectile,
                null, null
        )) {
            event.setCancelled(true);
            event.getEntity().remove();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerFish(final PlayerFishEvent event) {
        final Player player = event.getPlayer();

        if (Permissions.BYPASS_PERMISSION.hasPermission(player)) {
            return;
        }


        final FishHook hook = event.getHook();

        if (!provider.isAllowed(
                Contexts.PLAYER,
                hook.getLocation(),
                RegionEnums.Flags.FISHING,
                player,
                null, null, null
        )) {
            hook.remove();
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickup(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();

        if (Permissions.BYPASS_PERMISSION.hasPermission(player)) {
            return;
        }


        final Material material = event.getItem().getItemStack().getType();

        if (!provider.isAllowed(
                Contexts.PLAYER_ITEM,
                player.getLocation(),
                RegionEnums.Flags.PICKUP,
                player, material,
                null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickup(final PlayerPickupArrowEvent event) {
        final Player player = event.getPlayer();

        if (Permissions.BYPASS_PERMISSION.hasPermission(player)) {
            return;
        }


        final Material material = event.getItem().getItemStack().getType();

        if (!provider.isAllowed(
                Contexts.PLAYER_ITEM,
                player.getLocation(),
                RegionEnums.Flags.PICKUP,
                player, material,
                null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDrop(final EntityDropItemEvent event) {
        final Entity entity = event.getEntity();
        final Material material = event.getItemDrop().getItemStack().getType();

        if (!provider.isAllowed(
                Contexts.ENTITY_ITEM,
                entity.getLocation(),
                RegionEnums.Flags.DROP,
                entity, material,
                null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDrop(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();

        if (Permissions.BYPASS_PERMISSION.hasPermission(player)) {
            return;
        }


        final Material material = event.getItemDrop().getItemStack().getType();

        if (!provider.isAllowed(
                Contexts.PLAYER_ITEM,
                player.getLocation(),
                RegionEnums.Flags.DROP,
                player, material,
                null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityPickup(final EntityPickupItemEvent event) {
        final Entity entity = event.getEntity();
        final Material material = event.getItem().getItemStack().getType();

        if (!provider.isAllowed(
                Contexts.ENTITY_ITEM,
                entity.getLocation(),
                RegionEnums.Flags.PICKUP,
                entity, material,
                null, null
        )) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPistonExtend(final BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {

            if (!provider.isAllowed(
                    Contexts.BLOCK,
                    block.getLocation(),
                    RegionEnums.Flags.PISTON,
                    block,
                    null, null, null
            )) {
                event.setCancelled(true);
                return;
            }

            final Block nextBlock = block.getRelative(event.getDirection());

            if (!provider.isAllowed(
                    Contexts.BLOCK,
                    nextBlock.getLocation(),
                    RegionEnums.Flags.PISTON,
                    nextBlock,
                    null, null, null
            )) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }

        final Entity entity = event.getEntity();
        final Location location = entity.getLocation();

        if (!provider.isAllowed(
                Contexts.ENTITY,
                location,
                event.getEntity() instanceof Monster
                        ? RegionEnums.Flags.MONSTER_SPAWN
                        : RegionEnums.Flags.ANIMAL_SPAWN,
                entity,
                null, null, null
        )) {
            event.setCancelled(true);
        }
    }
}
