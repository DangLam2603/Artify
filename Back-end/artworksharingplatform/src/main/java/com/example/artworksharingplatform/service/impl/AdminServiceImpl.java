package com.example.artworksharingplatform.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.Role;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.mapper.UserMapper;
import com.example.artworksharingplatform.model.UserDTO;
import com.example.artworksharingplatform.repository.UserRepository;
import com.example.artworksharingplatform.service.AdminService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDTO updateUser(UserDTO updatedUser) {
        try {
            User userToUpdate = userRepository.findByEmailAddress(updatedUser.getEmailAddress())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            userToUpdate.setName(updatedUser.getUserName());
            userToUpdate.setTelephone(updatedUser.getTelephone());
            userToUpdate.setImagePath(updatedUser.getImagePath());
            userToUpdate.setStatus(updatedUser.getStatus());
            userRepository.save(userToUpdate);
            return findByEmailAddress(userToUpdate.getEmailAddress());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating user. Please try again later.");
        }
    }

    @Override
    public UserDTO getUserInfo(UUID userId) {
        User userToFind = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toUserDTO(userToFind);
    }

    @Override
    public List<UserDTO> viewAllUsers() {
        List<User> userList = userRepository.findAll();
        return userMapper.toList(userList);
    }

    @Override
    public UserDTO findByEmailAddress(String email) {
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        if (userDTO.getEmailAddress() == null || userDTO.getEmailAddress().isEmpty()) {
            throw new IllegalArgumentException("Email address cannot be null or empty");
        } else if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        } else if (userDTO.getUserName() == null || userDTO.getUserName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        } else if (userDTO.getStatus() == null || userDTO.getStatus().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        User addUser = User.builder()
                .name(userDTO.getUserName())
                .emailAddress(userDTO.getEmailAddress())
                .pass(passwordEncoder.encode(userDTO.getPassword()))
                .telephone((userDTO.getTelephone()))
                .imagePath((userDTO.getImagePath()))
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .status(userDTO.getStatus())
                .role(getRoleByRoleName(userDTO.getRoleName()))
                .build();
        userRepository.save(addUser);
        return findByEmailAddress(addUser.getEmailAddress());
    }

    public Role getRoleByRoleName(String roleName) {
        if (roleName.equalsIgnoreCase("admin")) {
            return Role.ADMIN;
        } else if (roleName.equalsIgnoreCase("creator")) {
            return Role.CREATOR;
        } else {
            return Role.AUDIENCE;
        }
    }
}
