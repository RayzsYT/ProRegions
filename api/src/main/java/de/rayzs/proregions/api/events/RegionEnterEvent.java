package de.rayzs.proregions.api.events;

import de.rayzs.proregions.api.region.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RegionEnterEvent extends ProRegionsEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Region region;

    public RegionEnterEvent(
            final Player player,
            final Region region,
            boolean cancelled
    ) {
        this.player = player;
        this.region = region;
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public Region getRegion() {
        return region;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
