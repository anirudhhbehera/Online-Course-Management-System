package com.capgemini.enrollment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminEnrollmentDTO {
    private Long enrollmentId;
    private Date enrollmentDate;
    private UserDTO user;
    private CourseDTO course;
}