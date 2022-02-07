package me.mullp.bettercrops.commands;

import me.mullp.bettercrops.BetterCrops;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class BetterCropsCommand implements CommandExecutor {
  Plugin plugin = BetterCrops.getPlugin(BetterCrops.class);

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      return false;
    } else {
      if (args[0].equalsIgnoreCase("reload")) {
        if (!sender.hasPermission("bettercrops.reload")) {
          sender.sendMessage(ChatColor.RED + "No permission!");
          return true;
        }

        plugin.reloadConfig();
        sender.sendMessage(ChatColor.WHITE + "Configuration file has been reloaded.");
        return true;
      }
    }

    return true;
  }
}
