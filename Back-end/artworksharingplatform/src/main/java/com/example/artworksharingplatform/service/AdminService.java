package com.example.artworksharingplatform.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.Role;
import com.example.artworksharingplatform.model.UserDTO;

@Service
public interface AdminService {
    UserDTO updateUser(UserDTO updatedUser);

    UserDTO getUserInfo(UUID userId);

    List<UserDTO> viewAllUsers();

    UserDTO findByEmailAddress(String email);

    UserDTO addUser(UserDTO userDTO);

    Role getRoleByRoleName(String roleName);
}
