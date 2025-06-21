package org.project.bean;

import org.bson.types.ObjectId;

public class PaymentBean {
    private ObjectId paymentId;
    private int paymentValue;
    private String paymentDate;
    private ObjectId projectId;
    private ObjectId freelancerId;
    private ObjectId hirerid;

    public PaymentBean(int paymentValue, String paymentDate, ObjectId projectId, ObjectId freelancerId, ObjectId hirerid) {
        this.paymentValue = paymentValue;
        this.paymentDate = paymentDate;
        this.projectId = projectId;
        this.freelancerId = freelancerId;
        this.hirerid = hirerid;
    }

    public PaymentBean(ObjectId paymentId, int paymentValue, String paymentDate, ObjectId freelancerId, ObjectId hirerid) {
        this.paymentId = paymentId;
        this.paymentValue = paymentValue;
        this.paymentDate = paymentDate;
        this.freelancerId = freelancerId;
        this.hirerid = hirerid;
    }

    public PaymentBean(ObjectId paymentId) {
        this.paymentId = paymentId;
    }

    public ObjectId getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(ObjectId paymentId) {
        this.paymentId = paymentId;
    }

    public int getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(int paymentValue) {
        this.paymentValue = paymentValue;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public ObjectId getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(ObjectId freelancerId) {
        this.freelancerId = freelancerId;
    }

    public ObjectId getHirerid() {
        return hirerid;
    }

    public void setHirerid(ObjectId hirerid) {
        this.hirerid = hirerid;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }
}
