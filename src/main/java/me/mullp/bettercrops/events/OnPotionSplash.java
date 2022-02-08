package me.mullp.bettercrops.events;

import me.mullp.bettercrops.utils.BlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Collection;
import java.util.Set;

public class OnPotionSplash implements Listener {
  private static final Set<PotionType> waterPotionTypes = Set.of(PotionType.WATER, PotionType.MUNDANE, PotionType.THICK, PotionType.AWKWARD);
  private static final Set<Material> cropTypes = Set.of(Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.BEETROOTS, Material.NETHER_WART, Material.COCOA);

  @EventHandler
  public void onPotionSplash(PotionSplashEvent event) {
    Location hitLocation = event.getEntity().getLocation();
    PotionType potionType = ((PotionMeta) event.getPotion().getItem().getItemMeta()).getBasePotionData().getType();

    if (waterPotionTypes.contains(potionType)) { // Potion is water type
      for (Location location : BlockUtil.generateSphere(hitLocation, Math.random() + 1.5, false)) {
        Block block = location.getBlock();

        if (!block.getType().equals(Material.FARMLAND)) continue;

        Farmland farmland = (Farmland) block.getBlockData();
        if (farmland.getMoisture() == farmland.getMaximumMoisture()) continue;

        farmland.setMoisture(farmland.getMaximumMoisture());
        block.setBlockData(farmland);
      }
    }

    Collection<PotionEffect> potionEffects = event.getPotion().getEffects();
    for (PotionEffect potionEffect : potionEffects) {
      int amplifier = potionEffect.getAmplifier();

      Bukkit.getLogger().info("Effect: " + potionEffect.getType());
      Bukkit.getLogger().info("Effect amplifier: " + amplifier);


      if (potionEffect.getType().equals(PotionEffectType.HEAL)) {
        for (Location location : BlockUtil.generateSphere(hitLocation, Math.random() + 1.5 + amplifier, false)) {
          Block block = location.getBlock();
          if (cropTypes.contains(block.getType())) {
            Ageable age = (Ageable) block.getBlockData();
            if (age.getAge() == age.getMaximumAge()) return;

            age.setAge(age.getAge() + 1);
            block.setBlockData(age);
          }
        }

      } else if (potionEffect.getType().equals(PotionEffectType.HARM)) {
        for (Location location : BlockUtil.generateSphere(hitLocation, Math.random() + 1.5 + amplifier, false)) {
          Block block = location.getBlock();

          block.setType(Material.STONE);
          if (cropTypes.contains(block.getType())) {
            Ageable age = (Ageable) block.getBlockData();
            if (age.getAge() == 0) return;

            age.setAge(age.getAge() - 1);
            block.setBlockData(age);
          }
        }
      }
    }
  }
}

