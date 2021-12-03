package com.example.azmoonet.logic;

public class UsersLesson {
    private String ID;
    private String userID;
    private String subjectID;
    private Boolean isPaid;

    public UsersLesson() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "UsersLesson{" +
                "ID='" + ID + '\'' +
                ", userID='" + userID + '\'' +
                ", subjectID='" + subjectID + '\'' +
                ", isPaid=" + isPaid +
                '}';
    }
}
