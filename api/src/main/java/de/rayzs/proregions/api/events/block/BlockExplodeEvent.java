package de.rayzs.proregions.api.events.block;

import de.rayzs.proregions.api.events.ProRegionsBlockEvent;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BlockExplodeEvent extends ProRegionsBlockEvent {

    private static final HandlerList handlers = new HandlerList();

    public BlockExplodeEvent(
            final Region region,
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state,
            final Block block
    ) {
        super(region, flag, state, block);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
