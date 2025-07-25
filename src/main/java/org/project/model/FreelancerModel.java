package org.project.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.project.bean.FreelancerBean;

import java.sql.SQLException;

public class FreelancerModel {

    public static void create(FreelancerBean freelancer, MongoDatabase db) throws SQLException {
        MongoCollection<Document> col = db.getCollection("freelancer");

        Document doc = new Document()
                .append("freelancername", freelancer.getFreelancerName())
                .append("freelancermail", freelancer.getFreelancerMail())
                .append("freelancerphone", freelancer.getFreelancerPhone())
                .append("isActive", true);

        col.insertOne(doc);
    }
}
