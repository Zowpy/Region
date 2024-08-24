package me.zowpy.region.command.providers;

import lombok.RequiredArgsConstructor;
import me.zowpy.command.provider.Provider;
import me.zowpy.command.provider.exception.CommandExitException;
import me.zowpy.region.RegionPlugin;
import me.zowpy.region.api.Flag;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FlagProvider implements Provider<Flag> {

    private final RegionPlugin plugin;

    @Override
    public Flag provide(String s) throws CommandExitException {
        Flag flag = plugin.getFlagRegistry().getFlag(s);

        if (flag == null) {
            throw new CommandExitException(ChatColor.RED + "Flag with name '" + s + "' not found.");
        }

        return flag;
    }

    @Override
    public List<String> tabComplete(Player player, String arg) {
        return plugin.getFlagRegistry().getFlags().stream().map(flag -> flag.getName().toLowerCase())
                .collect(Collectors.toList());
    }
}
