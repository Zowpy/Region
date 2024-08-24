package me.zowpy.region.listener;

import lombok.RequiredArgsConstructor;
import me.zowpy.region.RegionPlugin;
import me.zowpy.region.internal.DefaultFlags;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class DefaultFlagsListener implements Listener {

    private final RegionPlugin plugin;

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        plugin.getRegionManager().getRegions(event.getBlock().getLocation()).stream()
                .peek(region -> event.setCancelled(true))
                .forEach(region -> {
                    if (region.canAccess(DefaultFlags.BLOCK_BREAK, player)) {
                        event.setCancelled(false);
                    }
                });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        plugin.getRegionManager().getRegions(event.getBlock().getLocation()).stream()
                .peek(region -> event.setCancelled(true))
                .forEach(region -> {
            if (region.canAccess(DefaultFlags.BLOCK_PLACE, player)) {
                event.setCancelled(false);
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        plugin.getRegionManager().getRegions(event.getPlayer().getLocation()).stream()
                .peek(region -> event.setCancelled(true))
                .forEach(region -> {
            if (region.canAccess(DefaultFlags.INTERACT, player)) {
                event.setCancelled(false);
            }
        });
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {

            Player damager = (Player) event.getDamager();

            plugin.getRegionManager().getRegions(damager.getLocation()).stream()
                    .peek(region -> event.setCancelled(true))
                    .forEach(region -> {
                if (!region.canAccess(DefaultFlags.ENTITY_DAMAGE, damager)) {
                    event.setCancelled(true);
                }
            });

        }
    }
}
