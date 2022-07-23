package com.pmkcanadian.perseids.commands;

import com.pmkcanadian.perseids.Perseids;
import com.pmkcanadian.perseids.PerseidSphere;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandShootingStar implements CommandExecutor {
    Perseids perseids;

    public CommandShootingStar(Perseids perseids) {
        this.perseids = perseids;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage("§cError: Player not found.");
                return true;
            }
            PerseidSphere.createShootingStar(this.perseids, Bukkit.getPlayer(args[0]), false);
        } else if (sender instanceof Player) {
            Player player = (Player)sender;
            PerseidSphere.createShootingStar(this.perseids, player, false);
        } else {
            return false;
        }
        String message = this.perseids.getConfig().getString("shooting-stars-summon-text");
        if (message != null)
            sender.sendMessage(message);
        return true;
    }
}
