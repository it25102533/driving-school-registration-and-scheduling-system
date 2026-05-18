package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.InstructorLoginDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Instructor;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.InstructorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ProfessionalAuthController {

    public static final String SESSION_INSTRUCTOR = "SESSION_INSTRUCTOR";

    private final InstructorService instructorService;

    public ProfessionalAuthController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/login/professional")
    public String showLoginPage(@RequestParam(required = false) String required, Model model) {
        if (required != null) {
            model.addAttribute("requiredLogin", true);
        }
        return "login-professional";
    }

    @PostMapping("/login/professional")
    public String processLogin(InstructorLoginDto loginDto, HttpSession session, Model model) {
        Optional<Instructor> authenticated = instructorService.authenticate(loginDto);

        if (authenticated.isPresent()) {
            session.setAttribute(SESSION_INSTRUCTOR, authenticated.get());
            return "redirect:/instructor/dashboard";
        }

        model.addAttribute("error", "Invalid email or password.");
        return "login-professional";
    }

    @GetMapping("/logout/professional")
    public String logout(HttpSession session) {
        session.removeAttribute(SESSION_INSTRUCTOR);
        return "redirect:/login/professional?logout";
    }
}
