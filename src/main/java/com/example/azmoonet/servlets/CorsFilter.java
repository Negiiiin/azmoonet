package com.example.azmoonet.servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CorsFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
//        HttpServletRequest reqq = (HttpServletRequest) req;
//        String path = reqq.getServletPath();
//        long st = System.currentTimeMillis();
//        chain.doFilter(req, resp);
//        long et = System.currentTimeMillis();
//
//        long tt = (et - st);
//        System.out.println("Time take to execute action " + path + "   is  :  " + tt);
        HttpServletRequest request = (HttpServletRequest) req;
        System.out.println("HTTP method: " + request.getMethod());
        ((HttpServletResponse) resp).addHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) resp).addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");

//        if (request.getMethod().equals("OPTIONS")) {
//            System.out.println("options");
//            ((HttpServletResponse) resp).setStatus(HttpServletResponse.SC_ACCEPTED);
//            return;
//        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
