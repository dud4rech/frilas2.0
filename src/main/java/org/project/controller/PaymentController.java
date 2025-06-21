package org.project.controller;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.project.bean.PaymentBean;
import org.project.cli.actions.LoginAction;
import org.project.model.PaymentModel;
import org.project.model.ProjectModel;

import java.sql.SQLException;

public class PaymentController {

    public static void createPayment(ObjectId freelancerId, int proposalValue, String projectDeadline, ObjectId projectId, MongoDatabase db) throws SQLException {
        ObjectId hirerId = LoginAction.getLoggedUser();

        PaymentBean payment = new PaymentBean(proposalValue, projectDeadline, projectId, freelancerId, hirerId);
        PaymentModel.create(payment, db);

        System.out.println("Payment created successfully!");
    }

    public static void makePayment(MongoDatabase db) throws SQLException {
        Document selectedPayment = PaymentModel.chooseAndMarkPaymentAsPaid(db);

        if (selectedPayment == null) {
            return;
        }
        System.out.println("\n=== Making a payment ===\n");

        System.out.println("\nPayment made successfully!");

        ObjectId projectId = selectedPayment.getObjectId("projectid");
        ProjectModel.finishProject(projectId, db);
    }
}
