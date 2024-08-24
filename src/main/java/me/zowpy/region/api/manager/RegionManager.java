package me.zowpy.region.api.manager;

import me.zowpy.region.api.Region;
import org.bukkit.Location;

import java.util.List;

public interface RegionManager {

    Region getRegion(String name);

    List<Region> getRegions();

    void save(Region region);

    void delete(String region);

    Region createRegion(String name, Location position1, Location position2);

    List<Region> getRegions(Location location);
}
