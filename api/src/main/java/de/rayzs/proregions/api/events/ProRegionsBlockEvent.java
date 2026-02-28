package de.rayzs.proregions.api.events;

import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.block.Block;

public abstract class ProRegionsBlockEvent extends ProRegionsEvent {

    protected final Block block;

    public ProRegionsBlockEvent(
            final Region region,
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state,
            final Block block
    ) {
        super(region, flag, state);

        this.block = block;
    }
}
