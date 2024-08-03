package com.example.artworksharingplatform.model;

import java.util.UUID;

import lombok.Data;

@Data
public class UserDTO {
    private UUID userId;
    private String userName;
    private String emailAddress;
    private String telephone;
    private String imagePath;
    private String password;
    private String status;
    private String roleName;
}