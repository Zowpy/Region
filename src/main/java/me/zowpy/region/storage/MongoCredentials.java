package me.zowpy.region.storage;

import lombok.Data;

public record MongoCredentials(String host, int port, String database, boolean auth, String user, String password,
                               boolean uri, String connectionString) {}
