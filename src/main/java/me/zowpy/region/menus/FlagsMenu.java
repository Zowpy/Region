package me.zowpy.region.menus;

import lombok.RequiredArgsConstructor;
import me.zowpy.menu.buttons.Button;
import me.zowpy.menu.pagination.PaginatedMenu;
import me.zowpy.menu.utils.ItemBuilder;
import me.zowpy.region.RegionPlugin;
import me.zowpy.region.api.Flag;
import me.zowpy.region.api.FlagAccess;
import me.zowpy.region.api.Region;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class FlagsMenu extends PaginatedMenu {

    private final Region region;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Flags";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {

        Map<Integer, Button> toReturn = new HashMap<>();

        for (Flag flag : RegionPlugin.getInstance().getFlagRegistry().getFlags()) {
            FlagAccess access = region.getAccessType(flag.getName());

            if (access == null) {
                access = FlagAccess.NONE;
            }

            FlagAccess finalAccess = access;
            toReturn.put(toReturn.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(getMaterial(finalAccess))
                            .name(flag.getDisplay())
                            .lore(Arrays.asList(
                                    symbol(FlagAccess.NONE, finalAccess) + " &cNone",
                                    symbol(FlagAccess.WHITELIST, finalAccess) + " &eWhitelist",
                                    symbol(FlagAccess.EVERYONE, finalAccess) + " &aEveryone",
                                    " ",
                                    "&aClick to change"
                            )).build();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    switch (finalAccess) {
                        case NONE -> {
                            region.addFlag(flag, FlagAccess.WHITELIST);
                            player.sendMessage(ChatColor.GREEN + "You have set the flag access to " + FlagAccess.WHITELIST.getDisplayName());
                        }

                        case WHITELIST -> {
                            region.addFlag(flag, FlagAccess.EVERYONE);
                            player.sendMessage(ChatColor.GREEN + "You have set the flag access to " + FlagAccess.EVERYONE.getDisplayName());
                        }

                        case EVERYONE -> {
                            region.addFlag(flag, FlagAccess.NONE);
                            player.sendMessage(ChatColor.GREEN + "You have set the flag access to " + FlagAccess.NONE.getDisplayName());
                        }
                    }

                    RegionPlugin.getInstance().getRegionManager().save(region);
                    openMenu(player);
                }
            });
        }

        return toReturn;
    }

    private Material getMaterial(FlagAccess access) {
        switch (access) {
            case NONE -> {
                return Material.RED_WOOL;
            }

            case WHITELIST -> {
                return Material.YELLOW_WOOL;
            }

            case EVERYONE -> {
                return Material.GREEN_WOOL;
            }

            default -> {
                return Material.REDSTONE_BLOCK;
            }
        }
    }

    private String symbol(FlagAccess access, FlagAccess current) {
        if (access == current) {
            return "&a■";
        }

        return "&c■";
    }
}
