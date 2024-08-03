package com.example.artworksharingplatform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String userName;
    private String emailAddress;
    private String pass;
    private String telephone;
    // private Timestamp createdDate;
}
