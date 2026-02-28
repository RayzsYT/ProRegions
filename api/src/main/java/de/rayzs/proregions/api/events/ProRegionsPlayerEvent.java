package de.rayzs.proregions.api.events;

import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.entity.Player;

public abstract class ProRegionsPlayerEvent extends ProRegionsEvent {

    protected final Player player;

    public ProRegionsPlayerEvent(
            final Region region,
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state,
            final Player player
    ) {
        super(region, flag, state);

        this.player = player;
    }
}
