package com.capgemini.enrollment_service.repository;

import com.capgemini.enrollment_service.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query("SELECT e FROM Enrollment e LEFT JOIN FETCH e.user u LEFT JOIN FETCH e.course c WHERE e.userId = :userId")
    List<Enrollment> findEnrollmentsByUserIdWithDetails(@Param("userId") Long userId);

    @Query("SELECT e FROM Enrollment e LEFT JOIN FETCH e.user u LEFT JOIN FETCH e.course c")
    List<Enrollment> findAllEnrollmentsWithDetails();
}