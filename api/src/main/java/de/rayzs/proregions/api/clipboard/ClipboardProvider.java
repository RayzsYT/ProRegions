package de.rayzs.proregions.api.clipboard;

import java.util.UUID;

public interface ClipboardProvider {

    /**
     * Gets an already existing Clipboard instance or creates a new one.
     *
     * @param uuid The UUID of the Clipboard holder.
     * @return The existing or newly created Clipboard instance.
     */
    Clipboard getClipboard(final UUID uuid);
}
