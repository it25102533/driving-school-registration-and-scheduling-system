package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import jakarta.servlet.http.HttpSession;
import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentFeedbackDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.InstructorRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.StudentFeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentFeedbackController {

    private final StudentFeedbackService feedbackService;
    private final InstructorRepository instructorRepository;

    public StudentFeedbackController(StudentFeedbackService feedbackService, InstructorRepository instructorRepository) {
        this.feedbackService = feedbackService;
        this.instructorRepository = instructorRepository;
    }

    // FIXED: Endpoint changed to /feedback to cleanly match dashboard tiles and navigation links
    @GetMapping("/feedback")
    public String showFeedbackForm(HttpSession session, Model model) {
        Student loggedInStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (loggedInStudent == null) {
            return "redirect:/login";
        }

        model.addAttribute("feedbackDto", new StudentFeedbackDto());
        // Fetches all active instructors from MySQL to load into your template drop-down options
        model.addAttribute("instructors", instructorRepository.findAll());

        // FIXED: Returns "student/feedback" to perfectly align with your templates folder filename
        return "student/feedback";
    }

    // FIXED: Mapped to take Instructor feedback form submissions
    @PostMapping("/feedback/instructor")
    public String submitInstructorFeedback(@ModelAttribute("feedbackDto") StudentFeedbackDto dto, HttpSession session) {
        return processFeedbackSubmission(dto, session);
    }

    // FIXED: Mapped to take Course/Package feedback form submissions separately
    @PostMapping("/feedback/course")
    public String submitCourseFeedback(@ModelAttribute("feedbackDto") StudentFeedbackDto dto, HttpSession session) {
        return processFeedbackSubmission(dto, session);
    }

    // Reusable core submission processing routing module
    private String processFeedbackSubmission(StudentFeedbackDto dto, HttpSession session) {
        Student loggedInStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (loggedInStudent == null) {
            return "redirect:/login";
        }

        feedbackService.saveFeedback(loggedInStudent, dto);
        // FIXED: Redirects to ?sent to trigger the green alert message box in your frontend view template
        return "redirect:/student/feedback?sent";
    }
}