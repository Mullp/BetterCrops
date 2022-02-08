package me.mullp.bettercrops.utils;

import me.mullp.bettercrops.BetterCrops;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RegionUtil {
  private static Plugin plugin = BetterCrops.getPlugin(BetterCrops.class);

  public static boolean canBuild(final Player player, final Location location) {
    if (griefPreventionCanBuild(player, location)) {
      return true;
    } else {
      return false;
    }
  }

  private static boolean griefPreventionCanBuild(final Player player, final Location location) {
    if (plugin.getServer().getPluginManager().isPluginEnabled("GriefPrevention")) {
      // Hacky, stupid, overall just a weird methode to do this.
      // Guess what, they wrote it themselves...
      // https://github.com/TechFortress/GriefPrevention/blob/master/src/main/java/me/ryanhamshire/GriefPrevention/BlockEventHandler.java#L265
      String noBuildReason = GriefPrevention.instance.allowBuild(player, location, location.getBlock().getType());
      return noBuildReason == null;
    }

    return true;
  }
}
