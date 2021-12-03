package com.example.azmoonet.database.mappers.question;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.Mapper;
import com.example.azmoonet.logic.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class QuestionMapper extends Mapper<Question, String> implements IQuestionMapper {

    private static final String COLUMNS = " ID, question, rightAnswer, difficulty, answerAddress, subjectID";
    private static final String TABLE_NAME = "question_table";

    private Boolean doManage;

    public QuestionMapper(Boolean doManage) throws SQLException {
        if (this.doManage = doManage) {
            Connection con = ConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
            st.executeUpdate(String.format(
                    "CREATE TABLE %s (ID VARCHAR(40) NOT NULL , " +
                            "question VARCHAR(64) NOT NULL UNIQUE, " +
                            "rightAnswer INTEGER NOT NULL, " +
                            "difficulty INTEGER NOT NULL, " +
                            "answerAddress VARCHAR(64) NOT NULL, " +
                            "subjectID VARCHAR(40) NOT NULL, " +
                            "FOREIGN KEY(subjectID) REFERENCES subject_table(ID) ON DELETE CASCADE, " +
                            "PRIMARY KEY(ID));", TABLE_NAME));
            st.close();
            con.close();
        }
    }

    private List<Question> executingGivenQuery(String statement) throws SQLException {
        List<Question> result = new ArrayList<Question>();
        try (Connection con = ConnectionPool.getConnection()) {
            try (PreparedStatement st = con.prepareStatement(statement)) {
                try {
                    ResultSet resultSet = st.executeQuery();
                    while (resultSet.next())
                        result.add(convertResultSetToObject(resultSet));
                    return result;
                } catch (SQLException ex) {
                    System.out.println("error in Mapper.findByID 555 query.");
                    throw ex;
                }
            }
        }
    }

    public List<Question> searchQuestionBySubjectID(String inName) throws SQLException {
        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
                " WHERE subjectID = '" + inName + "';";
        return executingGivenQuery(statement);
    }

    public Question searchQuestionByQuestionID(String inName) throws SQLException {
        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
                " WHERE subjectID = '" + inName + "';";
        return executingGivenQuestionQuery(statement);
    }

    public Question executingGivenQuestionQuery(String inName) throws SQLException {
        try (Connection con = ConnectionPool.getConnection()) {
            try (PreparedStatement st = con.prepareStatement(inName)) {
                try {
                    ResultSet resultSet = st.executeQuery();
                    return convertResultSetToObject(resultSet);
                } catch (SQLException ex) {
                    System.out.println("error in Mapper.findByID 555 query.");
                    throw ex;
                }
            }
        }
    }


    @Override
    protected String getFindStatement(ArrayList<String> keys) {
        String questionID = keys.get(0);
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE ID = " + "'" + questionID + "'" +";";
    }

    @Override
    protected String getInsertStatement(Question question) {
        return "INSERT IGNORE INTO " + TABLE_NAME +
                "(" + COLUMNS + ")" + " VALUES "+
                "( "+
                "'" + question.getID()+ "'," +
                "'" + question.getQuestion()+ "'," +
                "'" + question.getRightAnswer()+ "'," +
                "'" + question.getDifficulty()+ "'," +
                "'" + question.getAnswer()+ "'," +
                "'" + question.getSubjectID()+ "'" +
                ");";
    }

    @Override
    protected String getDeleteStatement(ArrayList<String> keys) {
        String primary_key = keys.get(0);
        return "DELETE FROM " + TABLE_NAME +
                " WHERE ID = '" + primary_key + "';";
    }

    @Override
    protected Question convertResultSetToObject(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setID(rs.getString(1));
        question.setQuestion(rs.getString(2));
        question.setRightAnswer(rs.getInt(3));
        question.setDifficulty(rs.getInt(4));
        question.setAnswer(rs.getString(5));
        question.setSubjectID(rs.getString(6));
        System.out.println(question);
        return question;
    }

    @Override
    protected String getAllRows() {
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME ;
    }
}
