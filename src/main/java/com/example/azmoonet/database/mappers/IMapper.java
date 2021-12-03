package com.example.azmoonet.database.mappers;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IMapper<T, I> {
    T find(ArrayList<I> keys) throws SQLException;
    void insert(T t) throws SQLException;
    void delete(ArrayList<I> keys) throws SQLException;
    public ArrayList<T> findByForeignKey(ArrayList<I> keys) throws SQLException;
}

