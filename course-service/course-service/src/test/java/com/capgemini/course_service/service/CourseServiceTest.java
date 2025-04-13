package com.capgemini.course_service.service;

import com.capgemini.course_service.entity.Course;
import com.capgemini.course_service.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course createCourse(Long id, String name, String description) {
        Course course = new Course();
        course.setId(id);
        course.setCourse_name(name);
        course.setCourse_description(description);
        return course;
    }

    @Test
    void createCourse_ValidCourse_ReturnsSavedCourse() {
        Course newCourse = createCourse(null, "Mathematics", "Algebra and Calculus");

        when(courseRepository.save(any(Course.class))).thenReturn(newCourse);

        Course result = courseService.createCourse(newCourse);

        assertNotNull(result);
        assertEquals("Mathematics", result.getCourse_name());
        verify(courseRepository, times(1)).save(newCourse);
    }

    @Test
    void getAllCourses_WhenCoursesExist_ReturnsCourseList() {
        Course course1 = createCourse(1L, "Physics", "Classical Mechanics");
        Course course2 = createCourse(2L, "Chemistry", "Organic Chemistry");

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> courses = courseService.getAllCourses();

        assertEquals(2, courses.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getCourseById_ExistingId_ReturnsCourse() {
        Long courseId = 1L;
        Course course = createCourse(courseId, "Biology", "Cell Biology");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(courseId);

        assertNotNull(result);
        assertEquals("Biology", result.getCourse_name());
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void getCourseById_NonExistingId_ReturnsNull() {
        Long courseId = 999L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Course result = courseService.getCourseById(courseId);

        assertNull(result);
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void updateCourse_ExistingId_ReturnsUpdatedCourse() {
        Long courseId = 1L;
        Course existingCourse = createCourse(courseId, "Old Physics", "Classical Mechanics");
        Course updatedCourse = createCourse(courseId, "New Physics", "Quantum Mechanics");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        Course result = courseService.updateCourse(courseId, updatedCourse);

        assertNotNull(result);
        assertEquals("New Physics", result.getCourse_name());
        assertEquals("Quantum Mechanics", result.getCourse_description());
        verify(courseRepository, times(1)).save(updatedCourse);
    }

    @Test
    void updateCourse_NonExistingId_ReturnsNull() {
        Long courseId = 999L;
        Course updatedCourse = createCourse(courseId, "Test Course", "Test Description");

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Course result = courseService.updateCourse(courseId, updatedCourse);

        assertNull(result);
        verify(courseRepository, never()).save(any());
    }

    @Test
    void deleteCourse_ValidId_DeletesCourse() {
        Long courseId = 1L;

        doNothing().when(courseRepository).deleteById(courseId);

        courseService.deleteCourse(courseId);

        verify(courseRepository, times(1)).deleteById(courseId);
    }
}