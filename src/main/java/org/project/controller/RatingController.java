package org.project.controller;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.project.bean.RatingBean;
import org.project.cli.actions.LoginAction;
import org.project.model.RatingModel;
import org.project.utils.Utils;

import java.sql.SQLException;
import java.util.List;

public class RatingController {

    public static void createRating(MongoDatabase db) throws SQLException {
        System.out.println("=== Creating a new rating ===");

        List<Document> freelancers = RatingModel.listAllFreelancers(db);

        if (freelancers.isEmpty()) {
            System.out.println("There are no freelancers to rate.");
            return;
        }

        for (int i = 0; i < freelancers.size(); i++) {
            Document f = freelancers.get(i);
            System.out.println("[" + (i + 1) + "] Name: " + f.getString("freelancername"));
        }

        System.out.print("Choose a freelancer to rate: ");
        int choice = Utils.readInt();

        if (choice < 1 || choice > freelancers.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        ObjectId freelancerId = freelancers.get(choice - 1).getObjectId("_id");

        System.out.print("Enter the rating value (1-5): ");
        int ratingValue = Utils.readInt();

        System.out.print("Enter the rating description: ");
        String ratingDescription = Utils.readString();

        ObjectId hirerId = LoginAction.getLoggedUser();

        RatingBean rating = new RatingBean(ratingValue, ratingDescription, freelancerId, hirerId);
        RatingModel.create(rating, db);

        System.out.println("\nRating created successfully!");
    }

    public static void deleteRating(MongoDatabase db) throws SQLException {
        System.out.println("=== Deleting a rating ===");

        ObjectId hirerId = LoginAction.getLoggedUser();

        List<Document> ratings = RatingModel.listRatingsByHirer(hirerId, db);

        if (ratings.isEmpty()) {
            System.out.println("\nYou have no ratings to delete.");
            return;
        }

        for (int i = 0; i < ratings.size(); i++) {
            Document doc = ratings.get(i);
            System.out.println("[" + (i + 1) + "]");
            RatingModel.printRating(doc);
        }

        System.out.print("\nChoose the number of the rating to delete: ");
        int choice = Utils.readInt();

        if (choice < 1 || choice > ratings.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Document selected = ratings.get(choice - 1);
        ObjectId ratingId = selected.getObjectId("_id");

        int deleted = RatingModel.deleteById(ratingId, db);

        if (deleted == 0) {
            System.out.println("\nRating not deleted or doesn't exist.");
        } else {
            System.out.println("\nRating deleted successfully!");
        }
    }
}