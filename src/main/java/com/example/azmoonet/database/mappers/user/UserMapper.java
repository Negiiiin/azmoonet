package com.example.azmoonet.database.mappers.user;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.Mapper;
import com.example.azmoonet.logic.User;

import java.sql.*;
import java.util.ArrayList;

public class UserMapper extends Mapper<User, String> implements IUserMapper {

    private static final String COLUMNS = " ID, name, phoneNumber, password, field, year, registerDate ";
    private static final String TABLE_NAME = "user_table";

    private Boolean doManage;

    public UserMapper(Boolean doManage) throws SQLException {
        if (this.doManage = doManage) {
            Connection con = ConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
            st.executeUpdate(String.format(
                    "CREATE TABLE  %s " +
                            "(" +
                            "ID VARCHAR(40) NOT NULL, " +
                            "name VARCHAR(32), " +
                            "phoneNumber VARCHAR(11) UNIQUE, " +
                            "password VARCHAR(32), " +
                            "field VARCHAR(40), " +
                            "year INTEGER, " +
                            "registerDate DATE, " +
                            "FOREIGN KEY(field) REFERENCES field_table(ID) ON DELETE CASCADE, " +
                            "PRIMARY KEY(ID)" +
                            ");",
                    TABLE_NAME));
            st.close();
            con.close();
        }
    }

    protected String findSpecUser(String phoneNumber, String password) {
        System.out.println("SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE phoneNumber = '"+  phoneNumber + "' and password = '" + password +"';");
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE phoneNumber = '"+  phoneNumber + "' and password = '" + password +"';";
    }

    protected String findUserByPhoneNumber(String phoneNumber) {
        System.out.println("SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE phoneNumber = '"+  phoneNumber + "';");
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE phoneNumber = '"+  phoneNumber + "';";
    }

    public User findForSignup(String email) throws SQLException {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement st = con.prepareStatement(findUserByPhoneNumber(email))
        ) {
            ResultSet resultSet;
            try {
                resultSet = st.executeQuery();
                Object res = resultSet.next();
                if(!(Boolean)res)
                    return null;
                return convertResultSetToObject(resultSet);
            } catch (SQLException ex) {
                System.out.println("error in Mapper.findByID query.");
                throw ex;
            }
        }
    }

    public User findForLogin(String email, String password) throws SQLException {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement st = con.prepareStatement(findSpecUser(email, password))
        ) {
            ResultSet resultSet;
            try {
                resultSet = st.executeQuery();
                Object res = resultSet.next();
                if(!(Boolean)res)
                    return null;
                return convertResultSetToObject(resultSet);
            } catch (SQLException ex) {
                System.out.println("error in Mapper.findByID query.");
                throw ex;
            }
        }
    }

    @Override
    protected String getFindStatement(ArrayList<String> keys) {
        String userID = keys.get(0);
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE ID = " + "'" + userID + "'" +";";
    }

    @Override
    protected String getInsertStatement(User user) {
        return "INSERT IGNORE INTO " + TABLE_NAME +
                "(" + COLUMNS + ")" + " VALUES "+
                "( "+
                "'" + user.getID() + "'," +
                "'" + user.getName() + "'," +
                "'" + user.getPhoneNumber() + "'," +
                "'" + user.getPassword() + "'," +
                "'" + user.getField() + "'," +
                "'" + user.getYear() + "'," +
                "'" + user.getRegisterDate() + "'" +
                ");";
    }

    @Override
    protected String getDeleteStatement(ArrayList<String> keys) {
        String primary_key = keys.get(0);
        return "DELETE FROM " + TABLE_NAME +
                " WHERE ID = '" + primary_key + "';";
    }

    @Override
    protected User convertResultSetToObject(ResultSet rs) throws SQLException {
        User user = new User();
        user.setID(rs.getString(1));
        user.setName(rs.getString(2));
        user.setPhoneNumber(rs.getString(3));
        user.setPassword(rs.getString(4));
        user.setField(rs.getString(5));
        user.setYear(rs.getInt(6));
        user.setRegisterDate(rs.getDate(7));
        return user;
    }

    @Override
    protected String getAllRows() {
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME ;
    }
}
