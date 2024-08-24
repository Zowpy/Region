package me.zowpy.region.listener;

import lombok.RequiredArgsConstructor;
import me.zowpy.region.RegionPlugin;
import me.zowpy.region.api.Region;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class WandListener implements Listener {

    private final RegionPlugin plugin;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!player.hasMetadata("region-wand")) return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() == Material.STICK && itemStack.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Region Wand") && itemStack.getItemMeta().hasLore()) {
            Region region = plugin.getRegionManager().getRegion(player.getMetadata("region-wand").get(0).asString());

            event.setCancelled(true);

            if (region == null) {
                player.sendMessage(ChatColor.RED + "Region not found.");
                player.removeMetadata("region-wand", plugin);
                return;
            }

            region.setPosition1(event.getBlock().getLocation());
            plugin.getRegionManager().save(region);

            player.sendMessage(ChatColor.GREEN + "You have set " + region.getName() + "'s position #1");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.hasItem() || !event.hasBlock()) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();

        if (!player.hasMetadata("region-wand")) return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() == Material.STICK && itemStack.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Region Wand") && itemStack.getItemMeta().hasLore()) {
            Region region = plugin.getRegionManager().getRegion(player.getMetadata("region-wand").get(0).asString());

            event.setCancelled(true);

            if (region == null) {
                player.sendMessage(ChatColor.RED + "Region not found.");
                player.removeMetadata("region-wand", plugin);
                return;
            }

            region.setPosition2(event.getClickedBlock().getLocation());
            plugin.getRegionManager().save(region);

            player.sendMessage(ChatColor.GREEN + "You have set " + region.getName() + "'s position #2");
        }
    }
}
