package org.project.model;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.project.bean.RatingBean;
import org.project.cli.actions.LoginAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class RatingModel {

    public static void create(RatingBean rating, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("rating");

        Document document = new Document()
                .append("ratingvalue", rating.getRatingValue())
                .append("ratingdescription", rating.getRatingDescription())
                .append("freelancerid", rating.getFreelancerId())
                .append("hirerid", rating.getHirerId());

        InsertOneResult result = col.insertOne(document);
        if (!result.wasAcknowledged()) {
            throw new RuntimeException("Failed to insert rating");
        }
    }

    public static boolean deleteById(ObjectId ratingId, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("rating");

        DeleteResult result = col.deleteOne(eq("_id", ratingId));
        return result.getDeletedCount() > 0;
    }

    public static boolean listAllByHirer(MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("rating");
        ObjectId hirerId = LoginAction.getLoggedUser();
        List<Document> ratings = col.find(eq("hirerid", hirerId)).sort(Sorts.ascending("_id")).into(new ArrayList<>());

        if (ratings.isEmpty()) {
            System.out.println("\nThere are no ratings created.");
            return false;
        }

        int index = 1;
        for (Document doc : ratings) {
            printRating(doc, index++);
        }
        return true;
    }

    public static boolean listAllByFreelancer(MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("rating");
        ObjectId freelancerId = LoginAction.getLoggedUser();
        List<Document> ratings = col.find(eq("freelancerid", freelancerId)).sort(Sorts.ascending("_id")).into(new ArrayList<>());

        if (ratings.isEmpty()) {
            System.out.println("\nThere are no ratings created.");
            return false;
        }

        int index = 1;
        for (Document doc : ratings) {
            printRating(doc, index++);
        }
        return true;
    }

    public static List<Document> listFreelancersWithoutRating(MongoDatabase database) {
        MongoCollection<Document> freelancerCollection = database.getCollection("freelancer");
        ObjectId hirerId = LoginAction.getLoggedUser();

        List<Document> pipeline = Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from", "proposal")
                        .append("localField", "_id")
                        .append("foreignField", "freelancerid")
                        .append("as", "proposals")
                ),
                new Document("$unwind", "$proposals"),
                new Document("$lookup", new Document()
                        .append("from", "project")
                        .append("localField", "proposals.projectid")
                        .append("foreignField", "_id")
                        .append("as", "project")
                ),
                new Document("$unwind", "$project"),
                new Document("$match", new Document("$and", Arrays.asList(
                        new Document("project.hirerid", hirerId),
                        new Document("proposals.proposalstatus", 2)
                ))),
                new Document("$lookup", new Document()
                        .append("from", "rating")
                        .append("let", new Document("freelancerId", "$_id"))
                        .append("pipeline", Arrays.asList(
                                new Document("$match", new Document("$expr", new Document("$and", Arrays.asList(
                                        new Document("$eq", Arrays.asList("$freelancerid", "$$freelancerId")),
                                        new Document("$eq", Arrays.asList("$hirerid", hirerId))
                                ))))
                        ))
                        .append("as", "ratings")
                ),
                new Document("$match", new Document("ratings", new Document("$size", 0))),
                new Document("$project", new Document()
                        .append("_id", 1)
                        .append("freelancername", 1)
                        .append("freelancerphone", 1)
                )
        );

        List<Document> results = freelancerCollection.aggregate(pipeline).into(new ArrayList<>());

        if (results.isEmpty()) {
            System.out.println("\nThere are no related freelancers.");
            return results;
        }

        int index = 1;
        for (Document doc : results) {
            System.out.println(index++ + ". Name: " + doc.getString("freelancername"));
            System.out.println("   Phone: " + doc.getString("freelancerphone"));
            System.out.println("------------------------");
        }

        return results;
    }

    public static List<Document> listRatingsByHirer(ObjectId hirerId, MongoDatabase db) {
        MongoCollection<Document> col = db.getCollection("rating");
        return col.find(eq("hirerid", hirerId)).into(new ArrayList<>());
    }

    public static List<Document> listAllFreelancers(MongoDatabase db) {
        MongoCollection<Document> col = db.getCollection("freelancer");
        return col.find().into(new ArrayList<>());
    }

    public static void printRating(Document doc, int index) {
        Integer ratingValue = doc.getInteger("ratingvalue");
        String ratingDescription = doc.getString("ratingdescription");

        System.out.println(index + ". Value: " + ratingValue);
        System.out.println("   Description: " + ratingDescription);
        System.out.println("------------------------");
    }
}