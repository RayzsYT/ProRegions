package de.rayzs.proregions.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ProRegionsEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected boolean cancelled = false;
    protected boolean response = true;

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

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
