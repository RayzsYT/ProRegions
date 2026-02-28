package de.rayzs.proregions.api.events;

import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.api.region.RegionEnums;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class ProRegionsEvent extends Event implements Cancellable {

    protected boolean response = true;
    protected boolean cancelled;

    protected final Region region;
    protected final RegionEnums.Flags flag;

    public ProRegionsEvent(
            final Region region,
            final RegionEnums.Flags flag,
            final RegionEnums.FlagState state
    ) {
        this.region = region;
        this.flag = flag;
        this.cancelled = state == RegionEnums.FlagState.DENY;
    }

    public void setSendResponse(boolean response) {
        this.response = response;
    }

    public boolean canSendResponse() {
        return this.response;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean bool) {
        this.cancelled = bool;
    }

    public Region getRegion() {
        return region;
    }

    public RegionEnums.Flags getFlag() {
        return flag;
    }
}
