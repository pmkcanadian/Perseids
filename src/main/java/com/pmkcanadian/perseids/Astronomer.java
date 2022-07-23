package com.pmkcanadian.perseids;

import com.pmkcanadian.perseids.config.PerseidsConfig;
import java.util.List;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class Astronomer extends BukkitRunnable {
    private final Perseids perseids;

    public Astronomer(Perseids perseids) {
        this.perseids = perseids;
    }

    public void run() {
        if (this.perseids.getServer().getOnlinePlayers().size() == 0)
            return;
        List<World> worlds = this.perseids.getServer().getWorlds();
        for (World world : worlds) {
            double shootingStarChance, fallingStarChance;
            PerseidsConfig config = this.perseids.configManager.getConfigForWorld(world.getName());
            if (!this.perseids.configManager.doesWorldHaveOverrides(world.getName()) &&
                    !world.getEnvironment().equals(World.Environment.NORMAL))
                continue;
            if (world.getPlayers().size() == 0)
                continue;
            if (world.getTime() < config.beginSpawningStarsTime || world.getTime() > config.endSpawningStarsTime)
                continue;
            if (world.hasStorm())
                continue;
            if (config.newMoonMeteorShower && world.getFullTime() / 24000L % 8L == 4L) {
                shootingStarChance = config.shootingStarsPerMinuteMeteorShower / 120.0D;
                fallingStarChance = config.fallingStarsPerMinuteMeteorShower / 120.0D;
            } else {
                shootingStarChance = config.shootingStarsPerMinute / 120.0D;
                fallingStarChance = config.fallingStarsPerMinute / 120.0D;
            }
            if (config.shootingStarsEnabled && (new Random()).nextDouble() <= shootingStarChance)
                PerseidSphere.createShootingStar(this.perseids, world
                        .getPlayers().get((new Random()).nextInt(world.getPlayers().size())));
            if (config.fallingStarsEnabled && (new Random()).nextDouble() <= fallingStarChance)
                PerseidSphere.createFallingStar(this.perseids, world
                        .getPlayers().get((new Random()).nextInt(world.getPlayers().size())));
        }
    }
}

