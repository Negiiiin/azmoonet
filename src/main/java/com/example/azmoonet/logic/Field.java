package com.example.azmoonet.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Field {
    private String ID;
    private String name;
    private List<Course> courses;

    public Field() {
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

    public void addCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Field{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}