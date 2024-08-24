package me.zowpy.region.registry;

import me.zowpy.region.api.Flag;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlagRegistry {

    private final Map<String, Flag> registeredFlags = new HashMap<>();

    public void registerFlag(Flag flag) {
        if (registeredFlags.putIfAbsent(flag.getName().toLowerCase(), flag) != null) {
            throw new RuntimeException("Flag with the name '" + flag.getName().toLowerCase() + "' already exists!");
        }
    }

    public Flag getFlag(String name) {
        return registeredFlags.get(name.toLowerCase());
    }

    public Collection<Flag> getFlags() {
        return registeredFlags.values();
    }
}
