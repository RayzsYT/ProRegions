package de.rayzs.proregions.api.message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface MessageProvider {

    void reload();

    /**
     * Reads, saves, and returns the message from the given path.
     * If no message is found, the default message will be saved and returned.
     *
     * @param path the path where the message is located.
     * @param defaultMessage the default message to save and return if no message is found at the given path.
     * @return the message.
     */
    String get(String path, final String defaultMessage);

    /**
     * Sends a title and subtitle to the given player.
     *
     * @param player the player.
     * @param title the title.
     * @param subtitle the subtitle.
     */
    void sendTitle(
            final Player player,
            final String title,
            final String subtitle
    );

    /**
     * Sends an actionbar message to the given player.
     *
     * @param player the player.
     * @param message the message.
     */
    void sendActionbar(
            final Player player,
            final String message
    );

    /**
     * Sends a chat message to the given sender.
     *
     * @param sender the sender.
     * @param message the message.
     */
    void sendMessage(
            final CommandSender sender,
            final String message
    );

    /**
     * Sends a chat message to the given sender,
     * replacing the placeholders with the given replacements.
     *
     * @param sender the sender.
     * @param message the message.
     * @param replacements the placeholders and their replacements.
     *
     * <pre>
     *  {@code
     *      // Example usage:
     *      sendMessage(sender,
     *          "Player=%player%, World=%world%",
     *          "%player%", player.getName(),
     *          "%world%", player.getWorld().getName()
     *      );
     *  }
     * </pre>
     */
    void sendMessage(
            final CommandSender sender,
            String message,
            final String... replacements
    );
}
