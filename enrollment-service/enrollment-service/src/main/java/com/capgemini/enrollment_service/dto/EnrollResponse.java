package com.capgemini.enrollment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EnrollResponse {
    private Long enrollmentId;
    private Long userId;
    private Long courseId;
    private LocalDateTime enrollmentDate;
}
