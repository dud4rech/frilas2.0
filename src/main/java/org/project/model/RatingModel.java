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

    public static int delete(RatingBean rating, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("rating");

        DeleteResult result = col.deleteOne(
                Filters.eq("ratingid", rating.getRatingId())
        );

        return (int) result.getDeletedCount();
    }

    public static boolean listAllByHirer(MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("rating");
        ObjectId hirerId = LoginAction.getLoggedUser();
        boolean hasRatings = false;

        for (Document doc : col.find(Filters.eq("hirerid", hirerId))
                .sort(Sorts.ascending("_id"))) {
            hasRatings = true;
            printRating(doc);
        }

        if (!hasRatings) {
            System.out.println("\nThere are no ratings created.");
        }
        return hasRatings;
    }

    public static void listAllByFreelancer(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("rating");
        ObjectId freelancerId = LoginAction.getLoggedUser();
        boolean hasRatings = false;

        for (Document doc : collection.find(Filters.eq("freelancerid", freelancerId))
                .sort(Sorts.ascending("_id"))) {
            hasRatings = true;
            printRating(doc);
        }

        if (!hasRatings) {
            System.out.println("\nThere are no ratings created.");
        }
    }

    public static boolean listFreelancersInProjects(MongoDatabase database) {
        MongoCollection<Document> freelancerCollection = database.getCollection("freelancer");
        ObjectId hirerId = LoginAction.getLoggedUser();
        boolean hasRelatedFreelancer = false;

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

        if (!hasRelatedFreelancer) {
            System.out.println("\nThere are no related freelancers.");
        }
        return hasRelatedFreelancer;
    }

    public static void printRating(Document doc) {
        Integer ratingId = doc.getInteger("ratingid");
        Integer ratingValue = doc.getInteger("ratingvalue");
        String ratingDescription = doc.getString("ratingdescription");

        System.out.println("Rating ID: " + ratingId);
        System.out.println("Value: " + ratingValue);
        System.out.println("Description: " + ratingDescription);
        System.out.println("------------------------");
    }

    public static List<Document> listRatingsByHirer(ObjectId hirerId, MongoDatabase db) {
        MongoCollection<Document> col = db.getCollection("rating");

        return col.find(eq("hirerid", hirerId)).into(new ArrayList<>());
    }

    public static int deleteById(ObjectId ratingId, MongoDatabase db) {
        MongoCollection<Document> col = db.getCollection("rating");

        DeleteResult result = col.deleteOne(eq("_id", ratingId));
        return (int) result.getDeletedCount();
    }

    public static List<Document> listAllFreelancers(MongoDatabase db) {
        MongoCollection<Document> col = db.getCollection("freelancer");
        return col.find().into(new ArrayList<>());
    }

}