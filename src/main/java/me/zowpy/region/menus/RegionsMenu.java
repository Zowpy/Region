package me.zowpy.region.menus;

import me.zowpy.menu.buttons.Button;
import me.zowpy.menu.pagination.PaginatedMenu;
import me.zowpy.menu.utils.ItemBuilder;
import me.zowpy.region.RegionPlugin;
import me.zowpy.region.api.Region;
import me.zowpy.region.internal.RegionImpl;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegionsMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Regions";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {

        Map<Integer, Button> toReturn = new HashMap<>();

        for (Region region : RegionPlugin.getInstance().getRegionManager().getRegions()) {
            toReturn.put(toReturn.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.BOOK)
                            .name(ChatColor.GREEN + region.getName())
                            .lore(Arrays.asList(
                                    " ",
                                    "&aClick to edit region."
                            )).build();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    new RegionEditMenu((RegionImpl) region).openMenu(player);
                }
            });
        }

        return toReturn;
    }
}
