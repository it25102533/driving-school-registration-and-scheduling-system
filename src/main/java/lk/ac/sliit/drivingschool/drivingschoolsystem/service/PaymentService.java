package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.PaymentDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.LessonPackage;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Payment;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.PackageRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.PaymentRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Instructor;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Lesson;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.InstructorRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.LessonRepository;
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
    private final InstructorRepository instructorRepository;
    private final LessonRepository lessonRepository;

    public PaymentService(PaymentRepository paymentRepository, 
                          PackageRepository packageRepository, 
                          StudentRepository studentRepository,
                          InstructorRepository instructorRepository,
                          LessonRepository lessonRepository) {
        this.paymentRepository = paymentRepository;
        this.packageRepository = packageRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.lessonRepository = lessonRepository;
    }

    public LessonPackage getPackageById(Long packageId) {
        return packageRepository.findById(packageId).orElseThrow();
    }

    public Long processPayment(PaymentDto dto) { // PROCESSING PAYMENTS
        Student student = studentRepository.findById(dto.getStudentId()).orElseThrow();
        LessonPackage lessonPackage = packageRepository.findById(dto.getPackageId()).orElseThrow();

        double finalPrice = lessonPackage.getBasePrice();

        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setLessonPackage(lessonPackage);
        payment.setAmountPaid(finalPrice);
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment); // CREATING PAYMENT DATABASE AND STORING

        // Auto-generate lessons based on package
        List<Instructor> availableInstructors = instructorRepository.findByAssignedPackage_Id(lessonPackage.getId());
        Instructor assignedInstructor = null; // PICKING AND ASSIGNING INSTRUCTOR
        if (!availableInstructors.isEmpty()) {
            assignedInstructor = availableInstructors.get(0); // Pick first available one
        }

        if (assignedInstructor != null) {
            for(int i = 0; i < lessonPackage.getNumberOfLessons(); i++) {
                Lesson lesson = new Lesson();
                lesson.setStudent(student);
                lesson.setInstructor(assignedInstructor);
                lesson.setStatus("Pending");
                lessonRepository.save(lesson);
            }
        }

        return savedPayment.getId();
    }

    public Payment getPaymentByIdAndStudent(Long paymentId, Long studentId) { // ONLY STUDENT CAN VIEW THE RECEIPT
        return paymentRepository.findByIdAndStudent_Id(paymentId, studentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public List<PaymentDto> getStudentPaymentHistory(Long studentId) {
        // Defined outside the stream loop so it only initializes once in memory
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy"); // STORES IN HISTORY

        return paymentRepository.findByStudent_Id(studentId).stream().map(payment -> {
            PaymentDto dto = new PaymentDto();
            dto.setId(payment.getId());
            dto.setPackageName(payment.getLessonPackage().getPackageName());
            dto.setAmountPaid(payment.getAmountPaid());

            if (payment.getPaymentDate() != null) {
                dto.setFormattedDate(payment.getPaymentDate().format(formatter)); // CONVERTS RAW DATE INTO A READABLE PATTERN
            }
            return dto;
        }).collect(Collectors.toList());
    }
}