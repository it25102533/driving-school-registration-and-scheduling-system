package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.ProgressNoteDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.ProgressNote;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.ProgressRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Student;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final StudentRepository studentRepository;

    public ProgressService(ProgressRepository progressRepository, StudentRepository studentRepository) {
        this.progressRepository = progressRepository;
        this.studentRepository = studentRepository;
    }

    public void saveProgress(ProgressNoteDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));

        ProgressNote note = new ProgressNote();
        note.setStudent(student);
        note.setLessonTopic(dto.getLessonTopic());
        note.setInstructorNote(dto.getInstructorNote());
        note.setDate(LocalDateTime.now()); // Auto-set the current runtime stamp

        progressRepository.save(note);
    }

    @Transactional(readOnly = true)
    public List<ProgressNoteDto> getNotesForStudent(Long studentId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

        return progressRepository.findByStudent_Id(studentId).stream().map(note -> {
            ProgressNoteDto dto = new ProgressNoteDto();
            dto.setId(note.getId());
            dto.setStudentId(studentId);
            dto.setLessonTopic(note.getLessonTopic());
            dto.setInstructorNote(note.getInstructorNote());

            // Format the date string cleanly for the Thymeleaf view layers
            if (note.getDate() != null) {
                dto.setDateFormatted(note.getDate().format(formatter));
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void deleteProgress(Long id) {
        progressRepository.deleteById(id);
    }
}