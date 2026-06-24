package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.LessonDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Lesson;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Vehicle;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.LessonRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.InstructorRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final VehicleRepository vehicleRepository;

    public LessonService(LessonRepository lessonRepository, StudentRepository studentRepository,
                         InstructorRepository instructorRepository, VehicleRepository vehicleRepository) {
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public void bookLesson(LessonDto dto) {
        Lesson lesson = new Lesson();

        lesson.setStudent(studentRepository.findById(dto.getStudentId()).orElseThrow());
        lesson.setInstructor(instructorRepository.findById(dto.getInstructorId()).orElseThrow());

        if (dto.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId()).orElseThrow();
            if (dto.getVehicleType() != null && !dto.getVehicleType().equalsIgnoreCase("Any")) {
                if (!vehicle.getType().toLowerCase().contains(dto.getVehicleType().toLowerCase())) {
                    throw new IllegalArgumentException("Selected vehicle type mismatch. The lesson requires a " + dto.getVehicleType() + " vehicle, but the selected vehicle is " + vehicle.getType() + ".");
                }
            }
            lesson.setVehicle(vehicle);
        }

        lesson.setLessonTime(LocalDateTime.parse(dto.getLessonTime()));
        lesson.setStatus("SCHEDULED");
        lesson.setVehicleType(dto.getVehicleType());

        lessonRepository.save(lesson);
    }

    public List<LessonDto> getStudentLessons(Long studentId) {
        // Defined outside the stream loop so it only builds once in memory
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a");

        return lessonRepository.findByStudent_Id(studentId).stream().map(lesson -> {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setStatus(lesson.getStatus());
            dto.setVehicleType(lesson.getVehicleType());
            dto.setInstructorName(lesson.getInstructor().getName());

            if (lesson.getVehicle() != null) {
                dto.setVehicleModel(lesson.getVehicle().getModel());
            } else {
                dto.setVehicleModel("Not Assigned Yet");
            }

            if (lesson.getLessonTime() != null) {
                dto.setFormattedDate(lesson.getLessonTime().format(formatter));
            } else {
                dto.setFormattedDate("Not Scheduled Yet");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public void cancelLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.setStatus("CANCELLED");
        lessonRepository.save(lesson);
    }

    public List<LessonDto> getInstructorLessons(Long instructorId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a");

        return lessonRepository.findByInstructor_Id(instructorId).stream().map(lesson -> {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setStatus(lesson.getStatus());
            dto.setVehicleType(lesson.getVehicleType());
            dto.setStudentName(lesson.getStudent().getName());

            if (lesson.getVehicle() != null) {
                dto.setVehicleModel(lesson.getVehicle().getModel());
            } else {
                dto.setVehicleModel("Not Assigned Yet");
            }

            if (lesson.getLessonTime() != null) {
                dto.setFormattedDate(lesson.getLessonTime().format(formatter));
            } else {
                dto.setFormattedDate("Not Scheduled Yet");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public void scheduleLesson(LessonDto dto) {
        Lesson lesson = lessonRepository.findById(dto.getId()).orElseThrow();
        lesson.setInstructor(instructorRepository.findById(dto.getInstructorId()).orElseThrow());
        
        if (dto.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId()).orElseThrow();
            if (dto.getVehicleType() != null && !dto.getVehicleType().equalsIgnoreCase("Any")) {
                if (!vehicle.getType().toLowerCase().contains(dto.getVehicleType().toLowerCase())) {
                    throw new IllegalArgumentException("Selected vehicle type mismatch. The lesson requires a " + dto.getVehicleType() + " vehicle, but the selected vehicle is " + vehicle.getType() + ".");
                }
            }
            lesson.setVehicle(vehicle);
        } else {
            lesson.setVehicle(null);
        }
        
        lesson.setLessonTime(LocalDateTime.parse(dto.getLessonTime()));
        lesson.setStatus("SCHEDULED");
        lesson.setVehicleType(dto.getVehicleType());
        
        lessonRepository.save(lesson);
    }

    public LessonDto getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow();
        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setStudentId(lesson.getStudent().getId());
        dto.setInstructorId(lesson.getInstructor().getId());
        dto.setInstructorName(lesson.getInstructor().getName());
        if (lesson.getVehicle() != null) {
            dto.setVehicleId(lesson.getVehicle().getId());
            dto.setVehicleModel(lesson.getVehicle().getModel());
        }
        if (lesson.getLessonTime() != null) {
            dto.setLessonTime(lesson.getLessonTime().toString());
        }
        dto.setStatus(lesson.getStatus());
        dto.setVehicleType(lesson.getVehicleType());
        return dto;
    }
}