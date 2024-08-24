package me.zowpy.region.menus;

import lombok.RequiredArgsConstructor;
import me.zowpy.menu.Menu;
import me.zowpy.menu.buttons.Button;
import me.zowpy.menu.utils.ItemBuilder;
import me.zowpy.region.RegionPlugin;
import me.zowpy.region.internal.RegionImpl;
import me.zowpy.region.menus.prompt.SimpleStringPrompt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class RegionEditMenu extends Menu {

    private final RegionImpl region;

    @Override
    public String getTitle(Player player) {
        return "Region Editor";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {

        Map<Integer, Button> toReturn = new HashMap<>();

        toReturn.put(0, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.NAME_TAG)
                        .name("&aRename")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();

                new SimpleStringPrompt("&aPlease type a new name, or type &c'cancel' &ato cancel", s -> {
                    if (!s.equalsIgnoreCase("cancel")) {
                        RegionPlugin.getInstance().getRegionManager().delete(region.getName());
                        region.setName(s.trim());

                        RegionPlugin.getInstance().getRegionManager().save(region);
                        player.sendMessage(ChatColor.GREEN + "You have renamed the region to " + s.trim() + ".");
                    }

                    openMenu(player);
                });
            }
        });

        toReturn.put(1, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.GREEN_WOOL)
                        .name("&aAdd whitelist")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();

                new SimpleStringPrompt("&aPlease type a player's name to whitelist, or type &c'cancel' &ato cancel", s -> {
                    if (!s.equalsIgnoreCase("cancel")) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(s.trim());

                        if (offlinePlayer.hasPlayedBefore()) {
                            region.addWhitelist(offlinePlayer.getUniqueId());
                            RegionPlugin.getInstance().getRegionManager().save(region);

                            player.sendMessage(ChatColor.GREEN + "You have whitelisted " + offlinePlayer.getName() + ".");
                        }else {
                            player.sendMessage(ChatColor.RED + s.trim() + " has not played the server before.");
                        }
                    }

                    openMenu(player);
                });
            }
        });

        toReturn.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.RED_WOOL)
                        .name("&aRemove whitelist")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();

                new SimpleStringPrompt("&aPlease type a player's name to remove from whitelist, or type &c'cancel' &ato cancel", s -> {
                    if (!s.equalsIgnoreCase("cancel")) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(s.trim());

                        if (offlinePlayer.hasPlayedBefore()) {
                            region.removeWhitelist(offlinePlayer.getUniqueId());
                            RegionPlugin.getInstance().getRegionManager().save(region);

                            player.sendMessage(ChatColor.GREEN + "You have removed " + offlinePlayer.getName() + " from whitelist.");
                        }else {
                            player.sendMessage(ChatColor.RED + s.trim() + " has not played the server before.");
                        }
                    }

                    openMenu(player);
                });
            }
        });

        toReturn.put(3, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.STICK)
                        .name("&aWand")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();

                player.chat("/region wand " + region.getName());
            }
        });


        toReturn.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.BLACK_BANNER)
                        .name("Flags")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                new FlagsMenu(region).openMenu(player);
            }
        });

        return toReturn;
    }
}
