package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "members")
public class Member {
    @Id
    private String id;
    private String username;
    private String password;
    private String nickname;
    private List<MemberAuthority> authorities = new ArrayList<>();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<MemberAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<MemberAuthority> authorities) {
        this.authorities = authorities;
    }
}