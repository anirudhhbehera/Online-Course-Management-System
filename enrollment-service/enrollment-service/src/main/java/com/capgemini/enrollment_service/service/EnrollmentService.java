package com.capgemini.enrollment_service.service;

import com.capgemini.enrollment_service.dto.AdminEnrollmentDTO;
import com.capgemini.enrollment_service.dto.CourseDTO;
import com.capgemini.enrollment_service.dto.StudentEnrollmentDTO;
import com.capgemini.enrollment_service.dto.UserDTO;
import com.capgemini.enrollment_service.entity.Enrollment;
import com.capgemini.enrollment_service.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public List<StudentEnrollmentDTO> getEnrollmentsWithCourseDetails(Long userId) {
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByUserIdWithDetails(userId);
        return enrollments.stream().map(this::mapToStudentEnrollmentDTO).toList();
    }

    public List<AdminEnrollmentDTO> getAllEnrollmentsWithDetails() {
        List<Enrollment> enrollments = enrollmentRepository.findAllEnrollmentsWithDetails();
        return enrollments.stream().map(this::mapToAdminEnrollmentDTO).toList();
    }

    private StudentEnrollmentDTO mapToStudentEnrollmentDTO(Enrollment enrollment) {
        return new StudentEnrollmentDTO(
                enrollment.getId(),
                enrollment.getEnrollmentDate(),
                new CourseDTO(
                        enrollment.getCourse().getId(),
                        enrollment.getCourse().getCourseName(),
                        enrollment.getCourse().getDescription()
                )
        );
    }

    private AdminEnrollmentDTO mapToAdminEnrollmentDTO(Enrollment enrollment) {
        return new AdminEnrollmentDTO(
                enrollment.getId(),
                enrollment.getEnrollmentDate(),
                new UserDTO(
                        enrollment.getUser().getId(),
                        enrollment.getUser().getUsername(),
                        enrollment.getUser().getFullName(),
                        enrollment.getUser().getEmail()
                ),
                new CourseDTO(
                        enrollment.getCourse().getId(),
                        enrollment.getCourse().getCourseName(),
                        enrollment.getCourse().getDescription()
                )
        );
    }
}