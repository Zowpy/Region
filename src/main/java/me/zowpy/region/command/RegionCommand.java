package me.zowpy.region.command;

import me.zowpy.command.annotation.Command;
import me.zowpy.command.annotation.Named;
import me.zowpy.command.annotation.Permission;
import me.zowpy.command.annotation.Sender;
import me.zowpy.region.RegionPlugin;
import me.zowpy.region.api.Flag;
import me.zowpy.region.api.FlagAccess;
import me.zowpy.region.api.Region;
import me.zowpy.region.menus.RegionsMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RegionCommand {

    private RegionPlugin plugin;

    @Permission("region.menu")
    @Command(name = "region", async = false)
    public void help(@Sender CommandSender sender) {
        if (sender instanceof Player) {
            new RegionsMenu().openMenu((Player) sender);
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Region Commands");
        sender.sendMessage(ChatColor.GREEN + "/region create <name>");
        sender.sendMessage(ChatColor.GREEN + "/region wand <region>");
        sender.sendMessage(ChatColor.GREEN + "/region add <region> <player>");
        sender.sendMessage(ChatColor.GREEN + "/region remove <region> <player>");
        sender.sendMessage(ChatColor.GREEN + "/region whitelist <region>");
        sender.sendMessage(ChatColor.GREEN + "/region flag <region> <flag> <state>");
    }

    @Permission("region.create")
    @Command(name = "region create")
    public void create(@Sender Player player, @Named("name") String name) {
        if (plugin.getRegionManager().getRegion(name) != null) {
            player.sendMessage(ChatColor.RED + "A region with that name already exists.");
            return;
        }

        Region region = plugin.getRegionManager().createRegion(name, null, null);
        plugin.getRegionRepository().save(region);

        player.sendMessage(ChatColor.GREEN + "Successfully created region.");
    }

    @Permission("region.wand")
    @Command(name = "region wand", async = false)
    public void wand(@Sender Player player, @Named("region") Region region) {
        player.setMetadata("region-wand", new FixedMetadataValue(plugin, region.getName()));

        ItemStack wand = new ItemStack(Material.STICK);
        ItemMeta meta = wand.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "Region Wand");
        meta.setLore(Arrays.asList(
                ChatColor.YELLOW + "Left click to set position #1",
                ChatColor.YELLOW + "Right click to set position #2"
        ));

        wand.setItemMeta(meta);

        if (!player.getInventory().addItem(wand).isEmpty()) {
            player.sendMessage(ChatColor.RED + "Please free up some space in your inventory.");
        }
    }

    @Permission("region.add")
    @Command(name = "region add")
    public void add(@Sender CommandSender sender, @Named("region") Region region, @Named("player") OfflinePlayer target) {
        region.addWhitelist(target.getPlayer());
        plugin.getRegionManager().save(region);

        sender.sendMessage(ChatColor.GREEN + "You have whitelisted " + target.getName() + ".");
    }

    @Permission("region.remove")
    @Command(name = "region remove")
    public void remove(@Sender CommandSender sender, @Named("region") Region region, @Named("player") OfflinePlayer target) {
        if (!region.isWhitelisted(target.getPlayer())) {
            sender.sendMessage(ChatColor.RED + target.getName() + " is not whitelisted.");
            return;
        }

        region.removeWhitelist(target.getPlayer());
        plugin.getRegionManager().save(region);

        sender.sendMessage(ChatColor.GREEN + "You have unwhitelisted " + target.getName() + ".");
    }

    @Permission("region.whitelist")
    @Command(name = "region whitelist")
    public void whitelist(@Sender CommandSender sender, @Named("region") Region region) {
        sender.sendMessage(ChatColor.GREEN + region.getName() + "'s whitelisted users:");
        sender.sendMessage(
                ChatColor.YELLOW + region.getWhitelisted()
                        .stream()
                        .map(uuid -> plugin.getServer().getOfflinePlayer(uuid).getName())
                        .collect(Collectors.joining(", "))
        );
    }

    @Permission("region.flag")
    @Command(name = "region flag")
    public void flag(@Sender CommandSender sender, @Named("region") Region region, @Named("flag") Flag flag, @Named("state") FlagAccess access) {
        region.addFlag(flag, access);
        plugin.getRegionManager().save(region);

        sender.sendMessage(ChatColor.GREEN + "Successfully added flag '" + flag.getDisplay() + "' with state '" + access.getDisplayName() + "' to region '" + region.getName() + "'");
    }
}
