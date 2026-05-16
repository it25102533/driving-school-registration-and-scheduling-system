package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentLoginDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class StudentController {

    private static final String T = "student/";
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Student loggedInStudent = (Student) session.getAttribute("SESSION_STUDENT");
        StudentDto studentDto = studentService.getStudentById(loggedInStudent.getId());
        model.addAttribute("student", studentDto);
        return T + "dashboard";
    }

    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("student", new StudentDto());
        return T + "register";
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute("student") StudentDto studentDto, Model model) {
        try {
            studentService.registerStudent(studentDto);
            return "redirect:/login?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("student", studentDto);
            return T + "register";
        }
    }

    @GetMapping({"/login", "/login/student"})
    public String showLoginPage(@RequestParam(required = false) String required, Model model) {
        if (required != null) {
            model.addAttribute("requiredLogin", true);
        }
        return T + "login";
    }

    @PostMapping("/login")
    public String processLogin(StudentLoginDto loginDto, HttpSession session, Model model) {
        Optional<Student> authenticatedStudent = studentService.authenticate(loginDto);

        if (authenticatedStudent.isPresent()) {
            session.setAttribute("SESSION_STUDENT", authenticatedStudent.get());
            return "redirect:/student/dashboard";
        }

        model.addAttribute("error", "Invalid email or password.");
        return T + "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    @GetMapping("/students")
    public String viewStudents(Model model) {
        model.addAttribute("allStudents", studentService.getAllStudents());
        return T + "student-list";
    }

    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }

    @GetMapping("/editStudent")
    public String showEditForm(@RequestParam Long id, Model model) {
        StudentDto studentDto = studentService.getStudentById(id);
        model.addAttribute("student", studentDto);
        return T + "edit-student";
    }

    @PostMapping("/updateStudent")
    public String updateStudent(@ModelAttribute("student") StudentDto studentDto) {
        studentService.updateStudent(studentDto);
        return "redirect:/students";
    }
}