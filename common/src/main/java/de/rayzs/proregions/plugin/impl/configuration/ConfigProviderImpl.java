package de.rayzs.proregions.plugin.impl.configuration;

import de.rayzs.proregions.api.configuration.Config;
import de.rayzs.proregions.api.configuration.ConfigProvider;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigProviderImpl implements ConfigProvider {

    private final String defaultFolderPath = "plugins/ProRegions";
    private final Map<String, Config> configs = new HashMap<>();

    public ConfigProviderImpl() {
        final File folder = new File(defaultFolderPath);

        if (folder.isDirectory()) {
            return;
        }

        if (!folder.mkdirs()) {
            throw new RuntimeException("Failed to create ProRegions folder! (" + defaultFolderPath + ")");
        }
    }

    @Override
    public void reload() {
        configs.values().forEach(Config::reload);
    }

    @Override
    public Config getOrCreate(final String fileName) {
        return getOrCreate(null, fileName);
    }

    @Override
    public Config getOrCreate(final String filePath, final String fileName) {
        final String path = defaultFolderPath + (filePath != null ? ("/" + filePath) : "");
        final String id = path + "/" + fileName;

        Config config = configs.get(id);
        if (config != null) {
            return config;
        }

        config = new ConfigImpl(path, fileName);

        configs.put(id, config);
        return config;
    }
}
