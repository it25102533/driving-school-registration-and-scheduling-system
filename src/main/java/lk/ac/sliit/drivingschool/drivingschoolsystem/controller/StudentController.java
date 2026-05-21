package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentLoginDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.ProgressService;

import java.util.Optional;

@Controller
public class StudentController {

    private static final String T = "student/";
    private final StudentService studentService;
    private final ProgressService progressService;

    public StudentController(StudentService studentService, ProgressService progressService) {
        this.studentService = studentService;
        this.progressService = progressService;
    }

    @GetMapping("/student/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Student loggedInStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (loggedInStudent == null) {
            return "redirect:/login";
        }

        StudentDto studentDto = studentService.getStudentById(loggedInStudent.getId());
        model.addAttribute("student", studentDto);
        return T + "dashboard";
    }

    @GetMapping("/student/progress")
    public String showProgress(HttpSession session, Model model) {
        Student loggedInStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (loggedInStudent == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("notes", progressService.getNotesForStudent(loggedInStudent.getId()));
        return T + "progress"; 
    }

    @GetMapping("/signup")
    public String showForm(Model model) {
        model.addAttribute("student", new StudentDto());
        return "register";
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

    @GetMapping({"/login", "/login/student"})
    public String showLoginPage(@RequestParam(required = false) String required, Model model) {
        if (required != null) {
            model.addAttribute("requiredLogin", true);
        }
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

    @PostMapping("/login/legacy")
    public String processLegacyLogin(@RequestParam String name, @RequestParam Long studentId, HttpSession session, Model model) {
        try {
            StudentDto studentDto = studentService.getStudentById(studentId);
            if (studentDto != null && studentDto.getName().equalsIgnoreCase(name)) {
                Student studentEntity = new Student();
                studentEntity.setId(studentDto.getId());
                studentEntity.setName(studentDto.getName());

                session.setAttribute("SESSION_STUDENT", studentEntity);
                return "redirect:/student/dashboard";
            }
        } catch (IllegalArgumentException e) {
            // ID not found, fall through to error message below
        }
        
        model.addAttribute("error", "Legacy user record match failed. Invalid ID or Name.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    @GetMapping("/student/profile")
    public String showStudentProfile(HttpSession session, Model model) {
        Student loggedInStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (loggedInStudent == null) {
            return "redirect:/login";
        }
        StudentDto studentDto = studentService.getStudentById(loggedInStudent.getId());
        model.addAttribute("student", studentDto);
        return T + "profile";
    }

    @PostMapping("/student/profile")
    public String updateStudentProfile(@ModelAttribute("student") StudentDto studentDto,
                                       HttpSession session, Model model) {
        Student loggedInStudent = (Student) session.getAttribute("SESSION_STUDENT");
        if (loggedInStudent == null) {
            return "redirect:/login";
        }
        if (!loggedInStudent.getId().equals(studentDto.getId())) {
            return "redirect:/login";
        }
        try {
            studentService.updateStudent(studentDto);
            return "redirect:/student/profile?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("student", studentDto);
            return T + "profile";
        }
    }

    @GetMapping("/students")
    public String viewStudents(Model model) {
        model.addAttribute("allStudents", studentService.getAllStudents());
        return T + "student-list";
    }

    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam Long id) {
        try {
            studentService.deleteStudent(id);
            return "redirect:/students?deleted";
        } catch (Exception e) {
            return "redirect:/students?error=delete";
        }
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