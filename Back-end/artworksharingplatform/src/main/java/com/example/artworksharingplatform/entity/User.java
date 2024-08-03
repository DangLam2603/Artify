package com.example.artworksharingplatform.entity;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "Users")
public class User implements UserDetails {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "UserName", length = 50, nullable = false)
    private String name;

    @Column(name = "EmailAddress", unique = true, nullable = false)
    private String emailAddress;

    @Column(name = "Password", nullable = false)
    private String pass;

    @Column(name = "Telephone", length = 10, nullable = true)
    private String telephone;

    @Column(name = "ImagePath", columnDefinition = "nvarchar(max)")
    private String imagePath;

    @Column(name = "Status", nullable = false)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedDate")
    private Timestamp createdDate;

    @OneToMany(mappedBy = "userInputs", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MoneyInput> inputs;

    @OneToOne(mappedBy = "user")
    private EWallet eWallet;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "audience", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "preOrderCreator", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PreOrder> preOrderCreators;

    @OneToMany(mappedBy = "preOrderAudience", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PreOrder> preOrderAudiences;

    @OneToMany(mappedBy = "interactionAudience", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Interaction> interactions;

    @ManyToMany
    @JoinTable(name = "following", joinColumns = @JoinColumn(name = "audience_id"), inverseJoinColumns = @JoinColumn(name = "creator_id"))
    private List<User> audience;
    @ManyToMany(mappedBy = "audience")
    private List<User> creator;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Transaction> transactions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleNameWithPrefix = "ROLE_" + role.name();
        return List.of(new SimpleGrantedAuthority(roleNameWithPrefix));
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return emailAddress;
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
