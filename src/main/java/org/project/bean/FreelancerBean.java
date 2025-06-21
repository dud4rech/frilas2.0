package org.project.bean;

public class FreelancerBean {
   private int freelancerId;
   private String freelancerName;
   private String freelancerPhone;
   private String freelancerMail;

    public FreelancerBean(String freelancerName, String freelancerPhone, String freelancerMail) {
        this.freelancerName = freelancerName;
        this.freelancerPhone = freelancerPhone;
        this.freelancerMail = freelancerMail;
    }

   public FreelancerBean(int freelancerId, String freelancerName, String freelancerPhone, String freelancerMail) {
       this.freelancerId = freelancerId;
       this.freelancerName = freelancerName;
       this.freelancerPhone = freelancerPhone;
       this.freelancerMail = freelancerMail;
   }

    public int getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(int freelancerId) {
        this.freelancerId = freelancerId;
    }

    public String getFreelancerName() {
        return freelancerName;
    }

    public void setFreelancerName(String freelancerName) {
        this.freelancerName = freelancerName;
    }

    public String getFreelancerPhone() {
        return freelancerPhone;
    }

    public void setFreelancerPhone(String freelancerPhone) {
        this.freelancerPhone = freelancerPhone;
    }

    public String getFreelancerMail() {
        return freelancerMail;
    }

    public void setFreelancerMail(String freelancerMail) {
        this.freelancerMail = freelancerMail;
    }
}