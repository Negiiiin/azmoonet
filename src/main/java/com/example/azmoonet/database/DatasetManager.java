package com.example.azmoonet.database;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.question.QuestionMapper;
import com.example.azmoonet.logic.Question;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DatasetManager {

    public ArrayList<String> listFilesForFolder(final File folder) {
        ArrayList<String> result = new ArrayList<String>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
//                System.out.println(fileEntry.getName());
                result.add(fileEntry.getName());
            }
        }
        return result;
    }

    public void insertQuestions() throws SQLException, IOException {
        InputStream questionInputStream = getClass().getResourceAsStream("/assets/questions/");
        InputStream answerInputStream = getClass().getResourceAsStream("/assets/answers/");
        BufferedReader questionReader = new BufferedReader(new InputStreamReader(questionInputStream));
        List<String> questions = questionReader.lines()
                .collect(Collectors.toList());

        BufferedReader answerReader = new BufferedReader(new InputStreamReader(answerInputStream));
        List<String> answers = answerReader.lines()
                .collect(Collectors.toList());

        for (String question : questions) {
            for (String answer : answers) {
                if (question.substring(0, 11).equals(answer.substring(0, 11))) {
                    Question newQuestion = new Question();
                    newQuestion.setID(UUID.randomUUID().toString());

                    newQuestion.setQuestion("assets/questions/" + question);

                    newQuestion.setSubjectID(question.substring(7, 11));
                    newQuestion.setRightAnswer(Integer.parseInt(answer.substring(11, 12)));
                    newQuestion.setDifficulty(Integer.parseInt(answer.substring(12, 13)));
                    newQuestion.setAnswer("assets/answers/" + answer);
                    Connection connection = ConnectionPool.getConnection();
                    QuestionMapper questionMapper = new QuestionMapper(false);
                    questionMapper.insert(newQuestion);
                    connection.close();
                }
            }
        }
    }
}
