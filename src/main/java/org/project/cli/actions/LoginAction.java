package org.project.cli.actions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.project.utils.Utils;

import java.sql.SQLException;

public class LoginAction {
    private static ObjectId loggedUser;

    public static void execute(MongoDatabase db) throws SQLException {
        System.out.println("Are you a Freelancer (1) or a Hirer (2)?");
        int command = Utils.readInt();

        System.out.print("E-mail: ");
        String email = Utils.readString();

        if (command == 1) {
            if (authenticateFreelancer(email, db)) {
                System.out.println("Login was successful!");
                FreelancerAction.execute(db);
            }
        } else if (command == 2) {
            if (authenticateHirer(email, db)) {
                System.out.println("Login was successful!");
                HirerAction.execute(db);
            }
        }
        System.out.println("Login failed\n");
    }

    private static boolean authenticateFreelancer(String email, MongoDatabase db) {
        MongoCollection<Document> collection = db.getCollection("freelancer");

        Document freelancer = collection.find(Filters.eq("freelancermail", email)).first();
        if (freelancer != null) {
            loggedUser = freelancer.getObjectId("_id");
            return true;
        }
        return false;
    }

    private static boolean authenticateHirer(String email, MongoDatabase db) {
        MongoCollection<Document> collection = db.getCollection("hirer");

        Document hirer = collection.find(Filters.eq("hirermail", email)).first();
        if (hirer != null) {
            loggedUser = hirer.getObjectId("_id");
            return true;
        }
        return false;
    }

    public static ObjectId getLoggedUser() {
        return loggedUser;
    }
}
