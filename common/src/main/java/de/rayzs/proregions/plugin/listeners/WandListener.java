package de.rayzs.proregions.plugin.listeners;

import de.rayzs.proregions.api.utils.ExpireCache;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.UUID;

public class WandListener implements Listener {

    private final ExpireCache<UUID, Location> storedFirstPositions;
    private final ExpireCache<UUID, Location> storedSecondPositions;

    public WandListener(
            final ExpireCache<UUID, Location> storedFirstPositions,
            final ExpireCache<UUID, Location> storedSecondPositions
    ) {
        this.storedFirstPositions = storedFirstPositions;
        this.storedSecondPositions = storedSecondPositions;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (!player.hasPermission("proregions.use") || player.getGameMode() != GameMode.CREATIVE) {
            return;
        }

        if (event.getItem() == null || event.getItem().getType() != Material.STONE_AXE) {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }

        final Location location = event.getClickedBlock().getLocation();

        if (event.getAction().name().contains("LEFT")) {
            storedFirstPositions.put(uuid, location);
            player.sendMessage("§aFirst position!");
            event.setCancelled(true);
        } else if (event.getAction().name().contains("RIGHT")) {
            storedSecondPositions.put(uuid, location);
            player.sendMessage("§aSecond position set!");
            event.setCancelled(true);
        }
    }
}
