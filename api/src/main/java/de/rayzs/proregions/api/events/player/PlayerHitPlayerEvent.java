
package de.rayzs.proregions.api.events.player;

import de.rayzs.proregions.api.events.ProRegionsPlayerEvent;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerHitPlayerEvent extends ProRegionsPlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Player victim;

    public PlayerHitPlayerEvent(
            final Region region,
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state,
            final Player player,
            final Player victim
    ) {
        super(region, flag, state, player);

        this.victim = victim;
    }

    public Player getVictim() {
        return this.victim;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
