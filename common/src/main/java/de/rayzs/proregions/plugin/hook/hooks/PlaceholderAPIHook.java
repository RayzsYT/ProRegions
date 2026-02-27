package de.rayzs.proregions.plugin.hook.hooks;

import de.rayzs.proregions.api.ProRegions;
import de.rayzs.proregions.api.region.Region;
import de.rayzs.proregions.plugin.hook.Hook;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class PlaceholderAPIHook implements Hook {

    @Override
    public void start() {
        new Expansion().register();
    }

    public String replacePlaceholders(final Player player, @NotNull String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    private static class Expansion extends PlaceholderExpansion {
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
            return "1.0.6";
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {

            System.out.println("Got: " + params);
            final String[] split = params.split("_");

            if (params.equalsIgnoreCase("regions")) {
                final Collection<String> regions = ProRegions.get().getRegionProvider().getRegions().keySet();
                return String.join(", ", regions);
            }

            if (player != null && params.equalsIgnoreCase("player_regions")) {
                final Set<Region> regions = ProRegions.get().getRegionProvider().getCachedPlayerRegions(player);
                return String.join(", ", regions.stream().map(Region::getRegionName).toList());
            }

            if (player != null && params.startsWith("player_inside_")) {
                if (split.length != 5) {
                    return "Invalid placeholder format! Use %proregions_player_inside_<region>_<true>_<false>%";
                }

                final String region = split[2];
                final String trueVal = split[3];
                final String falseVal = split[4];

                final Collection<String> regions = ProRegions.get().getRegionProvider()
                        .getCachedPlayerRegions(player).stream().map(Region::getRegionName)
                        .toList();

                return regions.contains(region) ? trueVal : falseVal;
            }

            return super.onPlaceholderRequest(player, params);
        }
    }
}
