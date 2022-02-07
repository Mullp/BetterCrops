package me.mullp.bettercrops.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class BetterCropsTabCompletion implements TabCompleter {
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (args.length == 1 && sender.hasPermission("bettercrops.reload")) {
      List<String> completions = new ArrayList<>();
      completions.add("reload");

      return completions;
    }

    return new ArrayList<>();
  }
}
