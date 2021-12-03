package com.example.azmoonet.database.mappers.progress;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.Mapper;
import com.example.azmoonet.logic.Progress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgressMapper extends Mapper<Progress, String> implements IProgressMapper {

    private static final String COLUMNS = " ID, learned, userID, subjectID";
    private static final String TABLE_NAME = "progress_table";

    private Boolean doManage;

    public ProgressMapper(Boolean doManage) throws SQLException {
        if (this.doManage = doManage) {
            Connection con = ConnectionPool.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
            st.executeUpdate(String.format(
                    "CREATE TABLE  %s " +
                            "(" +
                            "ID VARCHAR(40) NOT NULL , " +
                            "learned INTEGER ," +
                            "userID VARCHAR(40)," +
                            "subjectID VARCHAR(40),"+
                            "FOREIGN KEY(userID) references user_table(ID) on delete  cascade, " +
                            "FOREIGN KEY(subjectID) references subject_table(ID) on delete  cascade, " +
                            "PRIMARY KEY(ID)" +
                            ");",
                    TABLE_NAME));
            st.close();
            con.close();
        }
    }

    private List<Progress> executingGivenQuery(String statement) throws SQLException {
        List<Progress> result = new ArrayList<Progress>();
        try (Connection con = ConnectionPool.getConnection()) {
            try (PreparedStatement st = con.prepareStatement(statement)) {
                try {
                    ResultSet resultSet = st.executeQuery();
                    while (resultSet.next())
                        result.add(convertResultSetToObject(resultSet));
                    return result;
                } catch (SQLException ex) {
                    System.out.println("error in Mapper.findByID 444 query.");
                    throw ex;
                }
            }
        }
    }

    public List<Progress> searchProgressByUserID(String inName) throws SQLException {
        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
                " WHERE userID = '" + inName + "';";
        return executingGivenQuery(statement);
    }

    public List<Progress> searchProgressBySubjectID(String inName) throws SQLException {
        String statement = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +
                " WHERE subjectID = '" + inName + "';";
        return executingGivenQuery(statement);
    }

    @Override
    protected String getFindStatement(ArrayList<String> keys) {
        String ID = keys.get(0);
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME +
                " WHERE ID = " + "'" + ID + "'" +";";
    }

    @Override
    protected String getInsertStatement(Progress progress) {
        return "INSERT IGNORE INTO " + TABLE_NAME +
                "(" + COLUMNS + ")" + " VALUES "+
                "( "+
                "'" + progress.getID()+ "'," +
                "'" + progress.getLearned()+ "'," +
                "'" + progress.getUserID()+ "'," +
                "'" + progress.getSubjectID()+ "'" +
                ");";
    }

    @Override
    protected String getDeleteStatement(ArrayList<String> keys) {
        String primary_key = keys.get(0);
        return "DELETE FROM " + TABLE_NAME +
                " WHERE ID = '" + primary_key + "';";
    }

    @Override
    protected Progress convertResultSetToObject(ResultSet rs) throws SQLException {
        Progress progress = new Progress();
        progress.setID(rs.getString(1));
        progress.setLearned(rs.getDouble(2));
        progress.setUserID(rs.getString(3));
        progress.setSubjectID(rs.getString(4));
        return progress;
    }

    @Override
    protected String getAllRows() {
        return "SELECT " + COLUMNS +
                " FROM " + TABLE_NAME ;
    }
}
