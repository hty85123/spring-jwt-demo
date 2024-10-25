package com.example.demo.model;

public class MemberSummary {
    private String id;
    private String username;
    private String nickname;

    public MemberSummary(String id, String username, String nickname) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
