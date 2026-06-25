package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.LessonDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.LessonService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.InstructorRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.VehicleRepository;
import jakarta.servlet.http.HttpSession;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Vehicle;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class LessonController {

    private final LessonService lessonService;
    private final InstructorRepository instructorRepository;
    private final VehicleRepository vehicleRepository;
    private final StudentRepository studentRepository;

    public LessonController(LessonService lessonService, InstructorRepository instructorRepository,
                            VehicleRepository vehicleRepository, StudentRepository studentRepository) {
        this.lessonService = lessonService;
        this.instructorRepository = instructorRepository;
        this.vehicleRepository = vehicleRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/book-lesson")
    public String showBookingForm(HttpSession session, Model model) {
        Student sessionStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (sessionStudent == null) {
            return "redirect:/login";
        }
        Student student = studentRepository.findById(sessionStudent.getId()).orElseThrow();
        String preference = student.getTransmissionPreference();

        List<Vehicle> matchedVehicles = vehicleRepository.findAll().stream()
                .filter(v -> v.getType() != null && v.getType().equalsIgnoreCase(preference))
                .toList();

        model.addAttribute("lesson", new LessonDto());
        model.addAttribute("instructors", instructorRepository.findAll());
        model.addAttribute("vehicles", matchedVehicles);
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
            Student student = studentRepository.findById(sessionStudent.getId()).orElseThrow();
            String preference = student.getTransmissionPreference();
            List<Vehicle> matchedVehicles = vehicleRepository.findAll().stream()
                    .filter(v -> v.getType() != null && v.getType().equalsIgnoreCase(preference))
                    .toList();

            model.addAttribute("error", e.getMessage());
            model.addAttribute("lesson", dto);
            model.addAttribute("instructors", instructorRepository.findAll());
            model.addAttribute("vehicles", matchedVehicles);
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
    public String showScheduleForm(@RequestParam Long id, HttpSession session, Model model) {
        Student sessionStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (sessionStudent == null) {
            return "redirect:/login";
        }
        Student student = studentRepository.findById(sessionStudent.getId()).orElseThrow();
        String preference = student.getTransmissionPreference();

        List<Vehicle> matchedVehicles = vehicleRepository.findAll().stream()
                .filter(v -> v.getType() != null && v.getType().equalsIgnoreCase(preference))
                .toList();

        LessonDto dto = lessonService.getLessonById(id);
        model.addAttribute("lesson", dto);
        model.addAttribute("instructors", instructorRepository.findAll());
        model.addAttribute("vehicles", matchedVehicles);
        return "lesson/schedule-lesson";
    }

    @PostMapping("/schedule-lesson/save")
    public String saveSchedule(@ModelAttribute("lesson") LessonDto dto, HttpSession session, Model model) {
        try {
            lessonService.scheduleLesson(dto);
            return "redirect:/student/my-lessons";
        } catch (IllegalArgumentException e) {
            Student sessionStudent = (Student) session.getAttribute("SESSION_STUDENT");
            Student student = studentRepository.findById(sessionStudent.getId()).orElseThrow();
            String preference = student.getTransmissionPreference();
            List<Vehicle> matchedVehicles = vehicleRepository.findAll().stream()
                    .filter(v -> v.getType() != null && v.getType().equalsIgnoreCase(preference))
                    .toList();

            model.addAttribute("error", e.getMessage());
            model.addAttribute("lesson", dto);
            model.addAttribute("instructors", instructorRepository.findAll());
            model.addAttribute("vehicles", matchedVehicles);
            return "lesson/schedule-lesson";
        }
    }
}