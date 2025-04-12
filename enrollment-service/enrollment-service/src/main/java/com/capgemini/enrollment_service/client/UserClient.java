package com.capgemini.enrollment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "registration-service")
public interface UserClient {
    @GetMapping("/api/users/{username}")
    UserDto getByUsername(@PathVariable("username") String username);

    class UserDto {
        public Long id;
        public String username;
        public String email;
        public String role;
    }
}
