package me.zowpy.region.internal;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.zowpy.region.api.Flag;
import me.zowpy.region.api.FlagAccess;
import me.zowpy.region.api.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

@Data
@RequiredArgsConstructor
public class RegionImpl implements Region {

    @SerializedName("_id")
    private String name;

    private final Map<String, FlagAccess> flags = new HashMap<>();
    private final List<UUID> whitelisted = new ArrayList<>();

    private Location point1, point2;

    public RegionImpl(String name) {
        this.name = name;
    }

    public Map<String, FlagAccess> getFlags() {
        return Collections.unmodifiableMap(flags);
    }

    @Override
    public void addFlag(Flag flag, FlagAccess access) {
        flags.put(flag.getName().toLowerCase(), access);
    }

    @Override
    public void removeFlag(String name) {
        flags.remove(name.toLowerCase());
    }

    @Override
    public void addWhitelist(UUID uuid) {
        whitelisted.add(uuid);
    }

    @Override
    public void addWhitelist(Player player) {
        addWhitelist(player.getUniqueId());
    }

    @Override
    public void removeWhitelist(UUID uuid) {
        whitelisted.remove(uuid);
    }

    @Override
    public void removeWhitelist(Player player) {
        removeWhitelist(player.getUniqueId());
    }

    @Override
    public boolean isWhitelisted(Player player) {
        return whitelisted.contains(player.getUniqueId());
    }

    @Override
    public List<UUID> getWhitelisted() {
        return Collections.unmodifiableList(whitelisted);
    }

    @Override
    public boolean hasFlag(String name) {
        return flags.containsKey(name.toLowerCase());
    }

    @Override
    public FlagAccess getAccessType(String flag) {
        return flags.get(flag);
    }

    @Override
    public boolean canAccess(Flag flag, Player player) {
        return canAccess(flag.getName().toLowerCase(), player);
    }

    @Override
    public boolean canAccess(String flag, Player player) {
        FlagAccess access = getAccessType(flag);

        if (access == null) return false;
        if (player.hasPermission("region.bypass")) return true;

        switch (access) {
            case WHITELIST -> {
                return isWhitelisted(player);
            }

            case EVERYONE -> {
                return true;
            }

            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean inRegion(Location location) {
        if (location == null || location.getWorld() == null) return false;
        if (point1 == null || point2 == null) return false;

        if (!location.getWorld().getName().equals(point1.getWorld().getName())) {
            return false;
        }

        double minX = Math.min(point1.getBlockX(), point2.getBlockX());
        double minY = Math.min(point1.getBlockY(), point2.getBlockY());
        double minZ = Math.min(point1.getBlockZ(), point2.getBlockZ());

        double maxX = Math.max(point1.getBlockX(), point2.getBlockX());
        double maxY = Math.max(point1.getBlockY(), point2.getBlockY());
        double maxZ = Math.max(point1.getBlockZ(), point2.getBlockZ());

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return minX <= x && maxX >= x && minY <= y && maxY >= y && minZ <= z && maxZ >= z;
    }

    @Override
    public void setPosition1(Location location) {
        this.point1 = location;
    }

    @Override
    public void setPosition2(Location location) {
        this.point2 = location;
    }
}
