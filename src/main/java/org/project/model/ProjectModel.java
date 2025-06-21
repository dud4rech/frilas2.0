package org.project.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.project.bean.ProjectBean;
import org.project.cli.actions.LoginAction;
import org.project.enums.ProjectStatus;
import org.project.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ProjectModel {

    public static void create(ProjectBean project, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("project");

        Document document = new Document()
                .append("projectname", project.getProjectName())
                .append("projectdescription", project.getProjectDescription())
                .append("projectdeadline", project.getProjectDeadline())
                .append("projectbudget", project.getProjectBudget())
                .append("projectstatus", ProjectStatus.START.getValue())
                .append("hirerid", project.getHirerId());

        InsertOneResult result = col.insertOne(document);
        if (!result.wasAcknowledged()) {
            throw new RuntimeException("Failed to insert project");
        }
    }

    public static int update(ProjectBean project, MongoDatabase database, ObjectId id) {
        MongoCollection<Document> col = database.getCollection("project");

        UpdateResult result = col.updateOne(
                Filters.and(
                        eq("_id", id),
                        Filters.nin("projectstatus", ProjectStatus.ONGOING.getValue(), ProjectStatus.FINISHED.getValue())
                ),
                Updates.combine(
                        Updates.set("projectname", project.getProjectName()),
                        Updates.set("projectdescription", project.getProjectDescription()),
                        Updates.set("projectdeadline", project.getProjectDeadline()),
                        Updates.set("projectbudget", project.getProjectBudget())
                )
        );

        return (int) result.getModifiedCount();
    }

    public static int delete(ObjectId id, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("project");

        DeleteResult result = col.deleteOne(
                Filters.and(
                        eq("_id", id),
                        Filters.nin("projectstatus", ProjectStatus.ONGOING.getValue(), ProjectStatus.FINISHED.getValue())
                )
        );

        return (int) result.getDeletedCount();
    }

    public static int updateStatus(ObjectId projectId, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("project");

        UpdateResult result = col.updateOne(
                eq("_id", projectId),
                Updates.set("projectstatus", ProjectStatus.ONGOING.getValue())
        );

        return (int) result.getModifiedCount();
    }

    public static int finishProject(ObjectId projectId, MongoDatabase database) {
        MongoCollection<Document> col = database.getCollection("project");

        UpdateResult result = col.updateOne(
                eq("_id", projectId),
                Updates.set("projectstatus", ProjectStatus.FINISHED.getValue())
        );

        return (int) result.getModifiedCount();
    }

    public static boolean listAllByUserWithSelection(MongoDatabase db, List<ObjectId> projectIds) {
        MongoCollection<Document> col = db.getCollection("project");
        ObjectId hirerId = LoginAction.getLoggedUser();
        boolean hasProjects = false;
        int index = 1;

        for (Document doc : col.find(
                Filters.and(
                        eq("hirerid", hirerId),
                        Filters.nin("projectstatus", ProjectStatus.FINISHED.getValue(), ProjectStatus.ONGOING.getValue())
                )
        ).sort(Sorts.ascending("_id"))) {
            hasProjects = true;
            projectIds.add(doc.getObjectId("_id"));
            System.out.println("[" + (index++) + "] " + doc.getString("projectname"));
        }

        if (!hasProjects) {
            System.out.println("\nThere are no projects available.");
        }

        return hasProjects;
    }

    public static ObjectId chooseProjectFromList(List<ObjectId> projectIds) {
        System.out.print("Choose a project by number: ");
        int choice = Utils.readInt();

        if (choice < 1 || choice > projectIds.size()) {
            System.out.println("Invalid choice.");
            return null;
        }

        return projectIds.get(choice - 1);
    }

    public static List<ObjectId> listProjectsForProposal(MongoDatabase database) {
        MongoCollection<Document> projectCollection = database.getCollection("project");
        List<ObjectId> projectIds = new ArrayList<>();

        for (Document doc : projectCollection.find(
                Filters.and(
                        Filters.eq("projectstatus", ProjectStatus.START.getValue())
                )
        ).sort(Sorts.ascending("_id")).into(new ArrayList<>())) {

            projectIds.add(doc.getObjectId("_id"));
            printProject(doc, projectIds.size());
        }

        if (projectIds.isEmpty()) {
            System.out.println("\nThere are no projects available to create proposals.");
        }

        return projectIds;
    }

    public static ObjectId selectProjectByHirer(MongoDatabase db) {
        MongoCollection<Document> collection = db.getCollection("project");
        ObjectId hirerId = LoginAction.getLoggedUser();

        List<Document> projects = collection.find(eq("hirerid", hirerId)).into(new ArrayList<>());

        if (projects.isEmpty()) {
            System.out.println("You have no projects.");
            return null;
        }

        System.out.println("Select a project:");
        for (int i = 0; i < projects.size(); i++) {
            Document doc = projects.get(i);
            System.out.println((i + 1) + " - " + doc.getString("projectname"));
        }

        int option = Utils.readInt();

        if (option < 1 || option > projects.size()) {
            System.out.println("Invalid selection.");
            return null;
        }

        return projects.get(option - 1).getObjectId("_id");
    }

    private static void printProject(Document doc, Integer index) {
        if (index != null) {
            System.out.println(index + ". Project Name: " + doc.getString("projectname"));
        } else {
            System.out.println("Project Name: " + doc.getString("projectname"));
        }
        System.out.println("   Description: " + doc.getString("projectdescription"));
        System.out.println("   Deadline: " + doc.getString("projectdeadline"));
        System.out.println("   Budget: " + doc.getInteger("projectbudget"));
        System.out.println("------------------------");
    }
}