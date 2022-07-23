package com.pmkcanadian.perseids.commands;

import com.pmkcanadian.perseids.Perseids;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class CommandPerseids implements CommandExecutor {
    Perseids perseids;

    public CommandPerseids(Perseids perseids) {
        this.perseids = perseids;
    }

    public boolean on(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("perseids.reload")) {
                this.perseids.reload();
                sender.sendMessage("Perseids has been reloaded");
            } else {
                sender.sendMessage("You do not have permission to use this command");
            }
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            if (sender.hasPermission("perseids.info")) {
                sender.sendMessage("Perseids v" + this.perseids.getDescription().getVersion());
                sender.sendMessage("Shooting stars: " + (this.perseids.getConfig().getBoolean("shooting-stars-enabled") ? "Enabled" : "Disabled"));
                sender.sendMessage("Falling stars: " + (this.perseids.getConfig().getBoolean("falling-stars-enabled") ? "Enabled" : "Disabled"));
                sender.sendMessage("Meteor showers: " + (this.perseids.getConfig().getBoolean("new-moon-meteor-shower") ? "Enabled" : "Disabled"));
            } else {
                sender.sendMessage("You do not permission to use this command");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }
}