package com.example.azmoonet.logic;

import java.util.List;

public class Subject {
    private String ID;
    private String name;
    private int importance;
    private String courseID;
    private List<Question> questions;
    private List<Lesson> lessons;

    public Subject() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void addQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", importance='" + importance + '\'' +
                ", courseID='" + courseID + '\'' +
                '}';
    }
}