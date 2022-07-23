package com.pmkcanadian.perseids;

import com.pmkcanadian.perseids.config.PerseidsConfig;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FallingStar extends BukkitRunnable {
    private final Perseids perseids;

    private final Location location;

    private final Location dropLoc;

    private final PerseidsConfig config;

    private double y = 256.0D;

    private boolean soundPlayed = false;

    private boolean lootDropped = false;

    private int sparkTimer;

    public FallingStar(Perseids perseids, Location location) {
        this.perseids = perseids;
        this.location = location;
        this.config = perseids.configManager.getConfigForWorld(location.getWorld().getName());
        this.sparkTimer = this.config.fallingStarsSparkTime;
        this
                .dropLoc = new Location(location.getWorld(), location.getX(), (location.getWorld().getHighestBlockAt(location).getY() + 1), location.getZ());
    }

    public void run() {
        double step = 1.0D;
        this.location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, this.location.getX(), this.y, this.location.getZ(), 0, 0.0D, (new Random())
                .nextDouble(), 0.0D, 0.2D, null, true);
        this.location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, this.location.getX(), this.y + (new Random())
                .nextDouble() * step, this.location
                .getZ(), 0, 0.0D, -1.0D, 0.0D, 1.0D, null, true);
        if (this.y % step * 2.0D == 0.0D)
            this.location.getWorld().spawnParticle(Particle.LAVA, this.location.getX(), this.y + (new Random()).nextDouble(), this.location
                    .getZ(), 0, 0.0D, (new Random())
                    .nextDouble(), 0.0D, 0.2D, null, true);
        if (this.config.fallingStarsSoundEnabled && !this.soundPlayed && this.y <= this.dropLoc.getY() + 75.0D) {
            this.location.getWorld().playSound(this.dropLoc, Sound.BLOCK_BELL_RESONATE, (float)this.config.fallingStarsVolume, 0.5F);
            this.soundPlayed = true;
        }
        if (this.y <= this.dropLoc.getY()) {
            if (!this.lootDropped) {
                if (this.config.fallingStarSimpleLoot != null && this.config.fallingStarSimpleLoot.entries.size() > 0) {
                    ItemStack drop = new ItemStack(Material.valueOf((String)this.config.fallingStarSimpleLoot.getRandom()), 1);
                    this.location.getWorld().dropItem(this.dropLoc, drop);
                    if (this.perseids.getConfig().getBoolean("debug"))
                        this.perseids.getLogger().info("Spawned simple falling star loot");
                }
                if (this.config.fallingStarLootTable != null) {
                    Entity marker = this.dropLoc.getWorld().spawnEntity(this.location, EntityType.ARMOR_STAND);
                    String command = String.format("execute at %s run loot spawn %s %s %s loot %s", new Object[] { marker
                            .getUniqueId(),
                            Double.valueOf(this.dropLoc.getX()),
                            Double.valueOf(this.dropLoc.getY()),
                            Double.valueOf(this.dropLoc.getZ()), this.config.fallingStarLootTable });
                    this.perseids.getServer().dispatchCommand((CommandSender)this.perseids.getServer().getConsoleSender(), command);
                    marker.remove();
                    if (this.perseids.getConfig().getBoolean("debug"))
                        this.perseids.getLogger().info("Spawned falling star loot from loot table '" + this.config.fallingStarLootTable + "'");
                }
                if (this.config.fallingStarsExperience > 0) {
                    ExperienceOrb orb = (ExperienceOrb)this.dropLoc.getWorld().spawnEntity(this.dropLoc, EntityType.EXPERIENCE_ORB);
                    orb.setExperience(this.config.fallingStarsExperience);
                    if (this.perseids.getConfig().getBoolean("debug"))
                        this.perseids.getLogger().info("Dropping experience orbs with value " + this.config.fallingStarsExperience);
                }
                this.lootDropped = true;
            }
            if (this.y % step * 5.0D == 0.0D)
                this.location.getWorld().spawnParticle(Particle.LAVA, this.dropLoc, 0, 0.0D, (new Random())
                        .nextDouble(), 0.0D, 1.0D, null, true);
            this.sparkTimer--;
            if (this.sparkTimer <= 0)
                cancel();
        }
        this.y -= step;
    }
}