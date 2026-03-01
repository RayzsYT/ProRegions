package de.rayzs.proregions.api.events.player;

import de.rayzs.proregions.api.events.ProRegionsPlayerEvent;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerBreakBlockEvent extends ProRegionsPlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;

    public PlayerBreakBlockEvent(
            final Region region,
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state,
            final Player player,
            final Block block
    ) {
        super(region, flag, state, player);

        this.block = block;
    }

    public Block getBlock() {
        return this.block;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
