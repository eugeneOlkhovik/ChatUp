package by.olkhovik.chat;

import by.olkhovik.chat.storage.UserStorage;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 27.05.2016.
 */

@WebFilter(value = "/homepage.html")
public class Filter implements javax.servlet.Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String userId = "";

        Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("userId")){
                userId = cookie.getValue();
            }
        }

        boolean isAuth = isAuthenticated(userId);

        if(isAuth){
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            if(servletResponse instanceof HttpServletResponse){
                ((HttpServletResponse) servletResponse).sendRedirect("/login.html");
            }else{
                servletResponse.getOutputStream().println("403, Forbidden");
            }
        }
    }

    @Override
    public void destroy() {

    }

    public boolean isAuthenticated(String id){
        return UserStorage.getName(id) != null;
    }
}
