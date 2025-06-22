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

    public static void createRating(MongoDatabase db) {
        System.out.println("=== Create a New Rating ===");

        List<Document> freelancers = RatingModel.listFreelancersWithoutRating(db);
        if (freelancers.isEmpty()) {
            return;
        }

        int choice = Utils.readInt();
        if (choice < 1 || choice > freelancers.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        ObjectId freelancerId = freelancers.get(choice - 1).getObjectId("_id");
        ObjectId hirerId = LoginAction.getLoggedUser();

        System.out.print("Enter the rating value (1-5): ");
        int ratingValue = Utils.readInt();

        System.out.print("Enter the rating description: ");
        String ratingDescription = Utils.readString();

        RatingBean rating = new RatingBean(ratingValue, ratingDescription, freelancerId, hirerId);
        RatingModel.create(rating, db);

        System.out.println("\nRating created successfully!");
    }

    public static void deleteRating(MongoDatabase db) throws SQLException {
        System.out.println("=== Delete a Rating ===");

        ObjectId hirerId = LoginAction.getLoggedUser();
        List<Document> ratings = RatingModel.listRatingsByHirer(hirerId, db);

        if (ratings.isEmpty()) {
            System.out.println("\nYou have no ratings to delete.");
            return;
        }

        int index = 1;
        for (Document rating : ratings) {
            RatingModel.printRating(rating, index++);
        }

        System.out.print("\nChoose the number of the rating to delete: ");
        int choice = Utils.readInt();

        if (choice < 1 || choice > ratings.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        ObjectId ratingId = ratings.get(choice - 1).getObjectId("_id");

        boolean deleted = RatingModel.deleteById(ratingId, db);

        if (deleted) {
            System.out.println("\nRating deleted successfully!");
        } else {
            System.out.println("\nRating could not be deleted or does not exist.");
        }
    }
}