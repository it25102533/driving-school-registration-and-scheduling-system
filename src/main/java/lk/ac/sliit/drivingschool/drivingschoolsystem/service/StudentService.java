package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentLoginDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.LessonRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.PaymentRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.ProgressRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentFeedbackRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final LessonRepository lessonRepository;
    private final ProgressRepository progressRepository;
    private final PaymentRepository paymentRepository;
    private final StudentFeedbackRepository feedbackRepository;

    public StudentService(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder,
                          LessonRepository lessonRepository,
                          ProgressRepository progressRepository,
                          PaymentRepository paymentRepository,
                          StudentFeedbackRepository feedbackRepository) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.lessonRepository = lessonRepository;
        this.progressRepository = progressRepository;
        this.paymentRepository = paymentRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public void registerStudent(StudentDto dto) { // STUDENT REGISTRATION
        if (studentRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered!");
        }

        Student student = new Student();
        mapDtoToEntity(dto, student);

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            student.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        studentRepository.save(student);
    }

    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStudent(Long id) {
        lessonRepository.deleteByStudent_Id(id);
        progressRepository.deleteByStudent_Id(id);
        paymentRepository.deleteByStudent_Id(id);
        feedbackRepository.deleteByStudent_Id(id);
        studentRepository.deleteById(id);
    }

    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        return mapEntityToDto(student);
    }

    public void updateStudent(StudentDto dto) {
        Student student = studentRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + dto.getId()));

        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(student.getEmail())
                && studentRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered by another student.");
        }

        mapDtoToEntity(dto, student);

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            student.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        studentRepository.save(student);
    }

    public Optional<Student> authenticate(StudentLoginDto loginDto) {
        if (loginDto.getEmail() != null && !loginDto.getEmail().isEmpty()) {
            return studentRepository.findByEmailIgnoreCase(loginDto.getEmail().trim())
                    .filter(s -> s.getPasswordHash() != null)
                    .filter(s -> {
                        try {
                            return passwordEncoder.matches(loginDto.getPassword(), s.getPasswordHash());
                        } catch (IllegalArgumentException e) {
                            return false;
                        }
                    });
        }

        if (loginDto.getStudentId() != null && loginDto.getName() != null) {
            return studentRepository.findById(loginDto.getStudentId())
                    .filter(s -> s.getName().equalsIgnoreCase(loginDto.getName().trim()));
        }

        return Optional.empty();
    }

    private void mapDtoToEntity(StudentDto dto, Student student) {
        student.setName(dto.getName());
        student.setPhone(dto.getPhone());
        student.setAge(dto.getAge());
        student.setLicenseType(dto.getLicenseType());
        student.setEmail(dto.getEmail());
        student.setAddress(dto.getAddress());
    }

    private StudentDto mapEntityToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setPhone(student.getPhone());
        dto.setAge(student.getAge());
        dto.setLicenseType(student.getLicenseType());
        enrichLicenseFields(dto);
        dto.setEmail(student.getEmail());
        dto.setAddress(student.getAddress());
        return dto;
    }

    private void enrichLicenseFields(StudentDto dto) { // LICENSE TYPE
        String raw = dto.getLicenseType();
        if (raw == null || raw.isBlank()) {
            dto.setLicenseDisplay("—");
            return;
        }
        if (raw.contains("|")) {
            String[] parts = raw.split("\\|", 2);
            dto.setLicenseCode(parts[0].trim());
            dto.setLicenseCategory(parts.length > 1 ? parts[1].trim() : "");
            dto.setLicenseDisplay(dto.getLicenseCode() + " — " + dto.getLicenseCategory());
        } else {
            dto.setLicenseCode(raw);
            dto.setLicenseCategory("");
            dto.setLicenseDisplay(raw);
        }
    }
}
