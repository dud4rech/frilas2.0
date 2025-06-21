package org.project.bean;

import org.bson.types.ObjectId;

public class ProjectBean {
    private ObjectId projectId;
    private String projectName;
    private String projectDescription;
    private String projectDeadline;
    private int projectBudget;
    private ObjectId hirerId;

    public ProjectBean(String projectName, String projectDescription, String projectDeadline, int projectBudget, ObjectId hirerId) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectDeadline = projectDeadline;
        this.projectBudget = projectBudget;
        this.hirerId = hirerId;
    }

    public ProjectBean(ObjectId projectId, String projectName, String projectDescription, String projectDeadline, int projectBudget) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectDeadline = projectDeadline;
        this.projectBudget = projectBudget;
    }

    public ProjectBean(ObjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectBean(String projectName, String projectDescription, String projectDeadline, int projectBudget) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectDeadline = projectDeadline;
        this.projectBudget = projectBudget;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projecDescription) {
        this.projectDescription = projecDescription;
    }

    public String getProjectDeadline() {
        return projectDeadline;
    }

    public void setProjectDeadline(String projectDeadline) {
        this.projectDeadline = projectDeadline;
    }

    public int getProjectBudget() {
        return projectBudget;
    }

    public void setProjectBudget(int projectBudget) {
        this.projectBudget = projectBudget;
    }

    public ObjectId getHirerId() { return hirerId; }

    public void setHirerId(ObjectId hirerId) {
        this.hirerId = hirerId;
    }
}