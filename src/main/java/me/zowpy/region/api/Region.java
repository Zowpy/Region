package me.zowpy.region.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Region {

    String getName();

    Map<String, FlagAccess> getFlags();

    void addFlag(Flag flag, FlagAccess access);

    void removeFlag(String flag);

    void addWhitelist(UUID uuid);

    void addWhitelist(Player player);

    void removeWhitelist(UUID uuid);

    void removeWhitelist(Player player);

    boolean isWhitelisted(Player player);

    List<UUID> getWhitelisted();

    boolean hasFlag(String flag);

    FlagAccess getAccessType(String flag);

    boolean canAccess(Flag flag, Player player);

    boolean canAccess(String flag, Player player);

    boolean inRegion(Location location);

    void setPosition1(Location location);
    void setPosition2(Location location);
}
