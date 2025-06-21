package org.project.bean;

import org.bson.types.ObjectId;

public class ProposalBean {
    private ObjectId proposalId;
    private int proposalValue;
    private String proposalDescription;
    private ObjectId freelancerId;
    private ObjectId projectId;

    public ProposalBean(int proposalValue, String proposalDescription, ObjectId freelancerId, ObjectId projectId) {
        this.proposalValue = proposalValue;
        this.proposalDescription = proposalDescription;
        this.freelancerId = freelancerId;
        this.projectId = projectId;
    }

    public ProposalBean(ObjectId proposalId, int proposalValue, String proposalDescription) {
        this.proposalId = proposalId;
        this.proposalValue = proposalValue;
        this.proposalDescription = proposalDescription;
    }

    public ProposalBean(ObjectId proposalId) {
        this.proposalId = proposalId;
    }

    public ObjectId getProposalId() {
        return proposalId;
    }

    public void setProposalId(ObjectId proposalId) {
        this.proposalId = proposalId;
    }

    public int getProposalValue() {
        return proposalValue;
    }

    public void setProposalValue(int proposalValue) {
        this.proposalValue = proposalValue;
    }

    public String getProposalDescription() {
        return proposalDescription;
    }

    public void setProposalDescription(String proposalDescription) {
        this.proposalDescription = proposalDescription;
    }

    public ObjectId getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(ObjectId freelancerId) {
        this.freelancerId = freelancerId;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }
}
