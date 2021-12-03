package com.example.azmoonet;

import com.example.azmoonet.database.DatabaseManager;
import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.quiz.QuizMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

@SpringBootApplication
public class Api {

	public static void main(String[] args) throws SQLException, IOException {
		SpringApplication.run(Api.class, args);
		DatabaseManager dbm = new DatabaseManager();
		dbm.createDatabases();
		Date date = new Date();
		System.out.println(date);
		Connection connection = ConnectionPool.getConnection();
		QuizMapper qm = new QuizMapper(false);
		ArrayList<String> quizID = new ArrayList<>();
		quizID.add("54496742-acb6-4b0e-a7ea-758bbb28db28");
//        System.out.println(qm.find(quizID).getQuestionIDs().get(1));
		connection.close();
	}
}
