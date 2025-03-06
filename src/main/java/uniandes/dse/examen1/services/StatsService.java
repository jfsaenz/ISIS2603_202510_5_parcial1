package uniandes.dse.examen1.services;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class StatsService {

    @Autowired
    StudentRepository estudianteRepository;

    @Autowired
    CourseRepository cursoRepository;

    @Autowired
    RecordRepository inscripcionRepository;

    public Double calculateStudentAverage(String login) {
        // TODO
        log.info("Calculating average for student with login: {}", login);

        Optional<StudentEntity> studentOpt = estudianteRepository.findByLogin(login);
        if (!studentOpt.isPresent()) {
            log.error("Estudiante no encontrado");
            return null;
        }

        StudentEntity student = studentOpt.get();
        List<RecordEntity> records = student.getRecords();

        if (records.isEmpty()) {
            log.info("No hay registros para el estudiante");
            return null;
        }
        double average = records.stream()
        .collect(Collectors.averagingDouble(RecordEntity::getFinalGrade));

        return average;
    }

    public Double calculateCourseAverage(String courseCode) {
        // TODO
        log.info("Calculando {}", courseCode);

        Optional<CourseEntity> courseOpt = cursoRepository.findByCourseCode(courseCode);
        if (!courseOpt.isPresent()) {
            log.error("el curso {} no se encontró", courseCode);
            return null;
        }

        CourseEntity course = courseOpt.get();
        List<StudentEntity> students = course.getStudents();

        if (students.isEmpty()) {
            log.info("No hay estudiantes en: {}", courseCode);
            return null;
        }

        List<RecordEntity> records = students.stream()
                .flatMap(student -> student.getRecords().stream())
                .filter(record -> record.getCourse().equals(course))
                .collect(Collectors.toList());

        if (records.isEmpty()) {
            log.info("No hay registros para: {}", courseCode);
            return null;
        }
        double average = records.stream()
        .collect(Collectors.averagingDouble(RecordEntity::getFinalGrade));

        return average;

    }

}
