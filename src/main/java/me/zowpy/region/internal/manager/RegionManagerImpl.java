package me.zowpy.region.internal.manager;

import lombok.RequiredArgsConstructor;
import me.zowpy.region.RegionPlugin;
import me.zowpy.region.api.Region;
import me.zowpy.region.api.manager.RegionManager;
import me.zowpy.region.internal.RegionImpl;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RegionManagerImpl implements RegionManager {

    private final RegionPlugin plugin;

    private final List<Region> regions = new ArrayList<>();

    public RegionManager init() {
        for (Document document : plugin.getMongoStorage().getRegions().find()) {
            if (document == null) continue;

            regions.add(RegionPlugin.GSON.fromJson(document.toJson(), RegionImpl.class));
        }

        return this;
    }

    @Override
    public Region getRegion(String name) {
        return regions.stream().filter(region -> region.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    @Override
    public List<Region> getRegions() {
        return Collections.unmodifiableList(regions);
    }

    @Override
    public void save(Region region) {
        if (Bukkit.isPrimaryThread()) {
            CompletableFuture.runAsync(() -> save(region));
        }else {
            plugin.getRegionRepository().save(region);
        }
    }

    @Override
    public void delete(String region) {
        plugin.getRegionRepository().delete(region);
    }

    @Override
    public Region createRegion(String name, Location position1, Location position2) {
        RegionImpl region = new RegionImpl(name);
        region.setPoint1(position1);
        region.setPoint2(position2);

        regions.add(region);

        return region;
    }

    @Override
    public List<Region> getRegions(Location location) {
        return regions.stream().filter(region -> region.inRegion(location))
                .collect(Collectors.toUnmodifiableList());
    }
}
