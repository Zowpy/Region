package me.zowpy.region.command.providers;

import lombok.RequiredArgsConstructor;
import me.zowpy.command.provider.Provider;
import me.zowpy.command.provider.exception.CommandExitException;
import me.zowpy.region.RegionPlugin;
import me.zowpy.region.api.Region;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RegionProvider implements Provider<Region> {

    private final RegionPlugin plugin;

    @Override
    public Region provide(String s) throws CommandExitException {
        Region region = plugin.getRegionManager().getRegion(s);

        if (region == null) {
            throw new CommandExitException(ChatColor.RED + "No region with the name '" + s + "' found.");
        }

        return region;
    }

    @Override
    public List<String> tabComplete(Player player, String arg) {
        return plugin.getRegionManager().getRegions().stream().map(Region::getName)
                .collect(Collectors.toList());
    }
}
