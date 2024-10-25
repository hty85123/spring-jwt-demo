package com.example.demo.security;

import com.example.demo.model.Member;
import com.example.demo.model.MemberAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MemberUserDetails implements UserDetails {
    private String id;
    private String username;
    private String password;
    private String nickname;
    private List<MemberAuthority> memberAuthorities;

    public MemberUserDetails() {
    }

    public MemberUserDetails(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.nickname = member.getNickname();
        this.memberAuthorities = member.getAuthorities();
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getNickname() {
        return this.nickname;
    }

    public List<MemberAuthority> getMemberAuthorities() {
        return this.memberAuthorities;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.memberAuthorities
                .stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMemberAuthorities(List<MemberAuthority> memberAuthorities) {
        this.memberAuthorities = memberAuthorities;
    }
}