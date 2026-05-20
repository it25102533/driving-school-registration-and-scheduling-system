package lk.ac.sliit.drivingschool.drivingschoolsystem.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.ac.sliit.drivingschool.drivingschoolsystem.controller.ProfessionalAuthController;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class InstructorAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute(ProfessionalAuthController.SESSION_INSTRUCTOR) != null) {
            return true;
        }

        response.sendRedirect(request.getContextPath() + "/login/professional?required=true");
        return false;
    }
}