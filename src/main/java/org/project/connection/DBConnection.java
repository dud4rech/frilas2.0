package org.project.connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class DBConnection {
    private final String string = "mongodb://localhost:27017";
    private MongoClient mongoClient;

    public MongoClient getMongoClient() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(string);
        }
        return mongoClient;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
