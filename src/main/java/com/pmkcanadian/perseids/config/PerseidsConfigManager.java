package com.pmkcanadian.perseids.config;

import com.pmkcanadian.perseids.Perseids;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class PerseidsConfigManager {
    private final Perseids perseids;

    private final Map<String, PerseidsConfig> worldConfigs = new HashMap<>();

    private PerseidsConfig globalConfig;

    public PerseidsConfigManager(Perseids perseids) {
        this.perseids = perseids;
    }

    public void processConfigs() {
        this.worldConfigs.clear();
        FileConfiguration config = this.perseids.getConfig();
        this.globalConfig = new PerseidsConfig((ConfigurationSection)config);
        ConfigurationSection worlds = config.getConfigurationSection("world-overrides");
        if (worlds != null)
            for (String world : worlds.getKeys(false)) {
                ConfigurationSection worldSettings = worlds.getConfigurationSection(world);
                if (worldSettings == null) {
                    this.perseids.getLogger().severe("Your world override config for world '" + world + "' is malformed, please review example configs at https://github.com/pmkcanadian/Perseids");
                    continue;
                }
                PerseidsConfig worldConfig = new PerseidsConfig(worldSettings, this.globalConfig);
                this.worldConfigs.put(world, worldConfig);
    }
}

    public PerseidsConfig getConfigForWorld(String worldName) {
        PerseidsConfig config = this.worldConfigs.get(worldName);
        if (config == null)
            config = this.globalConfig;
        return config;
    }

    public boolean doesWorldHaveOverrides(String worldName) {
        return this.worldConfigs.containsKey(worldName);
    }
}

