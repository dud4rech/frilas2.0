package org.project.model;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.project.bean.HirerBean;

public class HirerModel {

    public static void create(HirerBean hirer, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("hirer");

        Document document = new Document()
                .append("hirername", hirer.getHirerName())
                .append("hirerphone", hirer.getHirerPhone())
                .append("hirermail", hirer.getHirerMail())
                .append("isactive", true);

        InsertOneResult result = col.insertOne(document);
        if (!result.wasAcknowledged()) {
            throw new RuntimeException("Failed to insert hirer.");
        }
    }
}