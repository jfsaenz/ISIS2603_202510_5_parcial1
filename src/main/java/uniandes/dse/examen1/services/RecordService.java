package uniandes.dse.examen1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class RecordService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RecordRepository recordRepository;

    public RecordEntity createRecord(String loginStudent, String courseCode, Double grade, String semester)
            throws InvalidRecordException {
        // TODO
        log.info("Creando nuevo registro para: {} en el curso: {}", loginStudent, courseCode);

    
        Optional<StudentEntity> studentOpt = studentRepository.findByLogin(loginStudent);
        if (!studentOpt.isPresent()) {
            throw new InvalidRecordException("El estudiante no existe");
        }
        StudentEntity student = studentOpt.get();

    
        Optional<CourseEntity> courseOpt = courseRepository.findByCourseCode(courseCode);
        if (!courseOpt.isPresent()) {
            throw new InvalidRecordException("El estudiante no existe.");
        }
        CourseEntity course = courseOpt.get();
        if (grade < 1.5 || grade > 5.0) {
            throw new InvalidRecordException("La nota debe ser entre 1.5 y 5");
        }
        List<RecordEntity> records = student.getRecords();
        for (RecordEntity record : records) {
            if (record.getCourse().equals(course) && record.getFinalGrade() >= 3.0) {
                throw new InvalidRecordException("El estudiante ya aprobó el curso");

            }
        }
        RecordEntity newRecord = new RecordEntity();
        newRecord.setStudent(student);
        newRecord.setCourse(course);
        newRecord.setFinalGrade(grade);
        newRecord.setSemester(semester);

        return recordRepository.save(newRecord);
    }
    
}
