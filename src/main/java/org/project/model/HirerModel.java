package org.project.model;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
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

    public static void delete(HirerBean hirer, MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("hirer");

        UpdateResult result = collection.updateOne(
                Filters.eq("hirerid", hirer.getHirerId()),
                Updates.set("isactive", false)
        );

        if (result.getModifiedCount() == 0) {
            throw new RuntimeException("No hirer found with ID " + hirer.getHirerId());
        }
    }
}