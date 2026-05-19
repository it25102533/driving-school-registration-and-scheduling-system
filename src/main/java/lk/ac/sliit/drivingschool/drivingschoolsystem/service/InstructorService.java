package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.InstructorDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.InstructorLoginDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Instructor;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.InstructorRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.LessonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;
    private final LessonRepository lessonRepository;

    public InstructorService(InstructorRepository instructorRepository,
                             PasswordEncoder passwordEncoder,
                             LessonRepository lessonRepository) {
        this.instructorRepository = instructorRepository;
        this.passwordEncoder = passwordEncoder;
        this.lessonRepository = lessonRepository;
    }

    public void addInstructor(InstructorDto dto) {
        if (dto.getEmail() != null && instructorRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered to an instructor.");
        }

        Instructor instructor = new Instructor();
        mapDtoToEntity(dto, instructor);

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            instructor.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        instructorRepository.save(instructor);
    }

    public List<InstructorDto> getAllInstructors() {
        return instructorRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteInstructor(Long id) {
        lessonRepository.deleteByInstructor_Id(id);
        instructorRepository.deleteById(id);
    }

    public InstructorDto getInstructorById(Long id) {
        Instructor inst = instructorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid instructor Id:" + id));
        return mapEntityToDto(inst);
    }

    public void updateInstructor(InstructorDto dto) {
        Instructor inst = instructorRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid instructor Id:" + dto.getId()));

        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(inst.getEmail())
                && instructorRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered to another instructor.");
        }

        mapDtoToEntity(dto, inst);

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            inst.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        instructorRepository.save(inst);
    }

    public Optional<Instructor> authenticate(InstructorLoginDto loginDto) {
        if (loginDto.getEmail() == null || loginDto.getEmail().isEmpty()) {
            return Optional.empty();
        }

        return instructorRepository.findByEmailIgnoreCase(loginDto.getEmail().trim())
                .filter(i -> i.getPasswordHash() != null)
                .filter(i -> {
                    try {
                        return passwordEncoder.matches(loginDto.getPassword(), i.getPasswordHash());
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                });
    }

    private void mapDtoToEntity(InstructorDto dto, Instructor inst) {
        inst.setName(dto.getName());
        inst.setPhone(dto.getPhone());
        inst.setLicenseNumber(dto.getLicenseNumber());
        inst.setSpecialization(dto.getSpecialization());
        inst.setEmail(dto.getEmail());
    }

    private InstructorDto mapEntityToDto(Instructor inst) {
        InstructorDto dto = new InstructorDto();
        dto.setId(inst.getId());
        dto.setName(inst.getName());
        dto.setPhone(inst.getPhone());
        dto.setLicenseNumber(inst.getLicenseNumber());
        dto.setSpecialization(inst.getSpecialization());
        dto.setEmail(inst.getEmail());
        return dto;
    }
}
