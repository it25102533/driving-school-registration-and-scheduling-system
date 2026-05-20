package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.controller.ProfessionalAuthController;
import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.ProgressNoteDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.ProgressService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/progress")
public class ProgressController {

    private static final String T = "progress/";
    private final ProgressService progressService;
    private final StudentRepository studentRepository;

    public ProgressController(ProgressService progressService, StudentRepository studentRepository) {
        this.progressService = progressService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        // Fetches all student profiles so the instructor can assign a progress milestone note
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("progressNote", new ProgressNoteDto());
        return T + "add-progress";
    }

    @PostMapping("/save")
    public String saveProgress(@ModelAttribute("progressNote") ProgressNoteDto dto, HttpSession session) {
        progressService.saveProgress(dto);
        if (session.getAttribute(ProfessionalAuthController.SESSION_INSTRUCTOR) != null) {
            return "redirect:/progress/report/" + dto.getStudentId() + "?saved";
        }
        return "redirect:/students";
    }

    @PostMapping("/delete")
    public String deleteProgress(@RequestParam Long id, @RequestParam Long studentId) {
        progressService.deleteProgress(id);
        return "redirect:/progress/report/" + studentId + "?deleted";
    }

    @GetMapping("/report/{studentId}")
    public String viewReport(@PathVariable Long studentId, Model model) {
        model.addAttribute("notes", progressService.getNotesForStudent(studentId));
        model.addAttribute("student", studentRepository.findById(studentId).orElseThrow());
        return T + "student-report";
    }
}