package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.LessonDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.LessonService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.InstructorRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.VehicleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class LessonController {

    private final LessonService lessonService;
    private final InstructorRepository instructorRepository;
    private final VehicleRepository vehicleRepository;

    public LessonController(LessonService lessonService, InstructorRepository instructorRepository, VehicleRepository vehicleRepository) {
        this.lessonService = lessonService;
        this.instructorRepository = instructorRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping("/book-lesson")
    public String showBookingForm(Model model) {
        model.addAttribute("lesson", new LessonDto());
        // Pass instructors and vehicles to the view for the dropdown menus
        model.addAttribute("instructors", instructorRepository.findAll());
        model.addAttribute("vehicles", vehicleRepository.findAll());
        return "lesson/book-lesson";
    }

    @PostMapping("/book-lesson/save")
    public String saveBooking(@ModelAttribute("lesson") LessonDto dto, HttpSession session, Model model) {
        // Automatically link the lesson to the logged-in student from the active session
        Student student = (Student) session.getAttribute("SESSION_STUDENT");
        dto.setStudentId(student.getId());

        try {
            lessonService.bookLesson(dto);
            return "redirect:/student/my-lessons";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("lesson", dto);
            model.addAttribute("instructors", instructorRepository.findAll());
            model.addAttribute("vehicles", vehicleRepository.findAll());
            return "lesson/book-lesson";
        }
    }

    @GetMapping("/my-lessons")
    public String viewMyLessons(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("SESSION_STUDENT");
        model.addAttribute("lessons", lessonService.getStudentLessons(student.getId()));
        return "lesson/my-lessons";
    }

    @GetMapping("/cancel-lesson")
    public String cancelLesson(@RequestParam Long id) {
        lessonService.cancelLesson(id);
        return "redirect:/student/my-lessons";
    }

    @GetMapping("/schedule-lesson")
    public String showScheduleForm(@RequestParam Long id, Model model) {
        LessonDto dto = lessonService.getLessonById(id);
        model.addAttribute("lesson", dto);
        model.addAttribute("instructors", instructorRepository.findAll());
        model.addAttribute("vehicles", vehicleRepository.findAll());
        return "lesson/schedule-lesson";
    }

    @PostMapping("/schedule-lesson/save")
    public String saveSchedule(@ModelAttribute("lesson") LessonDto dto, Model model) {
        try {
            lessonService.scheduleLesson(dto);
            return "redirect:/student/my-lessons";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("lesson", dto);
            model.addAttribute("instructors", instructorRepository.findAll());
            model.addAttribute("vehicles", vehicleRepository.findAll());
            return "lesson/schedule-lesson";
        }
    }
}