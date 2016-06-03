package by.olkhovik.chat.servlets;


import by.olkhovik.chat.storage.UserStorage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 27.05.2016.
 */

@WebServlet(value = "/signup", initParams = {
        @WebInitParam(name = "cookie-live-time", value = "500")
})
public class SignUpServlet extends HttpServlet {

    private int cookieLiveTime = -1;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        cookieLiveTime = Integer.parseInt(servletConfig.getInitParameter("cookie-live-time"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");

        if(username == null || username.isEmpty()){
            resp.sendRedirect("/login.html");
            return;
        }

        if(UserStorage.addUser(username)){

            String userId = UserStorage.getId(username);
            if(userId == null){
                resp.sendRedirect("/login.html");
                return;
            }

            Cookie cookie = new Cookie("userId", userId);
            cookie.setMaxAge(cookieLiveTime);
            resp.addCookie(cookie);

            resp.sendRedirect("/homepage.html");

        }else{
            resp.sendRedirect("/login.html");

        }
    }
}
