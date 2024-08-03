package com.example.artworksharingplatform.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.Role;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.model.UserDTO;

/**
 * UserServiceImpl
 */
@Service
public interface UserService {
    UserDTO updateUser(UserDTO updatedUser);

    UserDTO getUserInfo(UUID userId);

    User getUserById(UUID userId);

    UserDTO findByEmailAddress(String email);

    User findByEmail(String email);

    User getUser(UUID userId);

    boolean checkUserPassword(String email, String passwordToCheck);

    User updateUserPassword(String email, String passwordToUpdate);

    List<User> searchUserByName(String name) throws Exception;

    List<User> filterByRole(Role role);

    List<User> sortUserByCreatedDate(String sortBy);

    List<User> getUsersList();
    List<User> getListRequest() throws Exception;
}