package com.mh.auth.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean accountNonExpired = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean accountNonLocked = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean credentialsNonExpired = true;

    // Security tracking
    @Column
    private LocalDateTime lastLoginAt;

    @Column(nullable = false)
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column
    private LocalDateTime lockedUntil;

    @Column
    private String lastLoginIp;

    // Relationships
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    public Set<Permission> getAllPermissions() {
        Set<Permission> allPermissions = new HashSet<>();
        roles.forEach(role -> allPermissions.addAll(role.getPermissions()));
        return allPermissions;
    }

    public void incrementFailedAttempts() {
        this.failedLoginAttempts++;
    }

    public void resetFailedAttempts() {
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
        this.accountNonLocked = true;
    }

    public void lockAccount(int minutes) {
        this.lockedUntil = LocalDateTime.now().plusMinutes(minutes);
        this.accountNonLocked = false;
    }

    public boolean isAccountLocked() {
        return this.lockedUntil != null && this.lockedUntil.isAfter(LocalDateTime.now());
    }
}