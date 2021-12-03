package com.example.azmoonet.logic;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String ID;
    private String name;
    private String fieldID;
    private List<Subject> courseSubjects;

    public Course() {
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

    public String getFieldID() {
        return fieldID;
    }

    public void setFieldID(String fieldID) {
        this.fieldID = fieldID;
    }

    public void addSubjects(List<Subject> subjects) {
        this.courseSubjects = subjects;
    }

    @Override
    public String toString() {
        return "Course{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", fieldID='" + fieldID + '\'' +
                '}';
    }
}