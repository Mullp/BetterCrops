package me.mullp.bettercrops.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.mullp.bettercrops.BetterCrops;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RegionUtil {
  private static Plugin plugin = BetterCrops.getPlugin(BetterCrops.class);

  public static boolean canBuild(final Player player, final Location location) {
    return griefPreventionCanBuild(player, location) && worldGuardCanBuild(player, location);
  }

  private static boolean griefPreventionCanBuild(final Player player, final Location location) {
    if (!plugin.getServer().getPluginManager().isPluginEnabled("GriefPrevention")) return true;

    // Hacky, stupid, overall just a weird methode to do this.
    // Guess what, they wrote it themselves...
    // https://github.com/TechFortress/GriefPrevention/blob/master/src/main/java/me/ryanhamshire/GriefPrevention/BlockEventHandler.java#L265
    String noBuildReason = GriefPrevention.instance.allowBuild(player, location, location.getBlock().getType());
    return noBuildReason == null;
  }

  private static boolean worldGuardCanBuild(final Player player, final Location location) {
    if (!plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard")) return true;

    if (player.hasPermission("worldguard.region.bypass." + location.getWorld().getName())) return true;

    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
    WorldGuardPlatform platform = WorldGuard.getInstance().getPlatform();
    RegionQuery query = platform.getRegionContainer().createQuery();

    return query.testBuild(BukkitAdapter.adapt(location), localPlayer);
  }
}
