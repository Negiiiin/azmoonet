package com.example.azmoonet.database.mappers.course;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.Mapper;
import com.example.azmoonet.database.mappers.subject.SubjectMapper;
import com.example.azmoonet.logic.Course;
import com.example.azmoonet.logic.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseMapper extends Mapper<Course, String> implements ICourseMapper {

    private static final String COLUMNS = " ID, name, fieldID";
    private static final String TABLE_NAME = "course_table";

    private Boolean doManage;

    public CourseMapper(Boolean doManage) throws SQLException {
        if (this.doManage = doManage) {
            Connection con = ConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
            st.executeUpdate(String.format(
                    "CREATE TABLE %s (ID VARCHAR(40) NOT NULL ,  " +
                            "name VARCHAR(32) NOT NULL UNIQUE, " +
                            "fieldID VARCHAR(40) NOT NULL, " +
                            "FOREIGN KEY(fieldID) REFERENCES field_table(ID) ON DELETE CASCADE, " +
                            "PRIMARY KEY(ID));", TABLE_NAME));
            st.close();
            con.close();
        }
    }

    private List<Course> executingGivenQuery(String statement) throws SQLException {
        List<Course> result = new ArrayList<Course>();
        try (Connection con = ConnectionPool.getConnection()) {
            try (PreparedStatement st = con.prepareStatement(statement)) {
                try {
                    ResultSet resultSet = st.executeQuery();
                    while (resultSet.next())
                        result.add(convertResultSetToObject(resultSet));
                    return result;
                } catch (SQLException ex) {
                    System.out.println("error in Mapper.findByID 333 query.");
                    throw ex;
                }
            }
        }
    }

    public List<Course> searchCourseByFieldID(String inName) throws SQLException {
        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
                " WHERE fieldID = '" + inName + "';";
        return executingGivenQuery(statement);
    }

    @Override
    protected String getFindStatement(ArrayList<String> keys) {
        String courseID = keys.get(0);
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE ID = " + "'" + courseID + "'" +";";
    }

    @Override
    protected String getInsertStatement(Course course) {
        return "INSERT IGNORE INTO " + TABLE_NAME +
                "(" + COLUMNS + ")" + " VALUES "+
                "( "+
                "'" + course.getID()+ "'," +
                "'" + course.getName()+ "'," +
                "'" + course.getFieldID()+ "'" +
                ");";
    }

    @Override
    protected String getDeleteStatement(ArrayList<String> keys) {
        String primary_key = keys.get(0);
        return "DELETE FROM " + TABLE_NAME +
                " WHERE ID = '" + primary_key + "';";
    }

    @Override
    protected Course convertResultSetToObject(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setID(rs.getString(1));
        course.setName(rs.getString(2));
        course.setFieldID(rs.getString(3));
        Connection connection = ConnectionPool.getConnection();
        SubjectMapper subjectMapper = new SubjectMapper(false);
        List<Subject> subjects = subjectMapper.searchSubjectByCourseID(course.getID());
        course.addSubjects(subjects);
        connection.close();
        return course;
    }

    @Override
    protected String getAllRows() {
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME ;
    }
}