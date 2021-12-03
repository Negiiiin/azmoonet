package com.example.azmoonet.database.mappers.lesson;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.Mapper;
import com.example.azmoonet.logic.Lesson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LessonMapper extends Mapper<Lesson, String> implements ILessonMapper {

    private static final String COLUMNS = " ID, name, content, difficulty, importance, subjectID";
    private static final String TABLE_NAME = "lesson_table";

    private Boolean doManage;

    public LessonMapper(Boolean doManage) throws SQLException {
        if (this.doManage = doManage) {
            Connection con = ConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
            st.executeUpdate(String.format(
            "CREATE TABLE %s (ID VARCHAR(40) NOT NULL , " +
            "name VARCHAR(20) NOT NULL UNIQUE, " +
            "content VARCHAR(64) NOT NULL UNIQUE, " +
            "difficulty INTEGER NOT NULL, " +
            "importance INTEGER NOT NULL, " +
            "subjectID VARCHAR(40) NOT NULL, " +
            "FOREIGN KEY(subjectID) REFERENCES subject_table(ID) ON DELETE CASCADE, " +
            "PRIMARY KEY(ID));", TABLE_NAME));
            st.close();
            con.close();
        }
    }

    private List<Lesson> executingGivenQuery(String statement) throws SQLException {
        List<Lesson> result = new ArrayList<Lesson>();
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

    public List<Lesson> searchLessonBySubjectID(String inName) throws SQLException {
        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
            " WHERE subjectID = '" + inName + "';";
        return executingGivenQuery(statement);
    }

    @Override
    protected String getFindStatement(ArrayList<String> keys) {
        String lessonID = keys.get(0);
        return "SELECT " + COLUMNS +
        " FROM " + TABLE_NAME +
        " WHERE ID = " + "'" + lessonID + "'" +";";
    }

@Override
protected String getInsertStatement(Lesson lesson) {
        return "INSERT IGNORE INTO " + TABLE_NAME +
        "(" + COLUMNS + ")" + " VALUES "+
        "( "+
        "'" + lesson.getID()+ "'," +
        "'" + lesson.getName()+ "'," +
        "'" + lesson.getContent()+ "'," +
        "'" + lesson.getDifficulty()+ "'," +
        "'" + lesson.getImportance()+ "'," +
        "'" + lesson.getSubjectID()+ "'" +
        ");";
        }

@Override
protected String getDeleteStatement(ArrayList<String> keys) {
        String primary_key = keys.get(0);
        return "DELETE FROM " + TABLE_NAME +
        " WHERE ID = '" + primary_key + "';";
        }

@Override
protected Lesson convertResultSetToObject(ResultSet rs) throws SQLException {
    Lesson lesson = new Lesson();
    lesson.setID(rs.getString(1));
    lesson.setName(rs.getString(2));
    lesson.setContent(rs.getString(3));
    lesson.setDifficulty(rs.getInt(4));
    lesson.setImportance(rs.getInt(5));
    lesson.setSubjectID(rs.getString(6));
    return lesson;
}

@Override
protected String getAllRows() {
    return "SELECT " + COLUMNS +
    " FROM " + TABLE_NAME ;
    }
}
