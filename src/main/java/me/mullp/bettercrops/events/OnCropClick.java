package me.mullp.bettercrops.events;

import me.mullp.bettercrops.BetterCrops;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class OnCropClick implements Listener {
  Plugin plugin = BetterCrops.getPlugin(BetterCrops.class);

  private static final Set<Material> cropTypes = Set.of(Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.BEETROOTS, Material.NETHER_WART, Material.COCOA);
  private static final Set<Material> seedTypes = Set.of(Material.WHEAT_SEEDS, Material.CARROT, Material.POTATO, Material.BEETROOT_SEEDS, Material.NETHER_WART, Material.COCOA_BEANS);

  @EventHandler(priority = EventPriority.LOWEST)
  public void onCropClick(PlayerInteractEvent event) {
    if (event.isCancelled()) return;

    if (plugin.getConfig().getBoolean("quick-harvest") && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand().equals(EquipmentSlot.HAND)) {
      Block block = event.getClickedBlock();
      if (cropTypes.contains(block.getType())) {
        Set<Material> quickHarvestCrops = new HashSet<>();
        plugin.getConfig().getStringList("quick-harvest-crops").forEach(material -> {
          quickHarvestCrops.add(Material.getMaterial(material.toUpperCase()));
        });

        if (quickHarvestCrops.contains(block.getType())) { // Config contains clicked block
          Ageable age = (Ageable) block.getBlockData();
          if (age.getAge() == age.getMaximumAge()) {

            List<ItemStack> drops = new ArrayList<>(block.getDrops(event.getPlayer().getInventory().getItemInMainHand()));

            int index = 0;
            for (ItemStack drop : drops) {
              if (seedTypes.contains(drop.getType())) {
                drop.setAmount(drop.getAmount() - 1);
                drops.set(index, drop);
                break;
              }
              index++;
            }

            drops.forEach(drop -> {
              if (drop.getAmount() > 0) {
                block.getWorld().dropItemNaturally(block.getLocation(), drop);
              }
            });

            age.setAge(0); // Reset crop to fresh
            block.setBlockData(age);
          }
        }
      }
    }
  }
}
