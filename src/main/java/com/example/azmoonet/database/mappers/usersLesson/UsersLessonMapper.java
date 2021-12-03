package com.example.azmoonet.database.mappers.usersLesson;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.Mapper;

import com.example.azmoonet.logic.Question;
import com.example.azmoonet.logic.UsersLesson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersLessonMapper extends Mapper<UsersLesson, String> implements IUsersLessonMapper {

    private static final String COLUMNS = " ID, userID, subjectID, isPaid";
    private static final String TABLE_NAME = "usersLesson_table";

    private Boolean doManage;

    public UsersLessonMapper(Boolean doManage) throws SQLException {
        if (this.doManage = doManage) {
            Connection con = ConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
            st.executeUpdate(String.format(
                    "CREATE TABLE %s (ID VARCHAR(40) NOT NULL , " +
                            "userID VARCHAR(40) NOT NULL , " +
                            "subjectID VARCHAR(40) NOT NULL, " +
                            "isPaid  BIT NOT NULL, " +
                            "FOREIGN KEY(userID) REFERENCES user_table(ID) ON DELETE CASCADE, " +
                            "FOREIGN KEY(subjectID) REFERENCES subject_table(ID) ON DELETE CASCADE, " +
                            "PRIMARY KEY(ID));", TABLE_NAME));
            st.close();
            con.close();
        }
    }

    private List<UsersLesson> executingGivenQuery(String statement) throws SQLException {
        List<UsersLesson> result = new ArrayList<UsersLesson>();
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

    @Override
    protected String getFindStatement(ArrayList<String> keys) {
        String usersLessonID = keys.get(0);
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE ID = " + "'" + usersLessonID + "'" +";";
    }

    public List<UsersLesson> findByUserID(String userID) throws SQLException {
        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
                " WHERE userID = '" + userID + "';";
        return executingGivenQuery(statement);
    }

    public boolean checkIfValid(String userID, String subjectID) throws SQLException {
        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
                " WHERE userID = '" + userID + "' and subjectID = '" + subjectID + "';";
        List<UsersLesson> result = executingGivenQuery(statement);
        if(result.isEmpty()) {
            return false;
        }
        else return result.get(0).getIsPaid() != false;
    }

    @Override
    protected String getInsertStatement(UsersLesson usersLesson) {
        return "INSERT IGNORE INTO " + TABLE_NAME +
                "(" + COLUMNS + ")" + " VALUES "+
                "( "+
                "'" + usersLesson.getID()+ "'," +
                "'" + usersLesson.getUserID()+ "'," +
                "'" + usersLesson.getSubjectID()+ "'," +
                "'" + usersLesson.getIsPaid()+ "'" +
                ");";
    }

    @Override
    protected String getDeleteStatement(ArrayList<String> keys) {
        String primary_key = keys.get(0);
        return "DELETE FROM " + TABLE_NAME +
                " WHERE ID = '" + primary_key + "';";
    }

    @Override
    protected UsersLesson convertResultSetToObject(ResultSet rs) throws SQLException {
        UsersLesson usersLesson = new UsersLesson();
        usersLesson.setID(rs.getString(1));
        usersLesson.setUserID(rs.getString(2));
        usersLesson.setSubjectID(rs.getString(3));
        usersLesson.setIsPaid(rs.getBoolean(4));
        return usersLesson;
    }

    @Override
    protected String getAllRows() {
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME ;
    }
}
