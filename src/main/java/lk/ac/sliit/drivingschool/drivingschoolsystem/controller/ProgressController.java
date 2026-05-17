package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;


import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.ProgressNoteDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.ProgressService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentRepository;
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
    public String saveProgress(@ModelAttribute("progressNote") ProgressNoteDto dto) {
        progressService.saveProgress(dto);
        // Cleanly redirects back to the administrative student list grid view
        return "redirect:/students";
    }

    @GetMapping("/report/{studentId}")
    public String viewReport(@PathVariable Long studentId, Model model) {
        model.addAttribute("notes", progressService.getNotesForStudent(studentId));
        model.addAttribute("student", studentRepository.findById(studentId).orElseThrow());
        return T + "student-report";
    }
}