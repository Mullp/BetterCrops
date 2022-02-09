package me.mullp.bettercrops;

import me.mullp.bettercrops.commands.BetterCropsCommand;
import me.mullp.bettercrops.commands.BetterCropsTabCompletion;
import me.mullp.bettercrops.events.OnCropClick;
import me.mullp.bettercrops.events.OnFarmlandTrample;
import me.mullp.bettercrops.events.OnPotionSplash;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterCrops extends JavaPlugin {

  @Override
  public void onEnable() {
    this.saveDefaultConfig();

    // Events
    this.getServer().getPluginManager().registerEvents(new OnCropClick(), this);
    this.getServer().getPluginManager().registerEvents(new OnFarmlandTrample(), this);
    this.getServer().getPluginManager().registerEvents(new OnPotionSplash(), this);

    // Commands
    this.getCommand("bettercrops").setExecutor(new BetterCropsCommand());
    this.getCommand("bettercrops").setTabCompleter(new BetterCropsTabCompletion());
  }
}
