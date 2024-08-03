package com.example.artworksharingplatform.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.entity.Role;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAddress(String emailAddress);

    User findByRole(Role role);

    List<User> findByRoleNotAndNameContainingIgnoreCase(Role role, String name);

    List<User> findAllByRole(Role role);

    List<User> findByRoleNotOrderByCreatedDateAsc(Role role);

    List<User> findByRoleNotOrderByCreatedDateDesc(Role role);

    List<User> findByRoleNot(Role role);
    List<User> findByStatus(String status);
}
