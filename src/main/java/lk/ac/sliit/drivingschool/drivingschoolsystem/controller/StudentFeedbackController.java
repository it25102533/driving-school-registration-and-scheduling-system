package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import jakarta.servlet.http.HttpSession;
import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentFeedbackDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.InstructorRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.LessonPackageService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.StudentFeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentFeedbackController {

    private final StudentFeedbackService feedbackService;
    private final InstructorRepository instructorRepository;
    private final LessonPackageService lessonPackageService;

    public StudentFeedbackController(StudentFeedbackService feedbackService,
                                     InstructorRepository instructorRepository,
                                     LessonPackageService lessonPackageService) {
        this.feedbackService = feedbackService;
        this.instructorRepository = instructorRepository;
        this.lessonPackageService = lessonPackageService;
    }

    @GetMapping("/feedback")
    public String showFeedbackForm(HttpSession session, Model model) {
        Student loggedInStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (loggedInStudent == null) {
            return "redirect:/login";
        }

        model.addAttribute("feedbackDto", new StudentFeedbackDto());
        model.addAttribute("instructors", instructorRepository.findAll());
        model.addAttribute("packages", lessonPackageService.getAllPackages());

        return "student/feedback";
    }

    @PostMapping("/feedback/instructor")
    public String submitInstructorFeedback(@RequestParam Long instructorId,
                                           @RequestParam int rating,
                                           @RequestParam(required = false) String comments,
                                           HttpSession session) {
        StudentFeedbackDto dto = new StudentFeedbackDto();
        dto.setInstructorId(instructorId);
        dto.setRating(rating);
        dto.setComments(comments);
        return processFeedbackSubmission(dto, session);
    }

    @PostMapping("/feedback/course")
    public String submitCourseFeedback(@RequestParam String courseName,
                                       @RequestParam int rating,
                                       @RequestParam(required = false) String comments,
                                       HttpSession session) {
        StudentFeedbackDto dto = new StudentFeedbackDto();
        dto.setCourseName(courseName);
        dto.setRating(rating);
        dto.setComments(comments);
        return processFeedbackSubmission(dto, session);
    }

    private String processFeedbackSubmission(StudentFeedbackDto dto, HttpSession session) {
        Student loggedInStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (loggedInStudent == null) {
            return "redirect:/login";
        }

        feedbackService.saveFeedback(loggedInStudent, dto);
        return "redirect:/student/feedback?sent";
    }
}