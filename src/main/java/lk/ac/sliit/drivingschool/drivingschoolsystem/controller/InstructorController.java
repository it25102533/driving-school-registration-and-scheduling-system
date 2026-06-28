package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.InstructorDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.InstructorService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.LessonPackageService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/instructors")
public class InstructorController {

    private static final String T = "instructor/";
    private final InstructorService instructorService;
    private final LessonPackageService lessonPackageService;
    private final VehicleService vehicleService;

    public InstructorController(InstructorService instructorService, LessonPackageService lessonPackageService, VehicleService vehicleService) {
        this.instructorService = instructorService;
        this.lessonPackageService = lessonPackageService;
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public String viewInstructors(Model model) {
        model.addAttribute("allInstructors", instructorService.getAllInstructors());
        return T + "instructor-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("instructor", new InstructorDto());
        model.addAttribute("packages", lessonPackageService.getAllPackages());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return T + "add-instructor";
    }

    @PostMapping("/save")
    public String saveInstructor(@ModelAttribute("instructor") InstructorDto dto, Model model) {
        try {
            instructorService.addInstructor(dto);
            return "redirect:/instructors";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("instructor", dto);
            model.addAttribute("packages", lessonPackageService.getAllPackages());
            model.addAttribute("vehicles", vehicleService.getAllVehicles());
            return T + "add-instructor";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        model.addAttribute("instructor", instructorService.getInstructorById(id));
        model.addAttribute("packages", lessonPackageService.getAllPackages());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return T + "edit-instructor";
    }

    @PostMapping("/update")
    public String updateInstructor(@ModelAttribute("instructor") InstructorDto dto, Model model) {
        try {
            instructorService.updateInstructor(dto);
            return "redirect:/instructors";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("instructor", dto);
            model.addAttribute("packages", lessonPackageService.getAllPackages());
            model.addAttribute("vehicles", vehicleService.getAllVehicles());
            return T + "edit-instructor";
        }
    }

    @GetMapping("/delete")
    public String deleteInstructor(@RequestParam Long id) {
        instructorService.deleteInstructor(id);
        return "redirect:/instructors";
    }
}