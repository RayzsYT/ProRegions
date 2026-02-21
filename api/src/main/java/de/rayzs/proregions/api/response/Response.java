package de.rayzs.proregions.api.response;

import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public interface Response extends ConfigurationSerializable {

    void setChatMessage(final String text);

    void setActionbarMessage(final String text);

    void setTitle(final String text);

    void setSubtitle(final String text);

    void setSound(final Sound sound, final double volume, final double pitch);

    void send(final Player player);
}
