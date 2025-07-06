package org.project.controller;

import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.project.bean.ProjectBean;
import org.project.cli.actions.LoginAction;
import org.project.model.ProjectModel;
import org.project.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ProjectController {

    public static void createProject(MongoDatabase db) {
        System.out.println("=== Creating a new project ===");

        System.out.print("Enter the project name: ");
        String projectName = Utils.readString();

        System.out.print("Enter the project description: ");
        String projectDescription = Utils.readString();

        System.out.print("Enter the project deadline (format YYYY-MM-DD, e.g., 2025-12-31): ");
        String projectDeadline = Utils.readString();

        System.out.print("Enter the project budget (as a whole number): ");
        int projectBudget = Utils.readInt();

        ObjectId hirerId = LoginAction.getLoggedUser();

        ProjectBean project = new ProjectBean(projectName, projectDescription, projectDeadline, projectBudget, hirerId);
        ProjectModel.create(project, db);

        System.out.println("\nProject created successfully!");
    }

    public static void updateProject(MongoDatabase db) {
        List<ObjectId> projectIds = new ArrayList<>();
        boolean hasProjects = ProjectModel.listAllByUserWithSelection(db, projectIds);

        if (!hasProjects) return;

        System.out.println("=== Editing a project ===");

        ObjectId chosenId = ProjectModel.chooseProjectFromList(projectIds);
        if (chosenId == null) return;

        System.out.print("Enter the new project name: ");
        String projectName = Utils.readString();

        System.out.print("Enter the new project description: ");
        String projectDescription = Utils.readString();

        System.out.print("Enter the new project deadline (format YYYY-MM-DD, e.g., 2025-12-31): ");
        String projectDeadline = Utils.readString();

        System.out.print("Enter the new project budget (as a whole number): ");
        int projectBudget = Utils.readInt();

        ProjectBean project = new ProjectBean(projectName, projectDescription, projectDeadline, projectBudget);
        boolean projectUpdated = ProjectModel.update(project, db, chosenId);

        if (projectUpdated) {
            System.out.println("\nProject edited successfully!");
        } else {
            System.out.println("\nProject not edited or it doesn't exist.");
        }
    }

    public static void deleteProject(MongoDatabase db) {
        System.out.println("=== Deleting a project ===");

        List<ObjectId> projectIds = new ArrayList<>();
        boolean hasProjects = ProjectModel.listAllByUserWithSelection(db, projectIds);

        if (!hasProjects) return;

        ObjectId chosenId = ProjectModel.chooseProjectFromList(projectIds);
        if (chosenId == null) return;

        boolean projectDeleted = ProjectModel.delete(chosenId, db);

        if (projectDeleted) {
            System.out.println("\nProject deleted successfully!");
        } else {
            System.out.println("\nProject not deleted or it doesn't exist.");
        }
    }
}