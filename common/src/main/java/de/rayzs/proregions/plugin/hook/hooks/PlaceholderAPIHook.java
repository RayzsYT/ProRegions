package de.rayzs.proregions.plugin.hook.hooks;

import de.rayzs.proregions.plugin.hook.Hook;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion implements Hook {

    @Override
    public void start() {
        register();
    }

    public String replacePlaceholders(final Player player, @NotNull String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "proregions";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Rayzs_YT";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.3";
    }
}
