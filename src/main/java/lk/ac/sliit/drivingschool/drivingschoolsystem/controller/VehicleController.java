package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.VehicleDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private static final String T = "vehicle/";
    private final VehicleService vehicleService;


    // This constructor matches the Spring Boot standard for dependency injection
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public String viewVehicles(Model model) {
        model.addAttribute("allVehicles", vehicleService.getAllVehicles());
        return T + "vehicle-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("vehicle", new VehicleDto());
        return T + "add-vehicle";
    }

    @PostMapping("/save")
    public String saveVehicle(@ModelAttribute("vehicle") VehicleDto vehicleDto) {
        vehicleService.addVehicle(vehicleDto);
        return "redirect:/vehicles";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        VehicleDto vehicleDto = vehicleService.getVehicleById(id);
        model.addAttribute("vehicle", vehicleDto);
        return T + "edit-vehicle";
    }

    @PostMapping("/update")
    public String updateVehicle(@ModelAttribute("vehicle") VehicleDto vehicleDto) {
        vehicleService.updateVehicle(vehicleDto);
        return "redirect:/vehicles";
    }

    @GetMapping("/delete")
    public String deleteVehicle(@RequestParam Long id) {
        vehicleService.deleteVehicle(id);
        return "redirect:/vehicles";
    }
}