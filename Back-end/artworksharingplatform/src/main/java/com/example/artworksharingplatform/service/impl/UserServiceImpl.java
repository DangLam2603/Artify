package com.example.artworksharingplatform.service.impl;

import java.util.ArrayList;
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
import com.example.artworksharingplatform.service.UserService;

import jakarta.persistence.EntityNotFoundException;

/**
 * UserService
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder _passwordEncoder;

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
    public User getUserById(UUID userId) {
        User userToFind = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userToFind;
    }

    @Override
    public UserDTO findByEmailAddress(String email) {
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toUserDTO(user);
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user;
    }

    @Override
    public User getUser(UUID userId) {
        User userToFind = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userToFind;
    }

    @Override
    public boolean checkUserPassword(String email, String passwordToCheck) {
        try {
            User user = userRepository.findByEmailAddress(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            if (_passwordEncoder.matches(passwordToCheck, user.getPassword())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error running the method.");
        }
    }

    @Override
    public User updateUserPassword(String email, String passwordToUpdate) {
        try {
            User user = userRepository.findByEmailAddress(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            user.setPass(_passwordEncoder.encode(passwordToUpdate));
            userRepository.save(user);
            return findByEmail(user.getEmailAddress());
        } catch (Exception ex) {
            throw new RuntimeException("Error running the method.");
        }
    }

    @Override
    public List<User> searchUserByName(String name) throws Exception {
        try {
            List<User> users = new ArrayList<>();
            Role role = Role.ADMIN;

            if (name != null) {
                users = userRepository.findByRoleNotAndNameContainingIgnoreCase(role, name);
            } else {
                users = userRepository.findByRoleNot(role);
            }

            return users;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<User> filterByRole(Role role) {
        List<User> users = userRepository.findAllByRole(role);
        return users;
    }

    @Override
    public List<User> sortUserByCreatedDate(String sortBy) {
        Role role = Role.ADMIN;
        List<User> users;

        switch (sortBy.toLowerCase()) {
            case "asc":
                users = userRepository.findByRoleNotOrderByCreatedDateAsc(role);
                break;

            case "desc":
                users = userRepository.findByRoleNotOrderByCreatedDateDesc(role);
                break;
            default:
                users = new ArrayList<>();
                break;
        }

        return users;
    }

    @Override
    public List<User> getUsersList() {
        Role role = Role.ADMIN;
        List<User> users = userRepository.findByRoleNot(role);
        return users;
    }

    @Override
    public List<User> getListRequest() throws Exception {
        try {
            List<User> list = userRepository.findByStatus("REQUESTING");
            return list;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}