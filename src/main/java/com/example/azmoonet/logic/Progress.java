package com.example.azmoonet.logic;

public class Progress {
    private String ID;
    private Double learned;
    private String userID;
    private String subjectID;


    public Progress() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Double getLearned() {
        return learned;
    }

    public void setLearned(Double learned) {
        this.learned = learned;
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

    @Override
    public String toString() {
        return "Progress{" +
                "ID='" + ID + '\'' +
                ", learned='" + learned + '\'' +
                ", userID='" + userID + '\'' +
                ", subjectID='" + subjectID + '\'' +
                '}';
    }
}