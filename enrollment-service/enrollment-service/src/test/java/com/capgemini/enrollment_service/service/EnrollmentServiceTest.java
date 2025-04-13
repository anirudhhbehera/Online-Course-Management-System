package com.capgemini.enrollment_service.service;

import com.capgemini.enrollment_service.dto.AdminEnrollmentDTO;
import com.capgemini.enrollment_service.dto.StudentEnrollmentDTO;
import com.capgemini.enrollment_service.entity.Course;
import com.capgemini.enrollment_service.entity.Enrollment;
import com.capgemini.enrollment_service.entity.User;
import com.capgemini.enrollment_service.repository.EnrollmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment createEnrollment(Long id, Long userId, Long courseId) {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(id);
        enrollment.setUserId(userId);
        enrollment.setCourseId(courseId);
        enrollment.setEnrollmentDate(new Date());

        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setFullName("Test User");

        Course course = new Course();
        course.setId(courseId);
        course.setCourseName("Test Course");
        course.setDescription("Test Description");

        enrollment.setUser(user);
        enrollment.setCourse(course);
        return enrollment;
    }

    @Test
    void getEnrollmentsWithCourseDetails_ValidUserId_ReturnsDTOs() {
        // Arrange
        Long userId = 1L;
        Enrollment enrollment = createEnrollment(1L, userId, 101L);

        when(enrollmentRepository.findEnrollmentsByUserIdWithDetails(userId))
                .thenReturn(Collections.singletonList(enrollment));

        // Act
        List<StudentEnrollmentDTO> result = enrollmentService.getEnrollmentsWithCourseDetails(userId);

        // Assert
        assertEquals(1, result.size());
        StudentEnrollmentDTO dto = result.get(0);
        assertEquals(1L, dto.getEnrollmentId());
        assertEquals("Test Course", dto.getCourse().getCourseName());
        verify(enrollmentRepository, times(1)).findEnrollmentsByUserIdWithDetails(userId);
    }

    @Test
    void getAllEnrollmentsWithDetails_WhenEnrollmentsExist_ReturnsAdminDTOs() {
        // Arrange
        Enrollment enrollment1 = createEnrollment(1L, 1L, 101L);
        Enrollment enrollment2 = createEnrollment(2L, 2L, 102L);

        when(enrollmentRepository.findAllEnrollmentsWithDetails())
            .thenReturn(List.of(enrollment1, enrollment2));

        // Act
        List<AdminEnrollmentDTO> result = enrollmentService.getAllEnrollmentsWithDetails();

        // Assert
        assertEquals(2, result.size());
        AdminEnrollmentDTO dto = result.get(0);
        assertEquals(1L, dto.getEnrollmentId());
        assertEquals("testuser", dto.getUser().getUsername());
        assertEquals("Test Course", dto.getCourse().getCourseName());
        verify(enrollmentRepository, times(1)).findAllEnrollmentsWithDetails();
    }

    @Test
    void getEnrollmentsWithCourseDetails_NoEnrollments_ReturnsEmptyList() {
        // Arrange
        Long userId = 999L;
        when(enrollmentRepository.findEnrollmentsByUserIdWithDetails(userId))
                .thenReturn(Collections.emptyList());

        // Act
        List<StudentEnrollmentDTO> result = enrollmentService.getEnrollmentsWithCourseDetails(userId);

        // Assert
        assertTrue(result.isEmpty());
        verify(enrollmentRepository, times(1)).findEnrollmentsByUserIdWithDetails(userId);
    }

    @Test
    void getAllEnrollmentsWithDetails_NoEnrollments_ReturnsEmptyList() {
        // Arrange
        when(enrollmentRepository.findAllEnrollmentsWithDetails())
            .thenReturn(Collections.emptyList());

        // Act
        List<AdminEnrollmentDTO> result = enrollmentService.getAllEnrollmentsWithDetails();

        // Assert
        assertTrue(result.isEmpty());
        verify(enrollmentRepository, times(1)).findAllEnrollmentsWithDetails();
    }
}