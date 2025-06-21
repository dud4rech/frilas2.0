package org.project.cli.actions;

import com.mongodb.client.MongoDatabase;
import org.project.enums.UserType;
import org.project.model.PaymentModel;
import org.project.model.ProposalModel;
import org.project.model.RatingModel;
import org.project.utils.Utils;

import java.sql.SQLException;

public class ReportAction {

    public static void execute(int user, MongoDatabase db) throws SQLException {

        System.out.println("\nWhich report would you like to generate?");

        if (UserType.HIRER.getValue() == user) {
            showHirerMenu(db);
        } else {
            showFreelancerMenu(db);
        }
    }

    private static void showFreelancerMenu(MongoDatabase db) throws SQLException {
        System.out.println("\n1 - Ratings report");
        System.out.println("2 - Back\n");

        int command;

        do {
            command = Utils.readInt();

            switch (command) {
                case 1:
                    System.out.println(" --- RATINGS REPORT ---");
                    RatingModel.listAllByFreelancer(db);
                    return;
                case 2:
                    FreelancerAction.execute(db);
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (command != 0);
    }

    private static void showHirerMenu(MongoDatabase db) throws SQLException {
        System.out.println("\n1 - Proposals report");
        System.out.println("2 - Payments report");
        System.out.println("3 - Back\n");

        int command;
        do {
            command = Utils.readInt();

            switch (command) {
                case 1:
                    System.out.println(" --- PROPOSALS REPORT ---");
                    ProposalModel.listHirerProposals(db);
                    return;
                case 2:
                    System.out.println(" --- PAYMENTS REPORT ---");
                    PaymentModel.listAllByUser(db);
                    return;
                case 3:
                    HirerAction.execute(db);
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (command != 0);
    }
}
