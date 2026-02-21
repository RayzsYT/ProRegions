package de.rayzs.proregions.plugin.impl.response;

import de.rayzs.proregions.api.response.Response;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

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
        chatMessage = "";
        actionbarMessage = "";
        title = "";
        subtitle = "";
        sound = null;
        soundVolume = 1.0;
        soundPitch = 1.0;
    }

    public ResponseImpl(
            final String chatMessage,
            final String actionbarMessage,
            final String title,
            final String subtitle,
            final Sound sound,
            final double soundVolume,
            final double soundPitch
    ) {
        this.chatMessage = chatMessage;
        this.actionbarMessage = actionbarMessage;
        this.title = title;
        this.subtitle = subtitle;
        this.sound = sound;
        this.soundVolume = soundVolume;
        this.soundPitch = soundPitch;
    }

    @Override
    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public void setActionbarMessage(String actionbarMessage) {
        this.actionbarMessage = actionbarMessage;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public void setSound(Sound sound, double volume, double pitch) {
        this.sound = sound;
        this.soundVolume = volume;
        this.soundPitch = pitch;
    }

    @Override
    public void send(Player player) {
        if (chatMessage != null && !chatMessage.isEmpty()) {
            player.sendMessage(chatMessage);
        }

        if (actionbarMessage != null && !actionbarMessage.isEmpty()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(actionbarMessage));
        }

        if (title != null && !title.isEmpty() || subtitle != null && !subtitle.isEmpty()) {
            player.sendTitle(
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

        map.put("chat", chatMessage);
        map.put("actionbar", actionbarMessage);
        map.put("title", title);
        map.put("subtitle", subtitle);
        map.put("sound.name", sound != null ? sound.name() : null);
        map.put("sound.volume", sound != null ? soundVolume : null);
        map.put("sound.pitch", sound != null ? soundPitch : null);

        return map;
    }

    public static ResponseImpl deserialize(Map<String, Object> map) {
        final Object chatMessage = map.get("chat");
        final Object actionbarMessage = map.get("actionbar");
        final Object title = map.get("title");
        final Object subtitle = map.get("subtitle");
        final Object sound = map.get("sound.name");
        final Object soundVolume = map.get("sound.volume");
        final Object soundPitch = map.get("sound.pitch");

        return new ResponseImpl(
                chatMessage != null      ? (String) chatMessage           : null,
                actionbarMessage != null ? (String) actionbarMessage      : null,
                title != null            ? (String) title                 : null,
                subtitle != null         ? (String) subtitle              : null,
                sound != null            ? Sound.valueOf((String) sound)  : null,
                soundVolume != null      ? (double) soundVolume         : 1.0,
                soundPitch != null       ? (double) soundPitch          : 1.0
        );
    }
}
