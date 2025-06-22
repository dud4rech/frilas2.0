package org.project.model;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.project.bean.ProposalBean;
import org.project.cli.actions.LoginAction;
import org.project.enums.ProposalStatus;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class ProposalModel {

    public static void create(ProposalBean proposal, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("proposal");

        Document document = new Document()
                .append("proposalvalue", proposal.getProposalValue())
                .append("proposaldescription", proposal.getProposalDescription())
                .append("proposalstatus", ProposalStatus.CREATED.getValue())
                .append("freelancerid", proposal.getFreelancerId())
                .append("projectid", proposal.getProjectId());

        InsertOneResult result = col.insertOne(document);
        if (!result.wasAcknowledged()) {
            throw new RuntimeException("Failed to insert proposal");
        }
    }

    public static boolean update(ProposalBean proposal, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("proposal");

        UpdateResult result = col.updateOne(
                Filters.and(
                        eq("_id", proposal.getProposalId()),
                        Filters.ne("proposalstatus", ProposalStatus.ACCEPTED.getValue())
                ),
                Updates.combine(
                        Updates.set("proposalvalue", proposal.getProposalValue()),
                        Updates.set("proposaldescription", proposal.getProposalDescription())
                )
        );

        return result.getModifiedCount() > 0;
    }

    public static boolean delete(ProposalBean proposal, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("proposal");

        DeleteResult result = col.deleteOne(
                Filters.and(
                        eq("_id", proposal.getProposalId()),
                        Filters.ne("proposalstatus", ProposalStatus.ACCEPTED.getValue())
                )
        );

        return result.getDeletedCount() > 0;
    }

    public static boolean updateStatus(ProposalBean proposal, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("proposal");

        UpdateResult result = col.updateOne(
                eq("_id", proposal.getProposalId()),
                Updates.set("proposalstatus", ProposalStatus.ACCEPTED.getValue())
        );

        return result.getModifiedCount() > 0;
    }

    public static Map<String, Object> findProjectAndProposalDetails(ObjectId proposalId, MongoDatabase database) {
        MongoCollection<Document> proposalCol = database.getCollection("proposal");
        MongoCollection<Document> projectCol = database.getCollection("project");

        Document proposal = proposalCol.find(eq("_id", proposalId)).first();
        if (proposal == null) {
            System.out.println("\nProposal not found!\n");
            return null;
        }

        ObjectId projectId = proposal.getObjectId("projectid");
        Document project = projectCol.find(eq("_id", projectId)).first();
        if (project == null) {
            System.out.println("\nProject not found!\n");
            return null;
        }

        Map<String, Object> details = new HashMap<>();
        details.put("freelancerid", proposal.get("freelancerid"));
        details.put("proposalvalue", proposal.get("proposalvalue"));
        details.put("projectdeadline", project.get("projectdeadline"));
        details.put("projectid", projectId);

        return details;
    }

    public static List<ObjectId> listUserProposals(MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("proposal");
        ObjectId freelancerId = LoginAction.getLoggedUser();
        List<ObjectId> ids = new ArrayList<>();

        List<Document> proposals = col.find(
                Filters.and(
                        eq("freelancerid", freelancerId),
                        eq("proposalstatus", ProposalStatus.CREATED.getValue())
                )
        ).sort(new Document("_id", 1)).into(new ArrayList<>());

        int index = 1;
        for (Document doc : proposals) {
            ids.add(doc.getObjectId("_id"));
            printProposalWithIndex(doc, index++);
        }

        if (ids.isEmpty()) {
            System.out.println("\nThere are no proposals available.");
        }

        return ids;
    }

    public static void listHirerProposals(MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("proposal");
        ObjectId hirerId = LoginAction.getLoggedUser();

        List<Document> pipeline = Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from", "project")
                        .append("localField", "projectid")
                        .append("foreignField", "_id")
                        .append("as", "project")
                ),
                new Document("$unwind", "$project"),
                new Document("$match", new Document("project.hirerid", hirerId)),
                new Document("$sort", new Document("_id", 1))
        );

        AggregateIterable<Document> results = col.aggregate(pipeline);

        List<ObjectId> ids = new ArrayList<>();
        int index = 1;

        for (Document doc : results) {
            ids.add(doc.getObjectId("_id"));
            printProposalWithIndex(doc, index++);
        }

        if (ids.isEmpty()) {
            System.out.println("\nThere are no proposals available.");
        }
    }

    public static List<ObjectId> listProposalsByProject(ObjectId projectId, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("proposal");
        List<ObjectId> ids = new ArrayList<>();

        List<Document> proposals = col.find(
                Filters.and(
                        eq("projectid", projectId),
                        eq("proposalstatus", ProposalStatus.CREATED.getValue())
                )
        ).sort(new Document("_id", 1)).into(new ArrayList<>());

        int index = 1;
        for (Document doc : proposals) {
            ids.add(doc.getObjectId("_id"));
            printProposalWithIndex(doc, index++);
        }

        return ids;
    }

    private static void printProposalWithIndex(Document doc, int index) {
        Integer proposalValue = doc.getInteger("proposalvalue");
        String proposalDescription = doc.getString("proposaldescription");
        Integer proposalStatus = doc.getInteger("proposalstatus");

        String statusDescription = ProposalStatus.fromValue(proposalStatus).getDescription();

        System.out.println(index + ". Proposal Value: " + proposalValue);
        System.out.println("   Description: " + proposalDescription);
        System.out.println("   Status: " + statusDescription);
        System.out.println("------------------------");
    }
}