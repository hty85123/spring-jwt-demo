package com.example.demo.model;

import com.example.demo.security.MemberUserDetails;

import java.util.List;

public class LoginResponse {
    private String jwt;
    private String id;
    private String username;
    private String nickname;
    private List<MemberAuthority> authorities;

    public static LoginResponse of(String jwt, MemberUserDetails user) {
        var res = new LoginResponse();
        res.jwt = jwt;
        res.id = user.getId();
        res.username = user.getUsername();
        res.nickname = user.getNickname();
        res.authorities = user.getMemberAuthorities();

        return res;
    }

    public String getJwt() {
        return jwt;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public List<MemberAuthority> getAuthorities() {
        return authorities;
    }
}