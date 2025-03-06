package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.services.CourseService;

@DataJpaTest
@Transactional
@Import(CourseService.class)
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateCourse() throws RepeatedCourseException {
        // TODO
        CourseEntity newCourse = factory.manufacturePojo(CourseEntity.class);
        newCourse.setCourseCode("TEST123");

        CourseEntity result = courseService.createCourse(newCourse);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    void testCreateRepeatedCourse() {
        // TODO
        CourseEntity existingCourse = factory.manufacturePojo(CourseEntity.class);
        existingCourse.setCourseCode("TEST123");
        entityManager.persist(existingCourse);

        CourseEntity newCourse = factory.manufacturePojo(CourseEntity.class);
        newCourse.setCourseCode("TEST123");

        assertThrows(RepeatedCourseException.class, () -> {
            courseService.createCourse(newCourse);
        });
    }
}
