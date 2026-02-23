package de.rayzs.proregions.api.response;

import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public interface Response extends ConfigurationSerializable {

    void send(final Player player);

    void setChatMessage(final @Nullable String text);

    void setActionbarMessage(final @Nullable String text);

    void setTitle(final @Nullable String text);

    void setSubtitle(final @Nullable String text);

    void setSound(
            final @Nullable Sound sound,
            final double volume,
            final double pitch
    );
}
