package me.mullp.bettercrops.events;

import me.mullp.bettercrops.BetterCrops;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class OnFarmlandTrample implements Listener {
  Plugin plugin = BetterCrops.getPlugin(BetterCrops.class);

  @EventHandler(priority = EventPriority.LOWEST)
  public void onFarmlandTrample(PlayerInteractEvent event) {
    if (event.isCancelled()) return;

    if (plugin.getConfig().getBoolean("anti-trample")) {
      event.setCancelled(true);
      return;
    }

    if (!plugin.getConfig().getBoolean("anti-trample-feather-falling")) return;

    Player player = event.getPlayer();
    Block block = event.getClickedBlock();
    ItemStack boots = player.getInventory().getBoots();
    if (block != null && block.getType().equals(Material.FARMLAND)
            && event.getAction().equals(Action.PHYSICAL)
            && boots != null
            && boots.containsEnchantment(Enchantment.PROTECTION_FALL)) { // Farmland trampled with PROTECTION_FALL boots on

      if (percentChance(plugin.getConfig().getDouble("anti-trample-percentages.feather-falling-" + boots.getEnchantmentLevel(Enchantment.PROTECTION_FALL)) / 100)) {
        event.setCancelled(true);
      }
    }

  }

  private static Boolean percentChance(double chance) {
    return Math.random() <= chance;
  }
}
