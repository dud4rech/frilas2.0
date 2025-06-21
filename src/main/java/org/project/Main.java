package org.project;

import com.mongodb.client.MongoClient;
import org.project.cli.ActionLineInterface;
import org.project.connection.DBConnection;

public class Main {
    public static void main(String[] args) {
        DBConnection connection = new DBConnection();
        MongoClient mongoClient = connection.getMongoClient();

        try {
            ActionLineInterface.execute(mongoClient.getDatabase("frilas"));
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        } finally {
            mongoClient.close();
        }
    }
}