package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.VehicleDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private static final String T = "vehicle/";
    private final VehicleService vehicleService;


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
    public String deleteVehicle(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            vehicleService.deleteVehicle(id);
            redirectAttributes.addFlashAttribute("success", "Vehicle deleted successfully.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete vehicle. It is currently assigned to one or more lessons.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while trying to delete the vehicle.");
        }
        return "redirect:/vehicles";
    }
}