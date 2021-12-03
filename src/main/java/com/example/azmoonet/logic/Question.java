package com.example.azmoonet.logic;

public class Question {
    private String ID;
    private String question;
    private int rightAnswer;
    private int difficulty;
    private String answerAddress;
    private String subjectID;

    public Question() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getAnswer() {
        return answerAddress;
    }

    public void setAnswer(String answer) {
        this.answerAddress = answer;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

//    @Override
//    public String toString() {
//        return "Question{" +
//                "ID='" + ID + '\'' +
//                ", question='" + question + '\'' +
//                ", rightAnswer='" + rightAnswer + '\'' +
//                ", difficulty='" + difficulty + '\'' +
//                ", answer='" + answer + '\'' +
//                ", subjectID='" + subjectID + '\'' +
//                '}';
//    }
}