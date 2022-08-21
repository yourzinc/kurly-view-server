package com.kurly.kurlyview.domain.member;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
@Document(collection = "Member")
public class Member implements UserDetails {

    @Id
    private String id;
    private String email;
    private String name;
    private String password;

    private List<Kurlyview> kurlyviews;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Data
    public static class Kurlyview {
        private String id;
        private String email;
        private String name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
