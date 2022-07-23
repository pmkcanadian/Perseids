package com.pmkcanadian.perseids;

import com.pmkcanadian.perseids.config.PerseidsConfig;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PerseidSphere {
    public static void createShootingStar(Perseids perseids, Player player) {
        createShootingStar(perseids, player, true);
    }

    public static void createShootingStar(Perseids perseids, Player player, boolean approximate) {
        createShootingStar(perseids, player.getLocation(), approximate);
    }

    public static void createShootingStar(Perseids perseids, Location location) {
        createShootingStar(perseids, location, true);
    }

    public static void createShootingStar(Perseids perseids, Location location, boolean approximate) {
        Location starLocation;
        PerseidsConfig config = perseids.configManager.getConfigForWorld(location.getWorld().getName());
        double w = 100.0D * Math.sqrt((new Random()).nextDouble());
        double t = 6.283185307179586D * (new Random()).nextDouble();
        double x = w * Math.cos(t);
        double range = Math.max(0, config.shootingStarsMaxHeight - config.shootingStarsMinHeight);
        double y = Math.max((new Random()).nextDouble() * range + config.shootingStarsMinHeight, location.getY() + 50.0D);
        double z = w * Math.sin(t);
        if (approximate) {
            starLocation = new Location(location.getWorld(), location.getX() + x, y, location.getZ() + z);
        } else {
            starLocation = new Location(location.getWorld(), location.getX(), y, location.getZ());
        }
        Vector direction = new Vector((new Random()).nextDouble() * 2.0D - 1.0D, (new Random()).nextDouble() * -0.75D, (new Random()).nextDouble() * 2.0D - 1.0D);
        direction.normalize();
        double speed = (new Random()).nextDouble() * 2.0D + 0.75D;
        location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, starLocation, 0, direction.getX(), direction
                .getY(), direction.getZ(), speed, null, true);
        if ((new Random()).nextDouble() >= 0.5D)
            location.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, starLocation, 0, direction.getX(), direction
                    .getY(), direction.getZ(), speed, null, true);
        if (perseids.getConfig().getBoolean("debug"))
            perseids.getLogger().info("Shooting star at " + stringifyLocation(starLocation) + " in world " + starLocation.getWorld().getName());
    }

    public static void createFallingStar(Perseids perseids, Player player) {
        createFallingStar(perseids, player, true);
    }

    public static void createFallingStar(Perseids perseids, Player player, boolean approximate) {
        createFallingStar(perseids, player.getLocation(), approximate);
    }

    public static void createFallingStar(Perseids perseids, Location location) {
        createFallingStar(perseids, location, true);
    }

    public static void createFallingStar(Perseids perseids, Location location, boolean approximate) {
        Location target = location;
        PerseidsConfig config = perseids.configManager.getConfigForWorld(location.getWorld().getName());
        if (approximate) {
            double fallingStarRadius = config.fallingStarsRadius;
            double w = fallingStarRadius * Math.sqrt((new Random()).nextDouble());
            double t = 6.283185307179586D * (new Random()).nextDouble();
            double x = w * Math.cos(t);
            double z = w * Math.sin(t);
            target = new Location(location.getWorld(), location.getX() + x, location.getY(), location.getZ() + z);
        }
        BukkitRunnable fallingStarTask = new FallingStar(perseids, target);
        fallingStarTask.runTaskTimer((Plugin)perseids, 0L, 1L);
        if (perseids.getConfig().getBoolean("debug"))
            perseids.getLogger().info("Falling star at " + stringifyLocation(target) + " in world " + target.getWorld().getName());
    }

    private static String stringifyLocation(Location location) {
        DecimalFormat format = new DecimalFormat("##.00");
        format.setRoundingMode(RoundingMode.HALF_UP);
        return "x: " + format.format(location.getX()) + " y: " + format.format(location.getY()) + " z: " + format.format(location.getZ());
    }
}
