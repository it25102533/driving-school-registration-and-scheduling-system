package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.PaymentDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.LessonPackage;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Payment;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.PackageRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.PaymentRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PackageRepository packageRepository;
    private final StudentRepository studentRepository;

    public PaymentService(PaymentRepository paymentRepository, PackageRepository packageRepository, StudentRepository studentRepository) {
        this.paymentRepository = paymentRepository;
        this.packageRepository = packageRepository;
        this.studentRepository = studentRepository;
    }

    public LessonPackage getPackageById(Long packageId) {
        return packageRepository.findById(packageId).orElseThrow();
    }

    public void processPayment(PaymentDto dto) {
        Student student = studentRepository.findById(dto.getStudentId()).orElseThrow();
        LessonPackage lessonPackage = packageRepository.findById(dto.getPackageId()).orElseThrow();

        // Business Logic calculating the final price based on the custom student discount
        double discount = student.calculateDiscount(lessonPackage.getBasePrice());
        double finalPrice = lessonPackage.getBasePrice() - discount;

        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setLessonPackage(lessonPackage);
        payment.setAmountPaid(finalPrice);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);
    }

    public List<PaymentDto> getStudentPaymentHistory(Long studentId) {
        // Defined outside the stream loop so it only initializes once in memory
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        return paymentRepository.findByStudent_Id(studentId).stream().map(payment -> {
            PaymentDto dto = new PaymentDto();
            dto.setId(payment.getId());
            dto.setPackageName(payment.getLessonPackage().getPackageName());
            dto.setAmountPaid(payment.getAmountPaid());

            if (payment.getPaymentDate() != null) {
                dto.setFormattedDate(payment.getPaymentDate().format(formatter));
            }
            return dto;
        }).collect(Collectors.toList());
    }
}