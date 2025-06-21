package org.project.cli.actions;

import com.mongodb.client.MongoDatabase;
import org.project.cli.ActionLineInterface;
import org.project.controller.FreelancerController;
import org.project.controller.HirerController;
import org.project.utils.Utils;

import java.sql.SQLException;

public class RegisterAction {

    public static void execute(MongoDatabase db) throws SQLException {
        int command;

        do {
            System.out.println("Are you a Freelancer (1) or a Hirer (2)?");

            command = Utils.readInt();

            switch (command) {
                case 1:
                    FreelancerController.createFreelancer(db);
                    return;
                case 2:
                    HirerController.createHirer(db);
                    return;
                case 3:
                    ActionLineInterface.execute(db);
                    return;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        } while (command != 5);
    }
}
