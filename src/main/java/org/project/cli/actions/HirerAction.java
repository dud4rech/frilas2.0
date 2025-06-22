package org.project.cli.actions;

import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.project.cli.ActionLineInterface;
import org.project.controller.PaymentController;
import org.project.controller.ProjectController;

import org.project.controller.ProposalController;
import org.project.enums.UserType;
import org.project.model.ProjectModel;
import org.project.utils.Utils;

import java.sql.SQLException;

public class HirerAction {

    public static void execute(MongoDatabase db) throws SQLException {
        int command;

        do {
            System.out.println("\n1 - Create project");
            System.out.println("2 - Edit project");
            System.out.println("3 - Delete project");
            System.out.println("4 - Choose proposal");
            System.out.println("5 - Make a payment");
            System.out.println("6 - Rating");
            System.out.println("7 - Reports");
            System.out.println("8 - Back to main menu");

            command = Utils.readInt();

            switch (command) {
                case 1:
                    ProjectController.createProject(db);
                    break;
                case 2:
                    ProjectController.updateProject(db);
                    break;
                case 3:
                    ProjectController.deleteProject(db);
                    break;
                case 4:
                    ObjectId projectId = ProjectModel.selectNotStartedProjectByHirer(db);
                    if (projectId != null) {
                        ProposalController.acceptProposal(projectId, db);
                    }
                    break;
                case 5:
                    PaymentController.makePayment(db);
                    break;
                case 6:
                    RatingAction.execute(db);
                    break;
                case 7:
                    ReportAction.execute(UserType.HIRER.getValue(), db);
                    break;
                case 8:
                    ActionLineInterface.execute(db);
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (command != 0);
    }
}
