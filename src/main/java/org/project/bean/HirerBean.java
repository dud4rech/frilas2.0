package org.project.bean;

public class HirerBean {
    private int hirerId;
    private String hirerName;
    private String hirerPhone;
    private String hirerMail;

    public HirerBean(String hirerName, String hirerPhone, String hirerMail) {
        this.hirerName = hirerName;
        this.hirerPhone = hirerPhone;
        this.hirerMail = hirerMail;
    }

    public HirerBean(int hirerId, String freelancerName, String hirerPhone, String hirerMail) {
        this.hirerId = hirerId;
        this.hirerName = freelancerName;
        this.hirerPhone = hirerPhone;
        this.hirerMail = hirerMail;
    }

    public int getHirerId() {
        return hirerId;
    }

    public void setHirerId(int hirerId) {
        this.hirerId = hirerId;
    }

    public String getHirerName() {
        return hirerName;
    }

    public void setHirerName(String hirerName) {
        this.hirerName = hirerName;
    }

    public String getHirerPhone() {
        return hirerPhone;
    }

    public void setHirerPhone(String hirerPhone) {
        this.hirerPhone = hirerPhone;
    }

    public String getHirerMail() {
        return hirerMail;
    }

    public void setHirerMail(String hirerMail) {
        this.hirerMail = hirerMail;
    }
}