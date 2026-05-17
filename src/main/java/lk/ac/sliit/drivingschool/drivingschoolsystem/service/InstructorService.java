package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.InstructorDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Instructor;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public void addInstructor(InstructorDto dto) {
        Instructor instructor = new Instructor();
        mapDtoToEntity(dto, instructor);
        instructorRepository.save(instructor);
    }

    public List<InstructorDto> getAllInstructors() {
        return instructorRepository.findAll().stream().map(inst -> {
            InstructorDto dto = new InstructorDto();
            dto.setId(inst.getId());
            dto.setName(inst.getName());
            dto.setPhone(inst.getPhone());
            dto.setLicenseNumber(inst.getLicenseNumber());
            dto.setSpecialization(inst.getSpecialization());
            return dto;
        }).collect(Collectors.toList());
    }

    public void deleteInstructor(Long id) {
        instructorRepository.deleteById(id);
    }

    public InstructorDto getInstructorById(Long id) {
        Instructor inst = instructorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid instructor Id:" + id));

        InstructorDto dto = new InstructorDto();
        dto.setId(inst.getId());
        dto.setName(inst.getName());
        dto.setPhone(inst.getPhone());
        dto.setLicenseNumber(inst.getLicenseNumber());
        dto.setSpecialization(inst.getSpecialization());
        return dto;
    }

    public void updateInstructor(InstructorDto dto) {
        Instructor inst = instructorRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid instructor Id:" + dto.getId()));

        mapDtoToEntity(dto, inst);
        instructorRepository.save(inst);
    }

    private void mapDtoToEntity(InstructorDto dto, Instructor inst) {
        inst.setName(dto.getName());
        inst.setPhone(dto.getPhone());
        inst.setLicenseNumber(dto.getLicenseNumber());
        inst.setSpecialization(dto.getSpecialization());
    }
}