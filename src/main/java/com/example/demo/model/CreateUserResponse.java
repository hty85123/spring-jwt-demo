package com.example.demo.model;

public class CreateUserResponse {
    private String id;
    private String username;
    private String message;

    public CreateUserResponse(String id, String username, String message) {
        this.id = id;
        this.username = username;
        this.message = message;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
