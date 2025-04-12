package com.capgemini.enrollment_service.controller;

import com.capgemini.enrollment_service.dto.AdminEnrollmentDTO;
import com.capgemini.enrollment_service.dto.StudentEnrollmentDTO;
import com.capgemini.enrollment_service.security.UserPrincipal;
import com.capgemini.enrollment_service.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<?> getEnrollments(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if (userPrincipal.getRole().equalsIgnoreCase("ADMIN")) {
            List<AdminEnrollmentDTO> enrollments = enrollmentService.getAllEnrollmentsWithDetails();
            return ResponseEntity.ok(enrollments.isEmpty() ? "No enrollments found" : enrollments);
        } else {
            List<StudentEnrollmentDTO> enrollments = enrollmentService.getEnrollmentsWithCourseDetails(userPrincipal.getId());
            return ResponseEntity.ok(enrollments.isEmpty() ? "No courses enrolled" : enrollments);
        }
    }
}