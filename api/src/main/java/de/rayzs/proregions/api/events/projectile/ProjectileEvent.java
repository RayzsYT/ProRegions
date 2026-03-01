
package de.rayzs.proregions.api.events.projectile;

import de.rayzs.proregions.api.events.ProRegionsPlayerEvent;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ProjectileEvent extends ProRegionsPlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Projectile projectile;

    public ProjectileEvent(
            final Region region,
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state,
            final Player player,
            final Projectile projectile
            ) {
        super(region, flag, state, player);

        this.projectile = projectile;
    }

    public Entity getProjectile() {
        return this.projectile;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
