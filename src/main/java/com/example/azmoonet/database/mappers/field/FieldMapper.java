package com.example.azmoonet.database.mappers.field;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.Mapper;
import com.example.azmoonet.database.mappers.course.CourseMapper;
import com.example.azmoonet.logic.Course;
import com.example.azmoonet.logic.Field;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FieldMapper extends Mapper<Field, String> implements IFieldMapper {

    private static final String COLUMNS = " ID, name";
    private static final String TABLE_NAME = "field_table";

    private Boolean doManage;

    public FieldMapper(Boolean doManage) throws SQLException {
        if (this.doManage = doManage) {
            Connection con = ConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
//            System.out.println(String.format(
//                    "CREATE TABLE %s (ID VARCHAR(40) NOT NULL , " +
//                            "name VARCHAR(32) NOT NULL UNIQUE, " +
//                            "PRIMARY KEY (ID));", TABLE_NAME));
            st.executeUpdate(String.format(
                    "CREATE TABLE %s (ID varchar(40) NOT NULL , " +
                            "name VARCHAR(32) NOT NULL UNIQUE, " +
                            "PRIMARY KEY(ID));", TABLE_NAME));
            st.close();
            con.close();
        }
    }

    @Override
    protected String getFindStatement(ArrayList<String> keys) {
        String fieldID = keys.get(0);
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE ID = " + "'" + fieldID + "'" +";";
    }

    @Override
    protected String getInsertStatement(Field field) {
        return "INSERT IGNORE INTO " + TABLE_NAME +
                "(" + COLUMNS + ")" + " VALUES "+
                "("+
                "'"+ field.getID()+ "'," +
                "'" + field.getName()+ "'" +
                ");";
    }

    @Override
    protected String getDeleteStatement(ArrayList<String> keys) {
        String primary_key = keys.get(0);
        return "DELETE FROM " + TABLE_NAME +
                " WHERE ID = '" + primary_key + "';";
    }

    @Override
    protected Field convertResultSetToObject(ResultSet rs) throws SQLException {
        Field field = new Field();
        field.setID(rs.getString(1));
        field.setName(rs.getString(2));
        Connection connection = ConnectionPool.getConnection();
        CourseMapper courseMapper = new CourseMapper(false);
        List<Course> courses = courseMapper.searchCourseByFieldID(field.getID());
        field.addCourses(courses);
        connection.close();
        return field;
    }

    @Override
    protected String getAllRows() {
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME ;
    }

}
