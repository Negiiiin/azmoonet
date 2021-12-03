package com.example.azmoonet.database;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.course.CourseMapper;
import com.example.azmoonet.database.mappers.field.FieldMapper;
import com.example.azmoonet.database.mappers.lesson.LessonMapper;
import com.example.azmoonet.database.mappers.progress.ProgressMapper;
import com.example.azmoonet.database.mappers.question.QuestionMapper;
import com.example.azmoonet.database.mappers.quiz.QuizMapper;
import com.example.azmoonet.database.mappers.subject.SubjectMapper;
import com.example.azmoonet.database.mappers.user.UserMapper;
import com.example.azmoonet.database.mappers.usersLesson.UsersLessonMapper;
import com.example.azmoonet.logic.Course;
import com.example.azmoonet.logic.Field;
import com.example.azmoonet.logic.Subject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;

    public static DatabaseManager getInstance() throws IOException {
        if (instance == null)
            instance = new DatabaseManager();
        return instance;
    }

    public boolean existInDatabase(String tableName) throws SQLException {
        boolean result = false;
        Connection connection = ConnectionPool.getConnection();
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables("azmoonet", null, "%", null);
        while (rs.next()) {
            if(rs.getString(3).equals(tableName)) {
                result = true;
                break;
            }
        }
        connection.close();
        return result;
    }

    public void createDatabases() throws SQLException, IOException {
        System.out.println("IN DATABASE MANAGER!!!");
        boolean courseDoManage, fieldDoManage, lessonDoManage, progressDoManage, questionDoManage, quizDoManage, subjectDoManage, userDoManage, usersLessonDoManage;
        courseDoManage = !(existInDatabase("course_table"));
        fieldDoManage = !(existInDatabase("field_table"));
        lessonDoManage = !(existInDatabase("lesson_table"));
        progressDoManage = !(existInDatabase("progress_table"));
        questionDoManage = !(existInDatabase("question_table"));
        quizDoManage = !(existInDatabase("quiz_table"));
        subjectDoManage = !(existInDatabase("subject_table"));
        userDoManage = !(existInDatabase("user_table"));
        usersLessonDoManage = !(existInDatabase("usersLesson_table"));

        FieldMapper fieldMapper = new FieldMapper(fieldDoManage);
        UserMapper userMapper = new UserMapper(userDoManage);
        CourseMapper courseMapper = new CourseMapper(courseDoManage);
        SubjectMapper subjectMapper = new SubjectMapper(subjectDoManage);
        LessonMapper lessonMapper = new LessonMapper(lessonDoManage);
        ProgressMapper progressMapper = new ProgressMapper(progressDoManage);
        QuestionMapper questionMapper = new QuestionMapper(questionDoManage);
        QuizMapper quizMapper = new QuizMapper(quizDoManage);
        UsersLessonMapper usersLessonMapper = new UsersLessonMapper(usersLessonDoManage);

        Connection connection = ConnectionPool.getConnection();
        Field field = new Field();
        field.setID("Tajrobi");
        field.setName("Tajrobi");
        fieldMapper.insert(field);

        Course course = new Course();
        course.setID("ph");
        course.setName("Physics");
        course.setFieldID("Tajrobi");
        courseMapper.insert(course);

        String[] subjectIDs = {"ph01", "ph02", "ph03", "ph04", "ph05", "ph06", "ph07", "ph08", "ph09", "ph10", "ph11", "ph12", "ph13", "ph14", "ph15"};
        for(String id : subjectIDs){
            Subject subject = new Subject();
            subject.setID(id);
            subject.setName(id);
            subject.setImportance(1);
            subject.setCourseID("ph");
            subjectMapper.insert(subject);
        }

        DatasetManager datasetManager = new DatasetManager();
        datasetManager.insertQuestions();

//        um.insert(currentUser);
        System.out.println("HEREE");
        connection.close();
    }
}
