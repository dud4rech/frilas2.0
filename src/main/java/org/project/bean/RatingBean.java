package org.project.bean;

import org.bson.types.ObjectId;

public class RatingBean {
    private ObjectId ratingId;
    private int ratingValue;
    private String ratingDescription;
    private ObjectId freelancerId;
    private ObjectId hirerId;

    public RatingBean(int ratingValue, String ratingDescription, ObjectId freelancerId, ObjectId hirerId) {
        this.ratingValue = ratingValue;
        this.ratingDescription = ratingDescription;
        this.freelancerId = freelancerId;
        this.hirerId = hirerId;
    }

    public RatingBean(ObjectId ratingId, int ratingValue, String ratingDescription) {
        this.ratingId = ratingId;
        this.ratingValue = ratingValue;
        this.ratingDescription = ratingDescription;
    }

    public RatingBean(ObjectId ratingId) {
        this.ratingId = ratingId;
    }

    public ObjectId getRatingId() {
        return ratingId;
    }

    public void setRatingId(ObjectId ratingId) {
        this.ratingId = ratingId;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getRatingDescription() {
        return ratingDescription;
    }

    public void setRatingDescription(String ratingDescription) {
        this.ratingDescription = ratingDescription;
    }

    public ObjectId getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(ObjectId freelancerId) {
        this.freelancerId = freelancerId;
    }

    public ObjectId getHirerId() {
        return hirerId;
    }

    public void setHirerId(ObjectId hirerId) {
        this.hirerId = hirerId;
    }
}
