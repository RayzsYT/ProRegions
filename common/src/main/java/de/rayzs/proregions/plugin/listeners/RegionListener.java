package de.rayzs.proregions.plugin.listeners;

import de.rayzs.proregions.api.ProRegionAPI;
import de.rayzs.proregions.api.region.RegionEnums;
import de.rayzs.proregions.api.region.RegionProvider;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class RegionListener implements Listener {

    private final ProRegionAPI api;
    private final RegionProvider provider;

    public RegionListener(final ProRegionAPI api) {
        this.api = api;
        this.provider = api.getRegionProvider();
    }

    @EventHandler(
            priority = org.bukkit.event.EventPriority.LOWEST
    )
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (!provider.isAllowed(event.getPlayer(), event.getBlock(), RegionEnums.Flags.PLACE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(
            priority = org.bukkit.event.EventPriority.LOWEST
    )
    public void onBlockBreak(final BlockBreakEvent event) {
        if (!provider.isAllowed(event.getPlayer(), event.getBlock(), RegionEnums.Flags.BREAK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(
            priority = org.bukkit.event.EventPriority.LOWEST
    )
    public void onBlockFlow(final BlockFromToEvent event) {

        if (!event.getBlock().isLiquid() && !event.getToBlock().isLiquid()) {
            return;
        }

        if (!provider.isAllowed(event.getBlock(), RegionEnums.Flags.FLOW)) {
            event.setCancelled(true);
            return;
        }

        if (!provider.isAllowed(event.getToBlock(), RegionEnums.Flags.FLOW)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(
            priority = org.bukkit.event.EventPriority.LOWEST
    )
    public void onFireSpread(final BlockFromToEvent event) {
        if (event.getBlock().getType() != org.bukkit.Material.FIRE) {
            return;
        }

        if (!event.getToBlock().getType().isFlammable()) {
            return;
        }

        if (!provider.isAllowed(event.getBlock(), RegionEnums.Flags.FIRE_SPREAD)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(
            priority = org.bukkit.event.EventPriority.LOWEST
    )
    public void onExplosion(final EntityExplodeEvent event) {
        if (!provider.isAllowed(event.getLocation().getBlock(), RegionEnums.Flags.EXPLODE_BLOCKS)) {
            event.setCancelled(true);
            return;
        }

        event.blockList().removeIf(block -> !provider.isAllowed(event.getEntity(), block, RegionEnums.Flags.EXPLODE_BLOCKS));
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }

        final Block block = event.getClickedBlock();

        if (block.getType().isInteractable()) {
            if (!provider.isAllowed(event.getPlayer(), event.getClickedBlock(), RegionEnums.Flags.INTERACT_BLOCK)) {
                event.setCancelled(true);
            }
        } else if (event.getAction() == Action.PHYSICAL) {
            if (!provider.isAllowed(event.getPlayer(), event.getClickedBlock(), RegionEnums.Flags.TRAMPLE_CROPS)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event) {
        final Material item = event.getBlock().getType();
        if (item != Material.LAVA_BUCKET && item != Material.WATER_BUCKET) {
            return;
        }

        final Material material = item == Material.LAVA_BUCKET ? Material.LAVA : Material.WATER;
        if (!provider.isAllowed(
                event.getPlayer(),
                event.getBlock(),
                material,
                RegionEnums.Flags.PLACE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onPlayerBucketFill(final PlayerBucketFillEvent event) {
        if (!provider.isAllowed(event.getPlayer(), event.getBlockClicked(), RegionEnums.Flags.BREAK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onEntityInteract(final PlayerInteractEntityEvent event) {
        if (!provider.isAllowed(event.getPlayer(), event.getRightClicked(), RegionEnums.Flags.INTERACT_ENTITY)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player damager) {
            final Player player = event.getEntity() instanceof Player p ? p : null;

            if (!provider.isAllowed(
                    damager,
                    player == null ? event.getEntity() : player,
                    player == null ? RegionEnums.Flags.PVE : RegionEnums.Flags.PVP
            )) {
                event.setCancelled(true);
            }

            return;
        }

        if (event.getEntity() instanceof Player victim) {
            if (!provider.isAllowed(
                    event.getDamager(),
                    victim,
                    RegionEnums.Flags.PVE
            )) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onPlayerDamage(final EntityDamageEvent event) {
        if (! (event.getEntity() instanceof Player)) {
            return;
        }

        switch (event.getCause()) {
            case LAVA: case FIRE:
                if (!provider.isAllowed(event.getEntity(), RegionEnums.Flags.BURN_DAMAGE)) {
                    event.setCancelled(true);
                }

                return;

            case FALL:
                if (!provider.isAllowed(event.getEntity(), RegionEnums.Flags.FALL_DAMAGE)) {
                    event.setCancelled(true);
                }

                return;

                case FALLING_BLOCK:
                if (!provider.isAllowed(event.getEntity(), RegionEnums.Flags.FALLING_BLOCK_DAMAGE)) {
                    event.setCancelled(true);
                }

                return;

            case DROWNING:
                if (!provider.isAllowed(event.getEntity(), RegionEnums.Flags.DROWNING_DAMAGE)) {
                    event.setCancelled(true);
                }

                return;

            default: break;
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onPistonExtend(final BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {

            if (!provider.isAllowed(block.getRelative(event.getDirection()), RegionEnums.Flags.PISTON)) {
                event.setCancelled(true);
                return;
            }

            if (!provider.isAllowed(block, RegionEnums.Flags.PISTON)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }

        if (!provider.isAllowed(
                event.getLocation().getBlock(),
                event.getEntity() instanceof Monster
                        ? RegionEnums.Flags.MONSTER_SPAWN : RegionEnums.Flags.ANIMAL_SPAWN
        )) {
            event.setCancelled(true);
        }
    }
}
