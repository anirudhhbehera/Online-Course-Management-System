package com.capgemini.enrollment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Assumes your Course Service app name is "course-service"
@FeignClient(name = "course-service")
public interface CourseClient {
    @GetMapping("/courses/{id}")
    CourseDto getCourseById(@PathVariable("id") Long id);

    // DTO matching CourseService.entity.Course
    class CourseDto {
        public Long id;
        public String course_name;
        public String course_description;
    }
}
