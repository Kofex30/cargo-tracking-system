package com.example.cargo.dto;

public class LoginRequestDTO {
    private String name;
    private String password;

    // getter ve setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
