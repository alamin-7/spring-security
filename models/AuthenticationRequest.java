package com.testSecurity.models;

import lombok.Data;
import lombok.Value;

@Data
@Value

public class AuthenticationRequest {
    private String username;
    private String password;
}
