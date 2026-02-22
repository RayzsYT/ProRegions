package de.rayzs.proregions.plugin.impl.clipboard;

import de.rayzs.proregions.api.clipboard.Clipboard;
import de.rayzs.proregions.api.clipboard.ClipboardProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClipboardProviderImpl implements ClipboardProvider {

    private final Map<UUID, Clipboard> cache = new HashMap<>();

    @Override
    public Clipboard getClipboard(UUID uuid) {
        return cache.computeIfAbsent(uuid, k -> new Clipboard());
    }
}
