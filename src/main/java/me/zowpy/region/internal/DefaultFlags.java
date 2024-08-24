package me.zowpy.region.internal;

import me.zowpy.region.api.Flag;

public class DefaultFlags {

    public static final Flag BLOCK_BREAK = createFlag("block_break", "Block Break");
    public static final Flag BLOCK_PLACE = createFlag("block_place", "Block Place");
    public static final Flag INTERACT = createFlag("interact", "Interact");
    public static final Flag ENTITY_DAMAGE = createFlag("entity_damage", "Entity Damage");

    private static Flag createFlag(String name, String display) {
        return new Flag() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDisplay() {
                return display;
            }
        };
    }
}
