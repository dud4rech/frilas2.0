package org.project.cli.actions;

import com.mongodb.client.MongoDatabase;
import org.project.cli.ActionLineInterface;
import org.project.controller.ProposalController;
import org.project.enums.UserType;
import org.project.utils.Utils;

import java.sql.SQLException;

public class FreelancerAction {

    public static void execute(MongoDatabase db) throws SQLException {
        int command;

        do {
            System.out.println("\n1 - Make proposal");
            System.out.println("2 - Edit proposal");
            System.out.println("3 - Delete proposal");
            System.out.println("4 - Reports");
            System.out.println("5 - Back");

            command = Utils.readInt();

            switch (command) {
                case 1:
                    ProposalController.createProposal(db);
                    break;
                case 2:
                    ProposalController.updateProposal(db);
                    break;
                case 3:
                    ProposalController.deleteProposal(db);
                    break;
                case 4:
                    ReportAction.execute(UserType.FREELANCER.getValue(), db);
                    break;
                case 5:
                    ActionLineInterface.execute(db);
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (command != 0);
    }
}
