package de.rayzs.proregions.plugin.impl.response;

import de.rayzs.proregions.api.ProRegions;
import de.rayzs.proregions.api.response.Response;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResponseImpl implements Response {

    private String chatMessage;
    private String actionbarMessage;
    private String title;
    private String subtitle;

    private Sound sound;
    private double soundVolume;
    private double soundPitch;

    public ResponseImpl() {
        chatMessage = null;
        actionbarMessage = null;
        title = null;
        subtitle = null;
        sound = null;
        soundVolume = 1.0;
        soundPitch = 1.0;
    }

    @Override
    public void setChatMessage(@Nullable String chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public void setActionbarMessage(@Nullable String actionbarMessage) {
        this.actionbarMessage = actionbarMessage;
    }

    @Override
    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Override
    public void setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public void setSound(@Nullable Sound sound, double volume, double pitch) {
        this.sound = sound;
        this.soundVolume = volume;
        this.soundPitch = pitch;
    }

    @Override
    public void send(Player player) {
        if (chatMessage != null) {
            ProRegions.get().getMessageProvider().sendMessage(player, chatMessage);
        }

        if (actionbarMessage != null && !actionbarMessage.isEmpty()) {
            ProRegions.get().getMessageProvider().sendActionbar(player, actionbarMessage);
        }

        if (title != null || subtitle != null) {
            ProRegions.get().getMessageProvider().sendTitle(player,
                    Objects.requireNonNullElse(title, ""),
                    Objects.requireNonNullElse(subtitle, "")
            );
        }

        if (sound != null) {
            player.playSound(player.getLocation(), sound, (float) soundVolume, (float) soundPitch);
        }
    }

    @Override
    public Map<String, Object> serialize() {

        final Map<String, Object> map = new HashMap<>();

        if (chatMessage != null) {
            map.put("chat", chatMessage);
        }

        if (actionbarMessage != null) {
            map.put("actionbar", actionbarMessage);
        }

        if (title != null) {
            map.put("title", title);
        }

        if (subtitle != null) {
            map.put("subtitle", subtitle);
        }

        if (sound != null) {
            map.put("sound-name", sound.name());
            map.put("sound-volume", soundVolume);
            map.put("sound-pitch", soundPitch);
        }

        return map;
    }

    public static ResponseImpl deserialize(Map<String, Object> map) {
        final Object chatMessage = map.get("chat");
        final Object actionbarMessage = map.get("actionbar");
        final Object title = map.get("title");
        final Object subtitle = map.get("subtitle");
        final Object sound = map.get("sound-name");
        final Object soundVolume = map.get("sound-volume");
        final Object soundPitch = map.get("sound-pitch");

        final ResponseImpl response = new ResponseImpl();

        if (chatMessage != null) {
            response.setChatMessage(chatMessage.toString());
        }

        if (actionbarMessage != null) {
            response.setActionbarMessage(actionbarMessage.toString());
        }

        if (title != null) {
            response.setTitle(title.toString());
        }

        if (subtitle != null) {
            response.setSubtitle(subtitle.toString());
        }

        if (sound != null) {
            response.setSound(
                    Sound.valueOf((String) sound),
                    (double) soundVolume,
                    (double) soundPitch
            );
        }

        return response;
    }
}
