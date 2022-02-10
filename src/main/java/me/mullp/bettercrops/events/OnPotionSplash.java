package me.mullp.bettercrops.events;

import me.mullp.bettercrops.BetterCrops;
import me.mullp.bettercrops.utils.BlockUtil;
import me.mullp.bettercrops.utils.RegionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class OnPotionSplash implements Listener {
  Plugin plugin = BetterCrops.getPlugin(BetterCrops.class);

  private static final Set<PotionType> waterPotionTypes = Set.of(PotionType.WATER, PotionType.MUNDANE, PotionType.THICK, PotionType.AWKWARD);
  private static final Set<Material> cropTypes = Set.of(Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.BEETROOTS, Material.NETHER_WART, Material.COCOA);

  @EventHandler
  public void onPotionSplash(PotionSplashEvent event) {
    if (!plugin.getConfig().getBoolean("potions-affect-crops")) return;

    ThrownPotion thrownPotion = event.getPotion();
    ProjectileSource projectileSource = thrownPotion.getShooter();

    if (!(projectileSource instanceof Player)) return; // Potion not thrown by player

    Player player = (Player) projectileSource;

    Location hitLocation = event.getEntity().getLocation();
    PotionType potionType = ((PotionMeta) event.getPotion().getItem().getItemMeta()).getBasePotionData().getType();

    if (waterPotionTypes.contains(potionType)) { // Potion is water type
      for (Location location : BlockUtil.generateSphere(hitLocation, Math.random() + 1.5, false)) {
        Block block = location.getBlock();

        if (!block.getType().equals(Material.FARMLAND)) continue;

        if (!RegionUtil.canBuild(player, block.getLocation())) continue;

        Farmland farmland = (Farmland) block.getBlockData();
        if (farmland.getMoisture() == farmland.getMaximumMoisture()) continue;

        farmland.setMoisture(farmland.getMaximumMoisture());
        block.setBlockData(farmland);
      }
    }

    Set<PotionEffectType> enabledPotions = new HashSet<>();
    plugin.getConfig().getStringList("potions").forEach(potion -> enabledPotions.add(PotionEffectType.getByName(potion.toUpperCase())));

    Collection<PotionEffect> potionEffects = event.getPotion().getEffects();
    for (PotionEffect potionEffect : potionEffects) {
      int amplifier = potionEffect.getAmplifier();
      double radiusAmplifier = plugin.getConfig().getDouble("potion-radius-amplifier") >= 11 ? 10 : plugin.getConfig().getDouble("potion-radius-amplifier");
      double radius = Math.random() + radiusAmplifier + (amplifier >= 6 ? 5 : amplifier);

      if (potionEffect.getType().equals(PotionEffectType.HEAL) && enabledPotions.contains(PotionEffectType.HEAL)) {
        for (Location location : BlockUtil.generateSphere(hitLocation, radius, false)) {
          Block block = location.getBlock();

          if (!RegionUtil.canBuild(player, block.getLocation())) continue;

          if (cropTypes.contains(block.getType())) {
            Ageable age = (Ageable) block.getBlockData();
            if (age.getAge() == age.getMaximumAge()) continue;

            age.setAge(age.getMaximumAge());
            block.setBlockData(age);
          }
        }
      } else if (potionEffect.getType().equals(PotionEffectType.HARM) && enabledPotions.contains(PotionEffectType.HARM)) {
        for (Location location : BlockUtil.generateSphere(hitLocation, radius, false)) {
          Block block = location.getBlock();

          if (!RegionUtil.canBuild(player, block.getLocation())) continue;

          if (cropTypes.contains(block.getType())) {
            Ageable age = (Ageable) block.getBlockData();
            if (age.getAge() == 0) continue;

            age.setAge(0);
            block.setBlockData(age);
          }
        }
      }
    }
  }
}

