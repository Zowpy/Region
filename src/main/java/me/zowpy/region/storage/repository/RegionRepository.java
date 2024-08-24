package me.zowpy.region.storage.repository;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.RequiredArgsConstructor;
import me.zowpy.region.RegionPlugin;
import me.zowpy.region.api.Region;
import org.bson.Document;

@RequiredArgsConstructor
public class RegionRepository implements Repository<String, Region> {

    private final RegionPlugin plugin;

    private final UpdateOptions updateOne = new UpdateOptions().upsert(true);

    @Override
    public Region findById(String key) {
        Document document = plugin.getMongoStorage().getRegions().find(Filters.eq("_id", key)).first();

        if (document == null) return null;

        return RegionPlugin.GSON.fromJson(document.toJson(), Region.class);
    }

    @Override
    public void save(Region value) {
        plugin.getMongoStorage().getRegions().updateOne(
                Filters.eq("_id", value.getName()),
                new Document(
                        "$set", Document.parse(RegionPlugin.GSON.toJson(value))
                ),
                updateOne
        );
    }

    @Override
    public void delete(String key) {
        plugin.getMongoStorage().getRegions().deleteOne(Filters.eq("_id", key));
    }
}
