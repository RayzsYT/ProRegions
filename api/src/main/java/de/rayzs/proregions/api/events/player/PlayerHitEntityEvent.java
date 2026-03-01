
package de.rayzs.proregions.api.events.player;

import de.rayzs.proregions.api.events.ProRegionsPlayerEvent;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerHitEntityEvent extends ProRegionsPlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Entity victim;

    public PlayerHitEntityEvent(
            final Region region,
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state,
            final Player player,
            final Entity victim
    ) {
        super(region, flag, state, player);

        this.victim = victim;
    }

    public Entity getVictim() {
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
