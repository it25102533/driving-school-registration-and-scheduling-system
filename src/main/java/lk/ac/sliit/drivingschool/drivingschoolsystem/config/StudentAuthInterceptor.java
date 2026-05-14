package lk.ac.sliit.drivingschool.drivingschoolsystem.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class StudentAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("SESSION_STUDENT") != null) {
            return true;
        }

        // Redirect to login if session is missing or invalid
        response.sendRedirect(request.getContextPath() + "/login?required=true");
        return false;
    }
}