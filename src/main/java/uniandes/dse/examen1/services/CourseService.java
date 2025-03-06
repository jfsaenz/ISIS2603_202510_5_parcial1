package uniandes.dse.examen1.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.repositories.CourseRepository;

@Slf4j
@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public CourseEntity createCourse(CourseEntity newCourse) throws RepeatedCourseException {
        // TODO
        log.info("Creando nuevo curso: {}", newCourse.getCourseCode());

        Optional<CourseEntity> existingCourse = courseRepository.findByCourseCode(newCourse.getCourseCode());
        if (existingCourse.isPresent()) {
            throw new RepeatedCourseException("El curso ya existe");
        }

        return courseRepository.save(newCourse);
    }
    
}
