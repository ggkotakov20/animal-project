package com.backendcore.backendcore.v1.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String forgotPassword;
    private String password;
    private String firstName;
    private String lastName;
    private Integer userLevel;
    private Integer active;
    @Column(name = "date_created", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp dateCreated;
    private Timestamp dateUpdated;
    private Timestamp lastLogin;
    private String token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Map userLevel to specific roles/authorities
        switch (userLevel) {
            case 99:  // Admin
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
            case 100:  // Super Admin
                authorities.add(new SimpleGrantedAuthority("ROLE_SUPERADMIN"));
                break;
            default:  // Member (default case for userLevel = 1)
                authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
                break;
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
