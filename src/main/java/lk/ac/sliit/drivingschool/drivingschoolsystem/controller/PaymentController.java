package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.PaymentDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.LessonPackageService;
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
    private final LessonPackageService lessonPackageService;

    public PaymentController(PaymentService paymentService, LessonPackageService lessonPackageService) {
        this.paymentService = paymentService;
        this.lessonPackageService = lessonPackageService;
    }

    @GetMapping("/packages")
    public String viewPackages(Model model, HttpSession session) {
        Student student = (Student) session.getAttribute("SESSION_STUDENT");
        if (student == null) {
            return "redirect:/login";
        }
        model.addAttribute("packages", lessonPackageService.getAllPackages());
        model.addAttribute("studentType", student.getStudentType());
        return "payment/packages";
    }

    @PostMapping("/buy-package")
    public String buyPackage(@RequestParam Long packageId, HttpSession session) {
        Student student = (Student) session.getAttribute("SESSION_STUDENT");
        if (student == null) {
            return "redirect:/login";
        }

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