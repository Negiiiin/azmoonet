package com.example.azmoonet.restAPIs;

import com.example.azmoonet.database.mappers.ConnectionPool;
import com.example.azmoonet.database.mappers.user.UserMapper;
import com.example.azmoonet.logic.MD5;
import com.example.azmoonet.logic.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.azmoonet.logic.Error;

public class UserRestAPI {

//    @RequestMapping(value = "/signup", method = RequestMethod.POST)
//    public @ResponseBody
//    Object addUser(
//            @RequestParam(value = "name") String name,
//            @RequestParam(value = "fieldID") String fieldID,
//            @RequestParam(value = "year") Integer year,
//            @RequestParam(value = "password") String password,
//            @RequestParam(value = "phoneNumber") String phoneNumber) throws IOException, SQLException {
//        User user = new User();
//        user.setField(fieldID);
//        user.setName(name);
//        user.setID(UUID.randomUUID().toString());
//        user.setYear(year);
//        user.setPassword(password);
//        user.setPhoneNumber(phoneNumber);
//
//        UserMapper userMapper = new UserMapper(false);
//        Connection connection = ConnectionPool.getConnection();
//        User exists = userMapper.findForSignup(phoneNumber);
//        userMapper.insert(user);
//        connection.close();
//        Error error;
//        if(exists != null){
//            error = new Error(403, "already existed");
//        }
//        else {
//            error = new Error(201, "added successfully");
//        }
//        return error;
//    }
//
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public @ResponseBody
//    Object authenticate(@RequestParam(value = "phoneNumber") String phoneNumber,
//                        @RequestParam(value = "password") String password) throws SQLException {
//        MD5 hash = new MD5();
//        String hashPass = hash.getMd5(password);
//        UserMapper userMapper = new UserMapper(false);
//        Connection connection = ConnectionPool.getConnection();
//        User found = userMapper.findForLogin(phoneNumber,hashPass);
//        connection.close();
//        if(found!=null) {
//            String token="";
//            try {
//                Algorithm algorithm = Algorithm.HMAC256("َAzmoonet Azmoonet Azmoonet Azmoonet");
//                Date now = new Date();
//                Date expire = new Date();
//                long nowMillis = System.currentTimeMillis();
//                expire.setTime(nowMillis+600000);
//                token = JWT.create()
//                        .withIssuer("user")
//                        .withClaim("phoneNumber", phoneNumber)
//                        .withClaim("id", found.getID())
//                        .withIssuedAt(now)
//                        .withExpiresAt(expire)
//                        .sign(algorithm);
//                System.out.println(token);
//            } catch (JWTCreationException | UnsupportedEncodingException exception){
//                //Invalid Signing configuration / Couldn't convert Claims.
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return new Error(200, token);
//        }
//        return new Error(403, "no such user");
//    }

}
