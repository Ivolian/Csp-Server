package com.withub.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String value = req.getRequestURI().replaceFirst(req.getContextPath(), "");
        if (value.startsWith("/admin")) {
            req.getRequestDispatcher("/admin.html").forward(req, resp);
        }
        if (value.startsWith("/index")) {
            req.getRequestDispatcher("/index.html").forward(req, resp);
        }
    }
}
