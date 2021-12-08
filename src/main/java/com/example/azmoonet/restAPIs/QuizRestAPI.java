package com.example.azmoonet.restAPIs;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.course.CourseMapper;
import com.example.azmoonet.database.mappers.lesson.LessonMapper;
import com.example.azmoonet.database.mappers.question.QuestionMapper;
import com.example.azmoonet.database.mappers.quiz.QuizMapper;
import com.example.azmoonet.database.mappers.subject.SubjectMapper;
import com.example.azmoonet.database.mappers.user.UserMapper;
import com.example.azmoonet.database.mappers.usersLesson.UsersLessonMapper;
import com.example.azmoonet.logic.*;
import com.example.azmoonet.logic.Error;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class QuizRestAPI {

//    private Quiz quiz;

    @CrossOrigin(origins = {"http://194.62.43.26:8081", "http://localhost:8081"})
    @RequestMapping(value = "/quiz/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Quiz produceNewQuiz(@RequestHeader Map<String, String> headers,
                                  @PathVariable(value = "id") String id) throws SQLException {
        System.out.println("IN GET PRODUCE QUIZ FUNCTION");

        String userID = null;
        UserMapper userMapper = new UserMapper(false);
        try {
            Algorithm algorithm = Algorithm.HMAC256("َAzmoonet Azmoonet Azmoonet Azmoonet");
            JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(headers.get("authorization"));
            ArrayList<String> ID = new ArrayList<>();
            ID.add(jwt.getClaim("id").asString());
            if(!(userMapper.find(ID).getID().equals(jwt.getClaim("id").asString())
                    && jwt.getClaim("phoneNumber").asString().equals(userMapper.find(ID).getPhoneNumber())
                    && jwt.getClaim("iss").asString().equals("user"))){
                Error error=new Error(300,"error in jwt");
                return null;

            }
            userID = jwt.getClaim("id").asString();
        } catch (JWTVerificationException | UnsupportedEncodingException exception){
            exception.printStackTrace();
            Error error=new Error(300,"error in jwt");
            return null;
        }

        System.out.println("AFTER JWT");

        Quiz quiz = new Quiz();

        quiz.setID(UUID.randomUUID().toString());
        Date date = new Date();
        java.sql.Date sDate = new java.sql.Date(date.getTime());
        quiz.setDate(sDate);

        List<Question> questions = quiz.getQuestion(id);

        quiz.setUserID(userID);

        quiz.setRightAnswers(quiz.getRightAnswers(questions));
        quiz.setQuestionIDs(quiz.getQuestionID(questions));


        Connection connection = ConnectionPool.getConnection();
        QuizMapper quizMapper = new QuizMapper(false);
        quizMapper.insert(quiz);

        ArrayList<String> quizID = new ArrayList<>();
        quizID.add(id);
        List<Quiz> quiz2 = quizMapper.convertAllResultToObject();
        for (Quiz q: quiz2) {
            System.out.println(q.getID());
        }

        connection.close();

        return quiz;
    }



    @RequestMapping(value = "/quiz/getQuestions/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Question> getQuestions(@RequestHeader Map<String, String> headers,
                        @PathVariable(value = "id") String id) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        QuizMapper quizMapper = new QuizMapper(false);
        ArrayList<String> quizID = new ArrayList<>();
        quizID.add(id);
        Quiz quiz = quizMapper.find(quizID);
        connection.close();

        List<Question> questions = quiz.getQuestion(id);

        return questions;
    }




    @CrossOrigin(origins = {"http://194.62.43.26:8081", "http://localhost:8081"})
    @RequestMapping(value = "/quiz/answers", method = RequestMethod.POST)
    public @ResponseBody
    void getUserAnswers(@RequestParam(value = "answers") String answers,
                        @RequestParam(value = "id") String id) throws IOException, InterruptedException, SQLException {
        Connection connection = ConnectionPool.getConnection();
        QuizMapper quizMapper = new QuizMapper(false);
        ArrayList<String> quizID = new ArrayList<>();
        quizID.add(id);
        Quiz quiz = quizMapper.find(quizID);

        ArrayList<Integer> intAnswers = new ArrayList<>();
        for (int i = 0; i < answers.length(); i++) {
            intAnswers.add(Integer.parseInt(String.valueOf(answers.charAt(i))));
        }
        quiz.setAnswers(intAnswers);
        Date date = new Date();
        java.sql.Date sDate = new java.sql.Date(date.getTime());
        quiz.setDate(sDate);

        System.out.println("AFTER SET ANSWERS");

        System.out.println("************************");
        System.out.println(quizMapper.convertAllResultToObject().get(0).getAnswers().get(3));

        connection.close();

        List<Progress> progresses = createProgress(quiz);
    }



    @CrossOrigin(origins = {"http://194.62.43.26:8081", "http://localhost:8081"})
    @RequestMapping(value = "/user/profile/subject/{courseID}/{subjectID}", method = RequestMethod.GET)
    public @ResponseBody
    Quiz produceQuizFromGivenTopic(
            @RequestHeader Map<String, String> headers,
            @PathVariable(value = "courseID") String id,
            @PathVariable(value = "subjectID") String subjectID) throws SQLException {

            String userID = null;
            UserMapper userMapper = new UserMapper(false);
            try {
                Algorithm algorithm = Algorithm.HMAC256("َAzmoonet Azmoonet Azmoonet Azmoonet");
                JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(headers.get("authorization"));
                ArrayList<String> ID = new ArrayList<>();
                ID.add(jwt.getClaim("id").asString());

                if(!(userMapper.find(ID).getID().equals(jwt.getClaim("id").asString())
                        && jwt.getClaim("phoneNumber").asString().equals(userMapper.find(ID).getPhoneNumber())
                        && jwt.getClaim("iss").asString().equals("user"))){
                    Error error=new Error(300,"error in jwt");
                    return null;

                }
                userID = jwt.getClaim("id").asString();
            } catch (JWTVerificationException | UnsupportedEncodingException exception){
                exception.printStackTrace();
                Error error=new Error(300,"error in jwt");
                return null;
            }

            Quiz quiz = new Quiz();

            quiz.setID(UUID.randomUUID().toString());
            Date date = new Date();
            java.sql.Date sDate = new java.sql.Date(date.getTime());
            quiz.setDate(sDate);

            List<Question> questions = quiz.getQuestionFromGivenSubject(id, subjectID);

            quiz.setUserID(userID);

            quiz.setRightAnswers(quiz.getRightAnswers(questions));
            quiz.setQuestionIDs(quiz.getQuestionID(questions));

            Connection connection = ConnectionPool.getConnection();
            QuizMapper quizMapper = new QuizMapper(false);
            quizMapper.insert(quiz);
            connection.close();

            return quiz;
    }



    @CrossOrigin(origins = {"http://194.62.43.26:8081", "http://localhost:8081"})
    @RequestMapping(value = "user/profile/quiz/answers", method = RequestMethod.POST)
    public @ResponseBody
    void getUserAnswersForQuizFromGivenTopic(@RequestParam(value = "answers") String answers,
                                             @RequestParam(value = "id") String id) throws IOException, InterruptedException, SQLException {
        ArrayList<Integer> intAnswers = new ArrayList<>();
        for (int i = 0; i < answers.length(); i++) {
            intAnswers.add(Integer.parseInt(String.valueOf(answers.charAt(i))));
        }

        Connection connection = ConnectionPool.getConnection();
        QuizMapper quizMapper = new QuizMapper(false);
        ArrayList<String> quizID = new ArrayList<>();
        quizID.add(id);
        Quiz quiz = quizMapper.find(quizID);

        quiz.setAnswers(intAnswers);
        Date date = new Date();
        java.sql.Date sDate = new java.sql.Date(date.getTime());
        quiz.setDate(sDate);

        connection.close();

        List<Progress> progresses = createProgress(quiz);
    }


    @CrossOrigin(origins = {"http://194.62.43.26:8081", "http://localhost:8081"})
    @RequestMapping(value = "/user/signup", method = RequestMethod.POST)
    public @ResponseBody
    Object addUser(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "fieldID") String fieldID,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "phoneNumber") String phoneNumber) throws IOException, SQLException {
        User user = new User();
        user.setField(fieldID);
        user.setName(name);
        user.setID(UUID.randomUUID().toString());
        user.setYear(year);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        Date date = new Date();
        java.sql.Date sDate = new java.sql.Date(date.getTime());
        user.setRegisterDate(sDate);

        UserMapper userMapper = new UserMapper(false);
        User exists = userMapper.findForSignup(phoneNumber);
        userMapper.insert(user);
        Error error;
        if(exists != null){
            error = new Error(403, "already existed");
        }
        else {
            error = new Error(201, "added successfully");
        }
        return error;
    }



    @CrossOrigin(origins = {"http://194.62.43.26:8081", "http://localhost:8081"})
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public @ResponseBody
    Object authenticate(@RequestParam(value = "phoneNumber") String phoneNumber,
                        @RequestParam(value = "password") String password) throws SQLException {
//        MD5 hash = new MD5();
//        String hashPass = hash.getMd5(password);
        UserMapper userMapper = new UserMapper(false);
        Connection connection = ConnectionPool.getConnection();
        User found = userMapper.findForLogin(phoneNumber,password);
        connection.close();
        if(found!=null) {
            String token="";
            try {
                Algorithm algorithm = Algorithm.HMAC256("َAzmoonet Azmoonet Azmoonet Azmoonet");
                Date now = new Date();
                Date expire = new Date();
                long nowMillis = System.currentTimeMillis();
                long expireTime = 999999999;
                expire.setTime(nowMillis+ 999999999);
                token = JWT.create()
                        .withIssuer("user")
                        .withClaim("phoneNumber", phoneNumber)
                        .withClaim("id", found.getID())
                        .withIssuedAt(now)
                        .withExpiresAt(expire)
                        .sign(algorithm);
                System.out.println(token);
            } catch (JWTCreationException | UnsupportedEncodingException exception){
                //Invalid Signing configuration / Couldn't convert Claims.
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Error(200, token);
        }
        return new Error(403, "no such user");
    }

    public List<Progress> createProgress(Quiz quiz) throws SQLException {
        Map<String, List<Question>> questionsWithSubjectsIDs = new HashMap<String, List<Question>>();
        List<Progress> progresses = new ArrayList<Progress>();

        List<Integer> answers = quiz.getAnswers();
        List<Integer> rightAnswers = quiz.getRightAnswers();
        QuestionMapper questionMapper = new QuestionMapper(false);

        for (int i = 0; i < quiz.getQuestionIDs().size(); i++) {

            ArrayList<String> keys = new ArrayList<String>();
            keys.add(quiz.getQuestionIDs().get(i));
            Question question = questionMapper.find(keys);

            if (questionsWithSubjectsIDs.containsKey(question.getSubjectID())) {
                questionsWithSubjectsIDs.get(question.getSubjectID()).add(question);

                for(Progress p: progresses) {
                    if (p.getSubjectID() == question.getSubjectID()) {
                        if (answers.get(i) == rightAnswers.get(i))
                            p.setLearned(p.getLearned() + 1.0);
                    }
                }
            }
            else {
                List<Question> temp = new ArrayList<Question>();
                temp.add(question);
                questionsWithSubjectsIDs.put(question.getSubjectID(), temp);

                Progress progress = new Progress();
                progress.setID(UUID.randomUUID().toString());
                progress.setUserID(quiz.getUserID());
                progress.setSubjectID(question.getSubjectID());

                if (answers.get(i) == rightAnswers.get(i))
                    progress.setLearned(1.0);
                else
                    progress.setLearned(0.0);
                progresses.add(progress);
            }
        }

        return progresses;
    }



    @CrossOrigin(origins = {"http://194.62.43.26:8081", "http://localhost:8081"})
    @RequestMapping(value = "/user/profile/{paidStatus}", method = RequestMethod.GET)
    public @ResponseBody
    List<Subject> getProfile(@RequestHeader Map<String, String> headers,
                             @PathVariable(value = "paidStatus") String paidStatus) throws SQLException {
        //paidStatus = paid | notPaid

        String userID = null;
        UserMapper userMapper = new UserMapper(false);
        try {
            Algorithm algorithm = Algorithm.HMAC256("َAzmoonet Azmoonet Azmoonet Azmoonet");
            JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(headers.get("authorization"));
            ArrayList<String> ID = new ArrayList<>();
            ID.add(jwt.getClaim("id").asString());
            if(!(userMapper.find(ID).getID().equals(jwt.getClaim("id").asString())
                    && jwt.getClaim("phoneNumber").asString().equals(userMapper.find(ID).getPhoneNumber())
                    && jwt.getClaim("iss").asString().equals("user"))){
                Error error=new Error(300,"error in jwt");
                return null;

            }
            userID = jwt.getClaim("id").asString();
        } catch (JWTVerificationException | UnsupportedEncodingException exception){
            exception.printStackTrace();
            Error error=new Error(300,"error in jwt");
            return null;
        }

        System.out.println("AFTER JWT");

        UsersLessonMapper usersLessonMapper = new UsersLessonMapper(false);
        SubjectMapper subjectMapper = new SubjectMapper(false);
        Connection connection = ConnectionPool.getConnection();

        List<UsersLesson> usersLessons = new ArrayList<UsersLesson>();
        usersLessons = usersLessonMapper.findByUserID(userID);

        List<Subject> userSubjects = new ArrayList<Subject>();
        for (UsersLesson ul:usersLessons){
            if(Objects.equals(paidStatus, "paid") && ul.getIsPaid() == true){
                ArrayList<String> keys = new ArrayList<String>();
                keys.add(ul.getSubjectID());
                userSubjects.add(subjectMapper.find(keys));

            }
            else if(Objects.equals(paidStatus, "notPaid") && ul.getIsPaid() == false){
                ArrayList<String> keys = new ArrayList<String>();
                keys.add(ul.getSubjectID());
                userSubjects.add(subjectMapper.find(keys));
            }
        }
        connection.close();

        return userSubjects;
    }

    @CrossOrigin(origins = {"http://194.62.43.26:8081", "http://localhost:8081"})
    @RequestMapping(value = "/user/profile/subject/{subjectID}", method = RequestMethod.GET)
    public @ResponseBody
    List<Lesson> getLessons(@RequestHeader Map<String, String> headers,
                            @PathVariable(value = "subjectID") String subjectID) throws SQLException {
        //paidStatus = paid | notPaid

        String userID = null;
        UserMapper userMapper = new UserMapper(false);
        try {
            Algorithm algorithm = Algorithm.HMAC256("َAzmoonet Azmoonet Azmoonet Azmoonet");
            JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(headers.get("authorization"));
            ArrayList<String> ID = new ArrayList<>();
            ID.add(jwt.getClaim("id").asString());
            if(!(userMapper.find(ID).getID().equals(jwt.getClaim("id").asString())
                    && jwt.getClaim("phoneNumber").asString().equals(userMapper.find(ID).getPhoneNumber())
                    && jwt.getClaim("iss").asString().equals("user"))){
                Error error=new Error(300,"error in jwt");
                return null;

            }
            userID = jwt.getClaim("id").asString();
        } catch (JWTVerificationException | UnsupportedEncodingException exception){
            exception.printStackTrace();
            Error error=new Error(300,"error in jwt");
            return null;
        }

        System.out.println("AFTER JWT");

        UsersLessonMapper usersLessonMapper = new UsersLessonMapper(false);
        LessonMapper lessonMapper = new LessonMapper(false);
        Connection connection = ConnectionPool.getConnection();

        boolean isValid = usersLessonMapper.checkIfValid(userID, subjectID);
        List<Lesson> result = new ArrayList<Lesson>();
        if (isValid) {
            result = lessonMapper.searchLessonBySubjectID(subjectID);
        }

        connection.close();

        return result;
    }

    @CrossOrigin(origins = {"http://194.62.43.26:8081", "http://localhost:8081"})
    @RequestMapping(value = "/user/profile/lesson/{lessonID}", method = RequestMethod.GET)
    public @ResponseBody
    Lesson getLessonContent(@RequestHeader Map<String, String> headers,
                            @PathVariable(value = "lessonID") String lessonID) throws SQLException {
        //paidStatus = paid | notPaid

        String userID = null;
        UserMapper userMapper = new UserMapper(false);
        try {
            Algorithm algorithm = Algorithm.HMAC256("َAzmoonet Azmoonet Azmoonet Azmoonet");
            JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(headers.get("authorization"));
            ArrayList<String> ID = new ArrayList<>();
            ID.add(jwt.getClaim("id").asString());
            if(!(userMapper.find(ID).getID().equals(jwt.getClaim("id").asString())
                    && jwt.getClaim("phoneNumber").asString().equals(userMapper.find(ID).getPhoneNumber())
                    && jwt.getClaim("iss").asString().equals("user"))){
                Error error=new Error(300,"error in jwt");
                return null;

            }
            userID = jwt.getClaim("id").asString();
        } catch (JWTVerificationException | UnsupportedEncodingException exception){
            exception.printStackTrace();
            Error error=new Error(300,"error in jwt");
            return null;
        }

        System.out.println("AFTER JWT");

        UsersLessonMapper usersLessonMapper = new UsersLessonMapper(false);
        LessonMapper lessonMapper = new LessonMapper(false);
        Connection connection = ConnectionPool.getConnection();

        ArrayList<String> keys = new ArrayList<String>();
        keys.add(lessonID);
        Lesson lesson = lessonMapper.find(keys);
        boolean isValid = usersLessonMapper.checkIfValid(userID, lesson.getSubjectID());

        if (!isValid) {
            lesson = null;
        }

        connection.close();

        return lesson;
    }



}