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
        // FIXED: Added defensive null handling to protect against expired session crashes
        Student loggedInStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (loggedInStudent == null) {
            return "redirect:/login";
        }

        StudentDto studentDto = studentService.getStudentById(loggedInStudent.getId());
        model.addAttribute("student", studentDto);
        return T + "dashboard";
    }

    // FIXED: Changed endpoint to /signup to mirror roadsync.html fragments and login templates exactly
    @GetMapping("/signup")
    public String showForm(Model model) {
        model.addAttribute("student", new StudentDto());
        return "register"; // Resolves directly to templates/register.html if it's a public landing-side form
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute("student") StudentDto studentDto, Model model) {
        try {
            studentService.registerStudent(studentDto);
            return "redirect:/login?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("student", studentDto);
            return "register";
        }
    }

    // FIXED: Mapped /login/student explicitly to point back to standard template path mappings
    @GetMapping({"/login", "/login/student"})
    public String showLoginPage(@RequestParam(required = false) String required, Model model) {
        if (required != null) {
            model.addAttribute("requiredLogin", true);
        }
        // FIXED: Removed the 'student/' prefix prefixing if login.html lives directly in templates root folder
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(StudentLoginDto loginDto, HttpSession session, Model model) {
        Optional<Student> authenticatedStudent = studentService.authenticate(loginDto);

        if (authenticatedStudent.isPresent()) {
            session.setAttribute("SESSION_STUDENT", authenticatedStudent.get());
            return "redirect:/student/dashboard";
        }

        model.addAttribute("error", "Invalid email or password.");
        return "login";
    }

    // NEW: Handles the legacy form processing action we added to the login.html toggle accordion details block
    @PostMapping("/login/legacy")
    public String processLegacyLogin(@RequestParam String name, @RequestParam Long studentId, HttpSession session, Model model) {
        // Fallback placeholder logic pulling identity records directly from persistence layer
        StudentDto studentDto = studentService.getStudentById(studentId);
        if (studentDto != null && studentDto.getName().equalsIgnoreCase(name)) {
            // Fetch raw domain wrapper or reconstruct domain entity session properties
            Student studentEntity = new Student();
            studentEntity.setId(studentDto.getId());
            studentEntity.setName(studentDto.getName());

            session.setAttribute("SESSION_STUDENT", studentEntity);
            return "redirect:/student/dashboard";
        }
        model.addAttribute("error", "Legacy user record match failed.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    // NEW: Restful mapping endpoint requested by the dashboard.html profile card tile
    @GetMapping("/student/profile")
    public String showStudentProfile(HttpSession session, Model model) {
        Student loggedInStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (loggedInStudent == null) {
            return "redirect:/login";
        }
        StudentDto studentDto = studentService.getStudentById(loggedInStudent.getId());
        model.addAttribute("student", studentDto);
        return T + "edit-student"; // Points directly to templates/student/edit-student.html
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