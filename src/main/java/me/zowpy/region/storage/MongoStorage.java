package me.zowpy.region.storage;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

@Getter
public class MongoStorage {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    private final MongoCollection<Document> regions;

    public MongoStorage(MongoCredentials credentials) {
        if (credentials.uri()) {
            mongoClient = new MongoClient(new MongoClientURI(credentials.connectionString()));
        }else {
            if (credentials.auth()) {
                mongoClient = new MongoClient(
                        new ServerAddress(credentials.host(), credentials.port()),
                        MongoCredential.createCredential(
                                credentials.user(),
                                credentials.database(),
                                credentials.password().toCharArray()
                        ),
                        MongoClientOptions.builder().build()
                );
            }else {
                mongoClient = new MongoClient(
                        new ServerAddress(credentials.host(), credentials.port())
                );
            }
        }

        mongoDatabase = mongoClient.getDatabase(credentials.database());
        regions = mongoDatabase.getCollection("regions");
    }
}
