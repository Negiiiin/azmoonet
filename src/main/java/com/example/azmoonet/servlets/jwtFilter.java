package com.example.azmoonet.servlets;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.azmoonet.database.mappers.user.UserMapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

@WebFilter(filterName = "jwtFilter")
public class jwtFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) resp;
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        String jwtString = null;
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = httpRequest.getHeader(name);
                if(name.equals("authorization"))
                    jwtString = value;
                System.out.println("Header: " + name + value);
            }
        }
        try {
            System.out.println("------------------------"+jwtString+"--------------------------");
            if(jwtString==null){
                httpResponse.setStatus(401);
                System.out.println("--------------------------------------------401");
            }
            else {
                Algorithm algorithm = Algorithm.HMAC256("َAzmoonet Azmoonet Azmoonet Azmoonet");
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT jwt = verifier.verify(jwtString);
                ArrayList<String> ID = new ArrayList<>();
                ID.add(jwt.getClaim("id").asString());
                UserMapper userMapper = new UserMapper(false);
                System.out.println("--------------------------------------------******");
                System.out.println(userMapper.find(ID).getID() == jwt.getClaim("id").asString());
                System.out.println("--------------------------------------------!!!!!!!");
                System.out.println(userMapper.find(ID).getPhoneNumber());

                System.out.println("--------------------------------------------******");
                System.out.println(jwt.getClaim("iss").asString());

                if (!(userMapper.find(ID).getID().equals(jwt.getClaim("id").asString())
                        && jwt.getClaim("phoneNumber").asString().equals(userMapper.find(ID).getPhoneNumber())
                        && jwt.getClaim("iss").asString().equals("user"))) {
                    httpResponse.setStatus(403);
                    System.out.println("--------------------------------------------403");
                }
            }
        }
        catch (JWTVerificationException | SQLException exception){
            exception.printStackTrace();
            httpResponse.setStatus(401);
            System.out.println("--------------------------------------------401");

        }
//
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}