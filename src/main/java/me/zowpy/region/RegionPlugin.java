package me.zowpy.region;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.zowpy.command.CommandAPI;
import me.zowpy.command.provider.impl.primitive.EnumProvider;
import me.zowpy.menu.MenuAPI;
import me.zowpy.region.adapter.LocationTypeAdapter;
import me.zowpy.region.api.Flag;
import me.zowpy.region.api.FlagAccess;
import me.zowpy.region.api.Region;
import me.zowpy.region.api.manager.RegionManager;
import me.zowpy.region.command.RegionCommand;
import me.zowpy.region.command.providers.FlagProvider;
import me.zowpy.region.command.providers.RegionProvider;
import me.zowpy.region.internal.DefaultFlags;
import me.zowpy.region.internal.manager.RegionManagerImpl;
import me.zowpy.region.listener.DefaultFlagsListener;
import me.zowpy.region.listener.WandListener;
import me.zowpy.region.registry.FlagRegistry;
import me.zowpy.region.storage.MongoCredentials;
import me.zowpy.region.storage.MongoStorage;
import me.zowpy.region.storage.repository.RegionRepository;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class RegionPlugin extends JavaPlugin {

    public static Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(Location.class, new LocationTypeAdapter())
            .create();

    @Getter
    private static RegionPlugin instance;

    private MongoStorage mongoStorage;
    private FlagRegistry flagRegistry;

    private RegionRepository regionRepository;
    private RegionManager regionManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        mongoStorage = new MongoStorage(
                new MongoCredentials(
                        getConfig().getString("mongodb.host"),
                        getConfig().getInt("mongodb.port"),
                        getConfig().getString("mongodb.database"),
                        getConfig().getBoolean("mongodb.authentication.enabled"),
                        getConfig().getString("mongodb.authentication.user"),
                        getConfig().getString("mongodb.authentication.password"),
                        getConfig().getBoolean("mongodb.uri.enabled"),
                        getConfig().getString("mongodb.uri.connection-string")
                )
        );

        flagRegistry = new FlagRegistry();
        flagRegistry.registerFlag(DefaultFlags.INTERACT);
        flagRegistry.registerFlag(DefaultFlags.BLOCK_BREAK);
        flagRegistry.registerFlag(DefaultFlags.BLOCK_PLACE);
        flagRegistry.registerFlag(DefaultFlags.ENTITY_DAMAGE);

        regionRepository = new RegionRepository(this);
        regionManager = new RegionManagerImpl(this).init();

        getServer().getPluginManager().registerEvents(new DefaultFlagsListener(this), this);
        getServer().getPluginManager().registerEvents(new WandListener(this), this);

        CommandAPI commandAPI = new CommandAPI(this)
                .bind(Region.class, new RegionProvider(this))
                .bind(Flag.class, new FlagProvider(this))
                .bind(FlagAccess.class, new EnumProvider(FlagAccess.class))
                .beginCommandRegister()
                .register(new RegionCommand())
                .endRegister();

        new MenuAPI(this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
