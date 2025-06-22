package org.project.controller;


import com.mongodb.client.MongoDatabase;
import org.project.bean.HirerBean;
import org.project.model.HirerModel;
import org.project.utils.Utils;

import java.sql.SQLException;

public class HirerController {

    public static void createHirer(MongoDatabase db) {
        System.out.println("=== Creating a new hirer ===");

        System.out.print("Enter the hirer’s name: ");
        String hirerName = Utils.readString();

        System.out.print("Enter the hirer’s phone number: ");
        String hirerPhone = Utils.readString();

        System.out.print("Enter the hirer’s email: ");
        String hirerMail = Utils.readString();

        HirerBean hirer = new HirerBean(hirerName, hirerPhone, hirerMail);
        HirerModel.create(hirer, db);
        System.out.println("\nNew account created successfully!\n");
    }
}
