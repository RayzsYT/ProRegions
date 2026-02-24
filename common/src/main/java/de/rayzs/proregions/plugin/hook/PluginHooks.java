package de.rayzs.proregions.plugin.hook;

import de.rayzs.proregions.api.ProRegion;
import de.rayzs.proregions.plugin.hook.hooks.PlaceholderAPIHook;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public enum PluginHooks {

    PLACEHOLDERAPI("PlaceholderAPI", PlaceholderAPIHook.class);

    private final boolean enabled;
    private Hook hookObj;

    PluginHooks(final String pluginName, final Class<? extends Hook> hookClass) {
        enabled = Bukkit.getPluginManager().isPluginEnabled(pluginName);

        if (!enabled) {
            this.hookObj = null;
            return;
        }

        try {
            this.hookObj = hookClass.newInstance();
            this.hookObj.start();

            ProRegion.get().getLogger().info("Successfully hooked into " + pluginName + "! ");
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    public <T extends Hook> void executeIfExist(final Consumer<T> consumer) {
        consumer.accept((T) hookObj);
    }

    public <T extends Hook, V> V modifyIfExist(final V input, final HookTask<T, V> consumer) {
        if (this.hookObj == null) {
            return input;
        }

        return consumer.apply((T) hookObj, input);
    }
}
