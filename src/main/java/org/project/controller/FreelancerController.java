package org.project.controller;

import com.mongodb.client.MongoDatabase;
import org.project.bean.FreelancerBean;
import org.project.model.FreelancerModel;
import org.project.utils.Utils;

import java.sql.SQLException;

public class FreelancerController {

    public static void createFreelancer(MongoDatabase db) throws SQLException {
        System.out.println("=== Creating a new freelancer ===");

        System.out.print("Enter the freelancer’s name: ");
        String freelancerName = Utils.readString();

        System.out.print("Enter the freelancer’s phone number: ");
        String freelancerPhone = Utils.readString();

        System.out.print("Enter the freelancer’s email: ");
        String freelancerMail = Utils.readString();

        FreelancerBean freelancer = new FreelancerBean(freelancerName, freelancerPhone, freelancerMail);

        try {
            FreelancerModel.create(freelancer, db);
            System.out.println("\nNew account created successfully!\n");
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.out.println("\nThe email provided is already in use. Try another one.\n");
            } else {
                System.out.println("\nError: " + e.getMessage() + "\n");
                throw e;
            }
        }
    }
}
