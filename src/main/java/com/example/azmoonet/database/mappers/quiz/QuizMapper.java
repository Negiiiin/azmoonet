package com.example.azmoonet.database.mappers.quiz;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.Mapper;
import com.example.azmoonet.logic.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizMapper  extends Mapper<Quiz, String> implements IQuizMapper {

    private static final String COLUMNS = " ID, date, userID, rightAnswers, answers, questionIDs";
    private static final String TABLE_NAME = "quiz_table";

    private Boolean doManage;

    public QuizMapper(Boolean doManage) throws SQLException {
        if (this.doManage = doManage) {
            Connection con = ConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
            st.executeUpdate(String.format(
                    "CREATE TABLE %s (ID VARCHAR(40) NOT NULL , " +
                            "date DATE NOT NULL, " +
                            "userID VARCHAR(40) NOT NULL, " +
                            "rightAnswers VARCHAR(512) NOT NULL, " +
                            "answers VARCHAR(512) NOT NULL, " +
                            "questionIDs TEXT NOT NULL, " +
                            "FOREIGN KEY(userID) REFERENCES user_table(ID) ON DELETE CASCADE, " +
                            "PRIMARY KEY(ID));", TABLE_NAME));
            st.close();
            con.close();
        }
    }

    private List<Quiz> executingGivenQuery(String statement) throws SQLException {
        List<Quiz> result = new ArrayList<Quiz>();
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement st = con.prepareStatement(statement);
        ) {
            try {
                ResultSet resultSet = st.executeQuery();
                while (resultSet.next())
                    result.add(convertResultSetToObject(resultSet));
                return result;
            } catch (SQLException ex) {
                System.out.println("error in Mapper.findByID 666 query.");
                throw ex;
            }
        }
    }

    public List<Quiz> searchQuizByUserID(String inName) throws SQLException {
        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
                " WHERE userID = '" + inName + "';";
        return executingGivenQuery(statement);
    }

    @Override
    protected String getFindStatement(ArrayList<String> keys) {
        String quizID = keys.get(0);
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE ID = " + "'" + quizID + "'" +";";
    }

    @Override
    protected String getInsertStatement(Quiz quiz) {
        return "INSERT IGNORE INTO " + TABLE_NAME +
                "(" + COLUMNS + ")" + " VALUES "+
                "("+
                "'" + quiz.getID()+ "'," +
                "'" + quiz.getDate()+ "'," +
                "'" + quiz.getUserID()+ "'," +
                "'" + quiz.getRightAnswers().toString() + "'," +
                "'" + quiz.getAnswers().toString() + "'," +
                "'" + quiz.getQuestionIDs().toString() + "'" +
                ");";
    }

    @Override
    protected String getDeleteStatement(ArrayList<String> keys) {
        String primary_key = keys.get(0);
        return "DELETE FROM " + TABLE_NAME +
                " WHERE ID = '" + primary_key + "';";
    }

    @Override
    protected Quiz convertResultSetToObject(ResultSet rs) throws SQLException {
        Quiz quiz = new Quiz();
        quiz.setID(rs.getString(1));
        quiz.setDate(rs.getDate(2));
        quiz.setUserID(rs.getString(3));
        quiz.setRightAnswers(convertStringToIntegerList(rs.getString(4)));
        quiz.setAnswers(convertStringToIntegerList(rs.getString(5)));
        quiz.setQuestionIDs(convertStringToStringList(rs.getString(6)));
        return quiz;
    }

    @Override
    protected String getAllRows() {
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME ;
    }

    public static List<Integer> convertStringToIntegerList(String str) {
        List<Integer> temp = new ArrayList<>();
        for (char ch: str.toCharArray()) {
            if ( ch == '[' || ch == ']' || ch == ',' || ch == ' ') {continue;}
            temp.add(Character.getNumericValue(ch));
        }
        return temp;
    }

    public static List<String> convertStringToStringList(String str) {
        str = str.substring(1,(str.length() - 1));
        List<String> temp = new ArrayList<>();
        temp = Arrays.asList(str.split(", "));
        return temp;
    }
}