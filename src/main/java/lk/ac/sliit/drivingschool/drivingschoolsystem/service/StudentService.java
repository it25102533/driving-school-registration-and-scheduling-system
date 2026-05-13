package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.StudentLoginDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerStudent(StudentDto dto) {
        // PREVENT DUPLICATES: Check if email exists before saving
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

    // ENCAPSULATION: Return a list of DTOs, not the raw Entities
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public void deleteStudent(Long id) {
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

        mapDtoToEntity(dto, student);

        // Only update password if a new one is provided
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            student.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        studentRepository.save(student);
    }

    public Optional<Student> authenticate(StudentLoginDto loginDto) {
        // Primary Login: Email & Password
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

        // Legacy Login fallback
        if (loginDto.getStudentId() != null && loginDto.getName() != null) {
            return studentRepository.findById(loginDto.getStudentId())
                    .filter(s -> s.getName().equalsIgnoreCase(loginDto.getName().trim()));
        }

        return Optional.empty();
    }

    // Helper Method 1: Map DTO to Entity
    private void mapDtoToEntity(StudentDto dto, Student student) {
        student.setName(dto.getName());
        student.setPhone(dto.getPhone());
        student.setAge(dto.getAge());
        student.setLicenseType(dto.getLicenseType());
        student.setStudentType(dto.getStudentType());
        student.setEmail(dto.getEmail());
        student.setAddress(dto.getAddress());
    }

    // Helper Method 2: Map Entity to DTO (Prevents code duplication)
    private StudentDto mapEntityToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setPhone(student.getPhone());
        dto.setAge(student.getAge());
        dto.setLicenseType(student.getLicenseType());
        dto.setStudentType(student.getStudentType());
        dto.setEmail(student.getEmail());
        dto.setAddress(student.getAddress());
        return dto;
    }
}