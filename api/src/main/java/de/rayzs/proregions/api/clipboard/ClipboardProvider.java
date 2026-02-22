package de.rayzs.proregions.api.clipboard;

import java.util.UUID;

public interface ClipboardProvider {
    Clipboard getClipboard(final UUID uuid);
}
