package de.rayzs.proregions.plugin.listeners;

import de.rayzs.proregions.api.ProRegionsAPI;
import de.rayzs.proregions.api.clipboard.Clipboard;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.UUID;

public class WandListener implements Listener {

    private final ProRegionsAPI api;

    public WandListener(final ProRegionsAPI api) {
        this.api = api;
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

        final Clipboard clipboard = api.getClipboardProvider().getClipboard(uuid);
        final Location location = event.getClickedBlock().getLocation();

        if (event.getAction().name().contains("LEFT")) {
            clipboard.setFirstLocation(location);
            event.setCancelled(true);

            final String positionSetMessage = api.getMessageProvider().get(
                    "first-position-set",
                    "&aFirst position set!"
            );

            api.getMessageProvider().sendMessage(
                    player,
                    positionSetMessage
            );

        } else if (event.getAction().name().contains("RIGHT")) {
            clipboard.setSecondLocation(location);
            event.setCancelled(true);

            final String positionSetMessage = api.getMessageProvider().get(
                    "second-position-set",
                    "&aSecond position set!"
            );

            api.getMessageProvider().sendMessage(
                    player,
                    positionSetMessage
            );
        }
    }
}
