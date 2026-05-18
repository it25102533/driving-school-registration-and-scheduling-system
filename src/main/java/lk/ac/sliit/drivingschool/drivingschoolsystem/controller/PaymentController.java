package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.PaymentDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.PaymentService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/packages")
    public String viewPackages(Model model) {
        model.addAttribute("packages", paymentService.getAllPackages());
        return "payment/packages";
    }

    @PostMapping("/buy-package")
    public String buyPackage(@RequestParam Long packageId, HttpSession session) {
        // Automatically fetch the logged-in student session
        Student student = (Student) session.getAttribute("SESSION_STUDENT");

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setStudentId(student.getId());
        paymentDto.setPackageId(packageId);

        paymentService.processPayment(paymentDto);
        return "redirect:/student/payment-history?success";
    }

    @GetMapping("/payment-history")
    public String viewHistory(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("SESSION_STUDENT");
        model.addAttribute("history", paymentService.getStudentPaymentHistory(student.getId()));
        return "payment/history";
    }
}