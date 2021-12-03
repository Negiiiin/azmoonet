package com.example.azmoonet.logic;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.question.QuestionMapper;
import com.example.azmoonet.database.mappers.subject.SubjectMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Quiz {
    private String ID;
    private Date date;
    private String userID;
    private List<Integer> rightAnswers;
    private List<Integer> answers;
    private List<String> questionIDs;


    public Quiz() {
        this.rightAnswers = new ArrayList<Integer>();
        this.answers = new ArrayList<Integer>();
        this.questionIDs = new ArrayList<String>();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<Integer> getRightAnswers() {
        return rightAnswers;
    }

    public void setRightAnswers(List<Integer> rightAnswers) {
        this.rightAnswers = rightAnswers;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    public List<String> getQuestionIDs() {
        return questionIDs;
    }

    public void setQuestionIDs(List<String> questionIDs) {
        this.questionIDs = questionIDs;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "ID='" + ID + '\'' +
                ", date='" + date + '\'' +
                ", userID='" + userID + '\'' +
                ", rightAnswers='" + rightAnswers + '\'' +
                ", answers='" + answers + '\'' +
                ", questionIDs='" + questionIDs + '\'' +
                '}';
    }

    public List<Question> getQuestion(String courseID) throws SQLException {
        List<Question> questions = new ArrayList<Question>();
        Connection connection = ConnectionPool.getConnection();
        SubjectMapper subjectMapper = new SubjectMapper(false);
        QuestionMapper questionMapper = new QuestionMapper(false);
        List<Subject> subjectsOfThisCourse = subjectMapper.searchSubjectByCourseID(courseID);
        for (Subject s : subjectsOfThisCourse) {
            List<Question> questionsOfThisSubject = questionMapper.searchQuestionBySubjectID(s.getID());

            if (!questionsOfThisSubject.isEmpty()) {
                while (true) {
                    Question tempQuestion = getRandomQuestionFromList(questionsOfThisSubject);
                    if (tempQuestion.getDifficulty() <= 2 && !questions.contains(tempQuestion)) {
                        questions.add(tempQuestion);
                        break;
                    }
                }
            }
//            for (Question q : questionsOfThisSubject) {
//                if (q.getDifficulty() <= 2 && 0.5 < Math.random()) {
//                    questions.add(q);
//                    break;
//                }
//            }
        }
        connection.close();
        return questions;
    }

    public List<Question> getQuestionFromGivenSubject(String courseID, String subjectID) throws SQLException {
        List<Question> questions = new ArrayList<Question>();
        Connection connection = ConnectionPool.getConnection();

        QuestionMapper questionMapper = new QuestionMapper(false);
        List<Question> questionsOfThisSubject = questionMapper.searchQuestionBySubjectID(subjectID);

        Integer easy = 0;
        Integer medium = 0;
        Integer hard = 0;

        if (!questionsOfThisSubject.isEmpty()) {
            while (questions.size() < 15) {
                Question tempQuestion = getRandomQuestionFromList(questionsOfThisSubject);

                if (tempQuestion.getDifficulty() == 1 && easy < 5 && !questions.contains(tempQuestion)) {
                    questions.add(tempQuestion);
                    easy++;
                } else if (tempQuestion.getDifficulty() == 2 && medium < 5 && !questions.contains(tempQuestion)) {
                    questions.add(tempQuestion);
                    medium++;
                } else if (tempQuestion.getDifficulty() == 3 && hard < 5 && !questions.contains(tempQuestion)) {
                    questions.add(tempQuestion);
                    hard++;
                }
            }
        }

        connection.close();
        return questions;
    }

    public Question getRandomQuestionFromList(List<Question> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public List<String> getQuestionID(List<Question> questions) {
        List<String> questionsID = new ArrayList<String>();
        for (Question q: questions) {
            questionsID.add(q.getID());
        }
        return questionsID;
    }

    public List<Integer> getRightAnswers(List<Question> questions) {
        List<Integer> rightAnswers = new ArrayList<Integer>();
        for (Question q: questions) {
            rightAnswers.add(q.getRightAnswer());
        }
        return rightAnswers;
    }

    public List<Integer> getRightAnswersBYQuestionsIDs(String courseID, List<String> questionsIDs) throws SQLException {
        List<Integer> rightAnswers = new ArrayList<Integer>();
        List<Question> temp_questions = this.getQuestion(courseID);
        for (String qID: questionsIDs) {
            for (Question q : temp_questions) {
                if (qID == q.getID()) {
                    rightAnswers.add(q.getRightAnswer());
                }
            }
        }
        return rightAnswers;
    }
}