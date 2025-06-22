package org.project.controller;

import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.project.bean.ProposalBean;
import org.project.cli.actions.LoginAction;
import org.project.model.ProjectModel;
import org.project.model.ProposalModel;
import org.project.utils.Utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProposalController {

    public static void createProposal(MongoDatabase db) throws SQLException {
        System.out.println("\n=== Creating a new proposal ===\n");

        List<ObjectId> projectIds = ProjectModel.listProjectsForProposal(db);
        if (projectIds.isEmpty()) return;

        System.out.print("Choose a project by number: ");
        int choice = Utils.readInt();
        if (choice < 1 || choice > projectIds.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        ObjectId projectId = projectIds.get(choice - 1);

        System.out.print("Enter the proposal value: ");
        int proposalValue = Utils.readInt();

        System.out.print("Enter the proposal description: ");
        String proposalDescription = Utils.readString();

        ObjectId freelancerId = LoginAction.getLoggedUser();

        ProposalBean proposal = new ProposalBean(proposalValue, proposalDescription, freelancerId, projectId);
        ProposalModel.create(proposal, db);

        System.out.println("\nProposal created successfully!");
    }

    public static void updateProposal(MongoDatabase db) throws SQLException {
        System.out.println("\n=== Editing a proposal ===\n");

        List<ObjectId> proposalIds = ProposalModel.listUserProposals(db);
        if (proposalIds.isEmpty()) return;

        System.out.print("Choose a proposal by number: ");
        int choice = Utils.readInt();
        if (choice < 1 || choice > proposalIds.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        ObjectId proposalId = proposalIds.get(choice - 1);

        System.out.print("Enter the proposal value: ");
        int proposalValue = Utils.readInt();

        System.out.print("Enter the proposal description: ");
        String proposalDescription = Utils.readString();

        ProposalBean proposal = new ProposalBean(proposalId, proposalValue, proposalDescription);
        boolean proposalUpdated = ProposalModel.update(proposal, db);

        if (proposalUpdated) {
            System.out.println("\nProposal not edited or it doesn't exist.");
        } else {
            System.out.println("\nProposal edited successfully!");
        }
    }

    public static void deleteProposal(MongoDatabase db) throws SQLException {
        System.out.println("\n=== Deleting a proposal ===\n");

        List<ObjectId> proposalIds = ProposalModel.listUserProposals(db);
        if (proposalIds.isEmpty()) return;

        System.out.print("Choose a proposal by number: ");
        int choice = Utils.readInt();
        if (choice < 1 || choice > proposalIds.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        ObjectId proposalId = proposalIds.get(choice - 1);

        ProposalBean proposal = new ProposalBean(proposalId);
        boolean proposalDeleted = ProposalModel.delete(proposal, db);

        if (proposalDeleted) {
            System.out.println("\nProposal not deleted or it doesn't exist.");
        } else {
            System.out.println("\nProposal deleted successfully!");
        }
    }

    public static void acceptProposal(ObjectId projectId, MongoDatabase db) throws SQLException {
        System.out.println("\n=== Accepting a proposal ===\n");
        List<ObjectId> proposalIds = ProposalModel.listProposalsByProject(projectId, db);
        if (proposalIds.isEmpty()) {
            System.out.println("\nThere are no proposals for this project.");
            return;
        }

        System.out.print("Choose a proposal by number: ");
        int choice = Utils.readInt();
        if (choice < 1 || choice > proposalIds.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        ObjectId proposalId = proposalIds.get(choice - 1);

        ProposalBean proposal = new ProposalBean(proposalId);
        boolean proposalAccepted = ProposalModel.updateStatus(proposal, db);

        System.out.println("\nProposal accepted successfully!");

        if (proposalAccepted) {
            ProjectModel.updateStatus(projectId, db);

            Map<String, Object> details = ProposalModel.findProjectAndProposalDetails(proposalId, db);
            PaymentController.createPayment(
                    (ObjectId) details.get("freelancerid"),
                    (int) details.get("proposalvalue"),
                    (String) details.get("projectdeadline"),
                    projectId,
                    db);
        }
    }
}