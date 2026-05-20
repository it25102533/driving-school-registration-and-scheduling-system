package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.InstructorDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Instructor;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.InstructorService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.LessonService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.LessonPackageService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.StudentFeedbackService;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/instructor")
public class InstructorPortalController {

    private static final String T = "instructor/";
    private final InstructorService instructorService;
    private final LessonService lessonService;
    private final StudentFeedbackService feedbackService;
    private final LessonPackageService lessonPackageService;

    public InstructorPortalController(InstructorService instructorService,
                                      LessonService lessonService,
                                      StudentFeedbackService feedbackService,
                                      LessonPackageService lessonPackageService) {
        this.instructorService = instructorService;
        this.lessonService = lessonService;
        this.feedbackService = feedbackService;
        this.lessonPackageService = lessonPackageService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Instructor loggedIn = getLoggedInInstructor(session);
        InstructorDto instructorDto = instructorService.getInstructorById(loggedIn.getId());
        model.addAttribute("instructor", instructorDto);
        model.addAttribute("instructorFeedback", feedbackService.getInstructorFeedbackForStaff());
        model.addAttribute("courseFeedback", feedbackService.getCourseFeedbackForStaff());
        return T + "dashboard";
    }

    @PostMapping("/feedback/delete")
    public String deleteFeedback(@RequestParam Long id) {
        feedbackService.deleteFeedback(id);
        return "redirect:/instructor/dashboard?feedbackDeleted";
    }

    @GetMapping("/packages")
    public String managePackages(HttpSession session, Model model) {
        getLoggedInInstructor(session);
        model.addAttribute("packages", lessonPackageService.getAllPackages());
        return T + "manage-packages";
    }

    @PostMapping("/packages/update")
    public String updatePackagePrices(@RequestParam("packageId") List<Long> packageId,
                                      @RequestParam("basePrice") List<Double> basePrice) {
        lessonPackageService.updatePrices(packageId, basePrice);
        return "redirect:/instructor/packages?saved";
    }

    @GetMapping("/my-lessons")
    public String showMyLessons(HttpSession session, Model model) {
        Instructor loggedIn = getLoggedInInstructor(session);
        model.addAttribute("lessons", lessonService.getInstructorLessons(loggedIn.getId()));
        return T + "my-lessons";
    }

    /**
     * Deletes the currently logged-in instructor account (and their lesson rows), then signs out.
     */
    @PostMapping("/account/delete")
    public String deleteOwnAccount(HttpSession session) {
        Instructor loggedIn = getLoggedInInstructor(session);
        if (loggedIn == null || loggedIn.getId() == null) {
            return "redirect:/login/professional";
        }
        instructorService.deleteInstructor(loggedIn.getId());
        session.removeAttribute(ProfessionalAuthController.SESSION_INSTRUCTOR);
        session.invalidate();
        return "redirect:/login/professional?accountDeleted";
    }

    private Instructor getLoggedInInstructor(HttpSession session) {
        return (Instructor) session.getAttribute(ProfessionalAuthController.SESSION_INSTRUCTOR);
    }
}