package me.mullp.bettercrops.utils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockUtil {
  public static List<Location> generateSphere(Location centerBlock, double radius, boolean hollow) {
    if (centerBlock == null || radius < 0) {
      return new ArrayList<>();
    }

    List<Location> circleBlocks = new ArrayList<>();

    int bx = centerBlock.getBlockX();
    int by = centerBlock.getBlockY();
    int bz = centerBlock.getBlockZ();

    for(double x = bx - radius; x <= bx + radius; x++) {
      for(double y = by - radius; y <= by + radius; y++) {
        for(double z = bz - radius; z <= bz + radius; z++) {

          double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));

          if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

            Location l = new Location(centerBlock.getWorld(), x, y, z);

            circleBlocks.add(l);

          }

        }
      }
    }

    return circleBlocks;
  }

}
