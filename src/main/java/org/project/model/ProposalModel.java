package org.project.model;

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

    public static int update(ProposalBean proposal, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("proposal");

        UpdateResult result = col.updateOne(
                Filters.and(
                        Filters.eq("_id", proposal.getProposalId()),
                        Filters.ne("proposalstatus", ProposalStatus.ACCEPTED.getValue())
                ),
                Updates.combine(
                        Updates.set("proposalvalue", proposal.getProposalValue()),
                        Updates.set("proposaldescription", proposal.getProposalDescription())
                )
        );

        return (int) result.getModifiedCount();
    }

    public static int delete(ProposalBean proposal, MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("proposal");

        DeleteResult result = collection.deleteOne(
                Filters.and(
                        Filters.eq("_id", proposal.getProposalId()),
                        Filters.ne("proposalstatus", ProposalStatus.ACCEPTED.getValue())
                )
        );

        return (int) result.getDeletedCount();
    }

    public static int updateStatus(ProposalBean proposal, MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("proposal");

        UpdateResult result = collection.updateOne(
                Filters.eq("_id", proposal.getProposalId()),
                Updates.set("proposalstatus", ProposalStatus.ACCEPTED.getValue())
        );

        return (int) result.getModifiedCount();
    }

    public static Map<String, Object> findProjectAndProposalDetails(ObjectId proposalId, MongoDatabase database) {
        MongoCollection<Document> proposalCollection = database.getCollection("proposal");
        MongoCollection<Document> projectCollection = database.getCollection("project");

        Document proposal = proposalCollection.find(Filters.eq("_id", proposalId)).first();
        if (proposal == null) {
            System.out.println("\nProposal not found!\n");
            return null;
        }

        ObjectId projectId = proposal.getObjectId("projectid");
        Document project = projectCollection.find(Filters.eq("_id", projectId)).first();
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
        MongoCollection<Document> collection = database.getCollection("proposal");
        ObjectId freelancerId = LoginAction.getLoggedUser();
        List<ObjectId> ids = new ArrayList<>();

        List<Document> proposals = collection.find(Filters.eq("freelancerid", freelancerId))
                .sort(new Document("_id", 1)).into(new ArrayList<>());

        for (int i = 0; i < proposals.size(); i++) {
            Document doc = proposals.get(i);
            ids.add(doc.getObjectId("_id"));
            printProposalWithIndex(doc, i + 1);
        }

        if (ids.isEmpty()) {
            System.out.println("\nThere are no proposals available.");
        }
        return ids;
    }

    public static void listHirerProposals(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("proposal");
        ObjectId hirerId = LoginAction.getLoggedUser();
        List<ObjectId> ids = new ArrayList<>();

        List<Document> proposals = collection.find(Filters.eq("hirerid", hirerId))
                .sort(new Document("_id", 1)).into(new ArrayList<>());

        for (int i = 0; i < proposals.size(); i++) {
            Document doc = proposals.get(i);
            ids.add(doc.getObjectId("_id"));
            printProposalWithIndex(doc, i + 1);
        }

        if (ids.isEmpty()) {
            System.out.println("\nThere are no proposals available.");
        }
    }

    public static List<ObjectId> listProposalsByProject(ObjectId projectId, MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("proposal");
        List<ObjectId> ids = new ArrayList<>();

        List<Document> proposals = collection.find(
                Filters.and(
                            eq("projectid", projectId),
                            eq("proposalstatus", ProposalStatus.CREATED.getValue())
                        ))
                .sort(new Document("_id", 1)).into(new ArrayList<>());

        for (int i = 0; i < proposals.size(); i++) {
            Document doc = proposals.get(i);
            ids.add(doc.getObjectId("_id"));
            printProposalWithIndex(doc, i + 1);
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
