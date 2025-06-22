package org.project.cli.actions;

import com.mongodb.client.MongoDatabase;
import org.project.cli.ActionLineInterface;
import org.project.controller.RatingController;
import org.project.model.RatingModel;
import org.project.utils.Utils;

import java.sql.SQLException;

public class RatingAction {

    public static void execute(MongoDatabase db) throws SQLException {
        int command;

        do {
            System.out.println("1 - Create rating");
            System.out.println("2 - Delete rating");
            System.out.println("3 - Back");

            command = Utils.readInt();

            switch (command) {
                case 1:
                    RatingController.createRating(db);
                    return;
                case 2:
                    RatingController.deleteRating(db);
                    return;
                case 3:
                    ActionLineInterface.execute(db);
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (command != 0);
    }
}
