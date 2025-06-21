package org.project.model;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.project.bean.PaymentBean;
import org.project.cli.actions.LoginAction;
import org.project.enums.PaymentStatus;
import org.project.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PaymentModel {

    public static void create(PaymentBean payment, MongoDatabase db) {
        MongoCollection<Document> col = db.getCollection("payment");

        Document document = new Document()
                .append("paymentvalue", payment.getPaymentValue())
                .append("paymentdate", payment.getPaymentDate())
                .append("freelancerid", payment.getFreelancerId())
                .append("hirerid", payment.getHirerid())
                .append("paymentstatus", PaymentStatus.PENDING.getValue());

        InsertOneResult result = col.insertOne(document);
        if (!result.wasAcknowledged()) {
            throw new RuntimeException("Failed to insert payment");
        }
    }

    public static boolean listAllByUser(MongoDatabase db) {
        MongoCollection<Document> col = db.getCollection("payment");
        ObjectId hirerId = LoginAction.getLoggedUser();
        boolean hasPayments = false;
        int index = 1;

        for (Document doc : col.find(eq("hirerid", hirerId))
                .sort(Sorts.ascending("paymentdate"))) {
            hasPayments = true;
            System.out.println("[" + index++ + "]");
            printPayment(doc);
        }

        if (!hasPayments) {
            System.out.println("\nThere are no payments.");
        }
        return hasPayments;
    }

    private static void printPayment(Document doc) {
        ObjectId paymentId = doc.getObjectId("_id");
        Integer paymentValue = doc.getInteger("paymentvalue");
        String paymentDate = doc.getString("paymentdate");
        Integer paymentStatus = doc.getInteger("paymentstatus");

        String statusDescription = PaymentStatus.fromValue(paymentStatus).getDescription();

        System.out.println("Value: " + paymentValue);
        System.out.println("Date: " + paymentDate);
        System.out.println("Status: " + statusDescription);
        System.out.println("------------------------");
    }

    public static Document chooseAndMarkPaymentAsPaid(MongoDatabase db) {
        MongoCollection<Document> col = db.getCollection("payment");
        ObjectId hirerId = LoginAction.getLoggedUser();

        List<Document> pendingPayments = col.find(
                Filters.and(
                        eq("hirerid", hirerId),
                        eq("paymentstatus", PaymentStatus.PENDING.getValue())
                )
        ).into(new ArrayList<>());

        if (pendingPayments.isEmpty()) {
            System.out.println("No pending payments found.");
            return null;
        }

        int index = 1;
        for (Document doc : pendingPayments) {
            System.out.println("[" + index++ + "]");
            PaymentModel.printPayment(doc);
        }

        System.out.print("Choose a payment by number: ");
        int choice = Utils.readInt();

        if (choice < 1 || choice > pendingPayments.size()) {
            System.out.println("Invalid choice.");
            return null;
        }

        Document selected = pendingPayments.get(choice - 1);

        col.updateOne(
                eq("_id", selected.getObjectId("_id")),
                Updates.set("paymentstatus", PaymentStatus.PAYED.getValue())
        );

        return selected;
    }
}
