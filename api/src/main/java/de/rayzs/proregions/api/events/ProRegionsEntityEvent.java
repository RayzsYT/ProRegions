package de.rayzs.proregions.api.events;

import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class ProRegionsEntityEvent extends ProRegionsEvent {

    protected final Entity entity;

    public ProRegionsEntityEvent(
            final Region region,
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state,
            final Entity entity
    ) {
        super(region, flag, state);

        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
