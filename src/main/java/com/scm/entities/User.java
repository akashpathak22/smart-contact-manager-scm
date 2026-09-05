package com.scm.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.scm.enums.Provider;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @Column(unique = true)
    private String userId;

    @Column(name = "username", nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Getter(value = AccessLevel.NONE)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String profileLink;

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(unique = true, nullable = true)
    private String mobile;

    @Builder.Default
    private boolean enable= false;  // ✅ Changed to true by default

    @Builder.Default
    private boolean emailVerified = false;

    @Builder.Default
    private boolean phoneVerified = false;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Provider provider = Provider.SELF;

    private String providerId;




    // gonna to be used for verificaition 
    private String emailToken;



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<Contacts> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    List<String> roles = new ArrayList<>();

    // ========== UserDetails Implementation ==========

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // ✅ ADD THESE 4 METHODS - They were missing!

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Credentials are not expired
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }
}