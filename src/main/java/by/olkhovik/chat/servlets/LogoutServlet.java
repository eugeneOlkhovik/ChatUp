package by.olkhovik.chat.servlets;

import by.olkhovik.chat.storage.UserStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 30.05.2016.
 */

@WebServlet(value = "/logout")
public class LogoutServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");

        UserStorage.deleteUser(username);
        Cookie cookie = new Cookie("userId", "");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);

        resp.sendRedirect("/login.html");
    }
}
