package com.example.azmoonet.logic;

public class Lesson {
    private String ID;
    private String name;
    private String content;
    private int difficulty;
    private int importance;
    private String subjectID;

    public Lesson() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    @Override
    public String toString() {
        return "Syllabus{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", importance='" + importance + '\'' +
                ", subjectID='" + subjectID + '\'' +
                '}';
    }
}
