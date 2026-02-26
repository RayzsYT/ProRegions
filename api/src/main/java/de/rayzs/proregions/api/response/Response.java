package de.rayzs.proregions.api.response;

import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public interface Response extends ConfigurationSerializable {

    /**
     * Sends the response to the given player.
     *
     * @param player the player to send the response to.
     */
    void send(final Player player);

    /**
     * Sets the chat message of the response.
     * This message will be sent to the player as a chat message when the response is sent.
     * @param text the chat message to set, or null to unset the chat message.
     */
    void setChatMessage(final @Nullable String text);

    /**
     * Sets the actionbar message of the response.
     * This message will be sent to the player as an actionbar message when the response is sent.
     *
     * @param text the actionbar message to set, or null to unset the actionbar message.
     */
    void setActionbarMessage(final @Nullable String text);

    /**
     * Sets the title of the response.
     * This message will be sent to the player as a title when the response is sent.
     *
     * @param text the title to set, or null to unset the title.
     */
    void setTitle(final @Nullable String text);

    /**
     * Sets the subtitle of the response.
     * This message will be sent to the player as a subtitle when the response is sent.
     *
     * @param text the subtitle to set, or null to unset the subtitle.
     */
    void setSubtitle(final @Nullable String text);

    /**
     * Sets the sound of the response.
     * This sound will be played to the player when the response is sent.
     * Set the sound to null to unset the sound.
     *
     * @param sound the sound to set, or null to unset the sound.
     * @param volume the volume of the sound. (0.0 - 10.0)
     * @param pitch the pitch of the sound. (0.0 - 2.0)
     */
    void setSound(
            final @Nullable Sound sound,
            final double volume,
            final double pitch
    );
}
