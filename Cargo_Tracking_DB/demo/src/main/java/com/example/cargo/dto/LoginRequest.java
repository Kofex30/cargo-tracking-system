package com.example.cargo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    private String name;
    private String password;

    public LoginRequest() {}

}
