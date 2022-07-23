package com.pmkcanadian.perseids.config;

import com.pmkcanadian.perseids.utilities.WeightedRandomBag;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class PerseidsConfig {
    public boolean newMoonMeteorShower;

    public int beginSpawningStarsTime;

    public int endSpawningStarsTime;

    public boolean shootingStarsEnabled;

    public double shootingStarsPerMinute;

    public double shootingStarsPerMinuteMeteorShower;

    public int shootingStarsMinHeight;

    public int shootingStarsMaxHeight;

    public boolean fallingStarsEnabled;

    public double fallingStarsPerMinute;

    public double fallingStarsPerMinuteMeteorShower;

    public int fallingStarsRadius;

    public boolean fallingStarsSoundEnabled;

    public double fallingStarsVolume;

    public int fallingStarsSparkTime;

    public int fallingStarsExperience;

    public WeightedRandomBag<String> fallingStarSimpleLoot;

    public String fallingStarLootTable;

    public PerseidsConfig(ConfigurationSection section) {
        buildFromConfigurationSection(section);
    }

    public PerseidsConfig(ConfigurationSection section, PerseidsConfig globalConfig) {
        buildFromConfigurationSectionWithGlobal(section, globalConfig);
    }

    private void buildFromConfigurationSection(ConfigurationSection section) {
        this.newMoonMeteorShower = section.getBoolean("new-moon-meteor-shower");
        this.beginSpawningStarsTime = section.getInt("begin-spawning-stars-time");
        this.endSpawningStarsTime = section.getInt("end-spawning-stars-time");
        this.shootingStarsEnabled = section.getBoolean("shooting-stars-enabled");
        this.shootingStarsPerMinute = section.getDouble("shooting-stars-per-minute");
        this.shootingStarsPerMinuteMeteorShower = section.getDouble("shooting-stars-per-minute-during-meteor-showers");
        this.shootingStarsMinHeight = section.getInt("shooting-stars-min-height");
        this.shootingStarsMaxHeight = section.getInt("shooting-stars-max-height");
        this.fallingStarsEnabled = section.getBoolean("falling-stars-enabled");
        this.fallingStarsPerMinute = section.getDouble("falling-stars-per-minute");
        this.fallingStarsPerMinuteMeteorShower = section.getDouble("falling-stars-per-minute-during-meteor-showers");
        this.fallingStarsRadius = section.getInt("falling-stars-radius");
        this.fallingStarsSoundEnabled = section.getBoolean("falling-stars-sound-enabled");
        this.fallingStarsVolume = section.getDouble("falling-stars-volume");
        this.fallingStarsSparkTime = section.getInt("falling-stars-spark-time");
        this.fallingStarsExperience = section.getInt("falling-stars-experience");
        this.fallingStarLootTable = section.getString("falling-stars-loot-table");
        if (section.isSet("falling-stars-loot"))
            this.fallingStarSimpleLoot = calculateSimpleLoot(section.getConfigurationSection("falling-stars-loot"));
    }

    private void buildFromConfigurationSectionWithGlobal(ConfigurationSection section, PerseidsConfig globalConfig) {
        this.newMoonMeteorShower = section.getBoolean("new-moon-meteor-shower", globalConfig.newMoonMeteorShower);
        this.beginSpawningStarsTime = section.getInt("begin-spawning-stars-time", globalConfig.beginSpawningStarsTime);
        this.endSpawningStarsTime = section.getInt("end-spawning-stars-time", globalConfig.endSpawningStarsTime);
        this.shootingStarsEnabled = section.getBoolean("shooting-stars-enabled", globalConfig.shootingStarsEnabled);
        this.shootingStarsPerMinute = section.getDouble("shooting-stars-per-minute", globalConfig.shootingStarsPerMinute);
        this.shootingStarsPerMinuteMeteorShower = section.getDouble("shooting-stars-per-minute-during-meteor-showers", globalConfig.shootingStarsPerMinuteMeteorShower);
        this.shootingStarsMinHeight = section.getInt("shooting-stars-min-height", globalConfig.shootingStarsMinHeight);
        this.shootingStarsMaxHeight = section.getInt("shooting-stars-max-height", globalConfig.shootingStarsMaxHeight);
        this.fallingStarsEnabled = section.getBoolean("falling-stars-enabled", globalConfig.fallingStarsEnabled);
        this.fallingStarsPerMinute = section.getDouble("falling-stars-per-minute", globalConfig.fallingStarsPerMinute);
        this.fallingStarsPerMinuteMeteorShower = section.getDouble("falling-stars-per-minute-during-meteor-showers", globalConfig.fallingStarsPerMinuteMeteorShower);
        this.fallingStarsRadius = section.getInt("falling-stars-radius", globalConfig.fallingStarsRadius);
        this.fallingStarsSoundEnabled = section.getBoolean("falling-stars-sound-enabled", globalConfig.fallingStarsSoundEnabled);
        this.fallingStarsVolume = section.getDouble("falling-stars-volume", globalConfig.fallingStarsVolume);
        this.fallingStarsSparkTime = section.getInt("falling-stars-spark-time", globalConfig.fallingStarsSparkTime);
        this.fallingStarsExperience = section.getInt("falling-stars-experience", globalConfig.fallingStarsExperience);
        if (section.isSet("falling-stars-loot") || section.isSet("falling-stars-loot-table")) {
            this.fallingStarLootTable = section.getString("falling-stars-loot-table");
            if (section.isSet("falling-stars-loot"))
                this.fallingStarSimpleLoot = calculateSimpleLoot(section.getConfigurationSection("falling-stars-loot"));
        } else {
            this.fallingStarLootTable = globalConfig.fallingStarLootTable;
            this.fallingStarSimpleLoot = globalConfig.fallingStarSimpleLoot;
        }
    }

    public WeightedRandomBag<String> calculateSimpleLoot(ConfigurationSection loot) {
        WeightedRandomBag<String> fallingStarDrops = new WeightedRandomBag();
        for (String key : loot.getKeys(false)) {
            try {
                Material.valueOf(key.toUpperCase());
                fallingStarDrops.addEntry(key.toUpperCase(), loot.getDouble(key));
            } catch (IllegalArgumentException e) {
                System.err.println("Error: Item with name " + key.toUpperCase() + " does not exist, skipping");
            }
        }
        return fallingStarDrops;
    }
}
