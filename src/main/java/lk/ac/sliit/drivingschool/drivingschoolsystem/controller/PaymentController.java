package lk.ac.sliit.drivingschool.drivingschoolsystem.controller;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.PaymentDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.LessonPackageService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.service.PaymentService;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.LessonPackage;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Payment;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.awt.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/student")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;
    private final LessonPackageService lessonPackageService;

    public PaymentController(PaymentService paymentService, LessonPackageService lessonPackageService) {
        this.paymentService = paymentService;
        this.lessonPackageService = lessonPackageService;
    }

    @GetMapping("/packages") // DISPLAY AVAILABLE PACKAGES
    public String viewPackages(Model model, HttpSession session) {
        Student student = (Student) session.getAttribute("SESSION_STUDENT");
        if (student == null) {
            return "redirect:/login";
        }
        model.addAttribute("packages", lessonPackageService.getAllPackages());
        return "payment/packages";
    }

    @GetMapping("/checkout") // PROCESSING CHECKOUT
    public String checkout(@RequestParam Long packageId, Model model, HttpSession session) {
        Student student = (Student) session.getAttribute("SESSION_STUDENT"); // IF THE STUDENT IS LOGGED IN
        if (student == null) {
            return "redirect:/login";
        }
        LessonPackage lessonPackage = paymentService.getPackageById(packageId); // TO SHOW DETAILS IN CHECKOUT PAGE
        model.addAttribute("packageDetails", lessonPackage);
        model.addAttribute("finalPrice", lessonPackage.getBasePrice());
        return "payment/checkout";
    }

    @PostMapping("/buy-package") // PAY
    public String buyPackage(@RequestParam Long packageId, HttpSession session) {
        Student student = (Student) session.getAttribute("SESSION_STUDENT");
        if (student == null) {
            return "redirect:/login";
        }

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setStudentId(student.getId());
        paymentDto.setPackageId(packageId); // SENDS INFO TO CREATE DATABASE

        Long paymentId = paymentService.processPayment(paymentDto); // PAYMENT SUCCESS
        return "redirect:/student/payment-success?paymentId=" + paymentId;
    }

    @GetMapping("/payment-success") // POST PAYMENT CONFIRMATION
    public String paymentSuccess(@RequestParam Long paymentId, Model model, HttpSession session) {
        Student student = (Student) session.getAttribute("SESSION_STUDENT");
        if (student == null) {
            return "redirect:/login";
        }
        
        Payment payment = paymentService.getPaymentByIdAndStudent(paymentId, student.getId());
        model.addAttribute("payment", payment);
        return "payment/success";
    }

    @GetMapping("/payment-history") // PAYMENT HISTORY
    public String viewHistory(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("SESSION_STUDENT");
        model.addAttribute("history", paymentService.getStudentPaymentHistory(student.getId()));
        return "payment/history";
    }

    @GetMapping("/invoice/{id}") // CREATING AN INVOICE
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id, HttpSession session) {
        Student student = (Student) session.getAttribute("SESSION_STUDENT");
        if (student == null) {
            return ResponseEntity.status(401).build();
        }

        Payment payment = paymentService.getPaymentByIdAndStudent(id, student.getId());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) { // PDF INITIALIZER
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK); // INVOICE DETAILS
            Paragraph title = new Paragraph("RoadSync Payment Invoice", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
            
            document.add(new Paragraph("Invoice ID: #" + payment.getId(), regularFont));
            document.add(new Paragraph("Date: " + payment.getPaymentDate().toString(), regularFont));
            document.add(new Paragraph("Student Name: " + payment.getStudent().getName(), regularFont));
            document.add(new Paragraph("Package: " + payment.getLessonPackage().getPackageName(), regularFont));
            document.add(new Paragraph("Amount Paid: LKR " + payment.getAmountPaid(), regularFont));

            document.close();

            HttpHeaders headers = new HttpHeaders(); // DOWNLOADING
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Invoice_" + payment.getId() + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(baos.toByteArray());
        } catch (Exception e) { // RUNTIME ERROR (500 SERVER ERROR)
            logger.error("Error generating invoice PDF", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}