package com.capgemini.login_service.dto;

import com.capgemini.login_service.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String role;

}