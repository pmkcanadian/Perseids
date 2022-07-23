package com.pmkcanadian.perseids;

import com.pmkcanadian.perseids.commands.CommandPerseids;
import com.pmkcanadian.perseids.commands.CommandFallingStar;
import com.pmkcanadian.perseids.commands.CommandShootingStar;
import com.pmkcanadian.perseids.config.PerseidsConfigManager;
import com.pmkcanadian.perseids.utilities.Metrics;
import com.pmkcanadian.perseids.utilities.UpdateChecker;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Perseids extends JavaPlugin {

    public PerseidsConfigManager configManager = new PerseidsConfigManager(this);

    public void onEnable() {
        saveDefaultConfig();
        Metrics metrics = new Metrics(this, 8292);
        getCommand("perseids").setExecutor((CommandExecutor)new CommandPerseids(this));
        getCommand("shootingstar").setExecutor((CommandExecutor)new CommandShootingStar(this));
        getCommand("fallingstar").setExecutor((CommandExecutor)new CommandFallingStar(this));
        this.configManager.processConfigs();
        BukkitRunnable stargazingTask = new Astronomer(this);
        stargazingTask.runTaskTimer((Plugin)this, 0L, 10L);
        checkForUpdates();
    }

    public void reload() {
        reloadConfig();
        this.configManager.processConfigs();
        checkForUpdates();
    }

    public void checkForUpdates() {
        if (getConfig().getBoolean("check-for-updates"))
            (new UpdateChecker((Plugin)this, 81862)).getVersion(version -> {
                try {
                    double current = Double.parseDouble(getDescription().getVersion());
                    double api = Double.parseDouble(version);
                    if (current < api)
                        getLogger().info("There is an update available for Perseids (" + current + " -> " + api + ")");
                } catch (NumberFormatException e) {
                    if (getConfig().getBoolean("debug"))
                        getLogger().severe("Unable to process remote plugin version number '" + version + "'");
                }
            });
    }
}
