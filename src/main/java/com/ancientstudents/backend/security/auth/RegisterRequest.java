package com.ancientstudents.backend.security.auth;

import com.ancientstudents.backend.tables.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String employee_id;
    private String email;
    private String password;
    private Role role;
}
