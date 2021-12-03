package com.example.azmoonet.database.mappers.subject;
import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.Mapper;
import com.example.azmoonet.database.mappers.lesson.LessonMapper;
import com.example.azmoonet.database.mappers.question.QuestionMapper;
import com.example.azmoonet.logic.Lesson;
import com.example.azmoonet.logic.Question;
import com.example.azmoonet.logic.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectMapper extends Mapper<Subject, String> implements ISubjectMapper {

    private static final String COLUMNS = " ID, name, importance, courseID";
    private static final String TABLE_NAME = "subject_table";

    private Boolean doManage;

    public SubjectMapper(Boolean doManage) throws SQLException {
        if (this.doManage = doManage) {
            Connection con = ConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
            st.executeUpdate(String.format(
                    "CREATE TABLE %s (ID VARCHAR(40) NOT NULL , " +
                            "name VARCHAR(32) NOT NULL UNIQUE, " +
                            "importance INTEGER NOT NULL, " +
                            "courseID VARCHAR(40) NOT NULL, " +
                            "FOREIGN KEY(courseID) REFERENCES course_table(ID) ON DELETE CASCADE, " +
                            "PRIMARY KEY(ID));", TABLE_NAME));
            st.close();
            con.close();
        }
    }

    private List<Subject> executingGivenQuery(String statement) throws SQLException {
        List<Subject> result = new ArrayList<Subject>();
        try (Connection con = ConnectionPool.getConnection()) {
            try (PreparedStatement st = con.prepareStatement(statement)) {
                try {
                    ResultSet resultSet = st.executeQuery();
                    while (resultSet.next())
                        result.add(convertResultSetToObject(resultSet));
                    return result;
                } catch (SQLException ex) {
                    System.out.println("error in Mapper.findByID 777 query.");
                    throw ex;
                }
            }
        }
    }

//    public List<Subject> searchSubjectByImportance(String inName) throws SQLException {
//        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
//                " WHERE importance = '" + inName + "';";
//        return executingGivenQuery(statement);
//    }

    public List<Subject> searchSubjectByCourseID(String inName) throws SQLException {
        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
                " WHERE courseID = '" + inName + "';";
        return executingGivenQuery(statement);
    }

//    public List<Subject> searchSubjectBySubjectID(String inName) throws SQLException {
//        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
//                " WHERE ID = '" + inName + "';";
//        return executingGivenQuery(statement);
//    }

    @Override
    protected String getFindStatement(ArrayList<String> keys) {
        String subjectName = keys.get(0);
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE ID = " + "'" + subjectName + "'" +";";
    }

    @Override
    protected String getInsertStatement(Subject subject) {
        return "INSERT IGNORE INTO " + TABLE_NAME +
                "(" + COLUMNS + ")" + " VALUES "+
                "( "+
                "'" + subject.getID()+ "'," +
                "'" + subject.getName()+ "'," +
                "'" + subject.getImportance()+ "'," +
                "'" + subject.getCourseID()+ "'" +
                ");";
    }

    @Override
    protected String getDeleteStatement(ArrayList<String> keys) {
        String primary_key = keys.get(0);
        return "DELETE FROM " + TABLE_NAME +
                " WHERE ID = '" + primary_key + "';";
    }

    @Override
    protected Subject convertResultSetToObject(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setID(rs.getString(1));
        subject.setName(rs.getString(2));
        subject.setImportance(rs.getInt(3));
        subject.setCourseID(rs.getString(4));
        Connection connection = ConnectionPool.getConnection();
        QuestionMapper questionMapper = new QuestionMapper(false);
        LessonMapper LessonMapper = new LessonMapper(false);
        List<Question> questions = questionMapper.searchQuestionBySubjectID(subject.getID());
        List<Lesson> lessons = LessonMapper.searchLessonBySubjectID(subject.getID());
        subject.addQuestions(questions);
        subject.addLessons(lessons);
        connection.close();
        return subject;
    }

    @Override
    protected String getAllRows() {
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME + ";";
    }
}
