package com.example.artworksharingplatform.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.artworksharingplatform.entity.Role;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.mapper.UserMapper;
import com.example.artworksharingplatform.model.ApiResponse;
import com.example.artworksharingplatform.model.UserDTO;
import com.example.artworksharingplatform.repository.UserRepository;
import com.example.artworksharingplatform.service.CloudinaryService;
import com.example.artworksharingplatform.service.UserService;

@RestController
@RequestMapping("api/auth")
@PreAuthorize("hasRole('ROLE_AUDIENCE') or hasRole('ROLE_CREATOR') or hasRole('ROLE_ADMIN')")
public class UserController {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository _userRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    @GetMapping("user/profile")
    public ResponseEntity<ApiResponse<UserDTO>> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse<UserDTO> apiResponse = new ApiResponse<UserDTO>();
        try {
            if (isUserAuthenticated(authentication)) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String email = userDetails.getUsername(); // getUserName này là email
                UserDTO userInfo = userService.findByEmailAddress(email);
                if (userInfo != null) {
                    apiResponse.ok(userInfo);
                    return ResponseEntity.ok(apiResponse);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            }
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isUserAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails;
    }

    @PutMapping("user/profile")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestPart(value = "user") UserDTO updatedUser,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestPart(value = "imagePath", required = false) String imagePath) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse<UserDTO> apiResponse = new ApiResponse<UserDTO>();
        try {
            if (isUserAuthenticated(authentication)) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String email = userDetails.getUsername();
                updatedUser.setImagePath(null);
                if (file != null) {
                    updatedUser.setImagePath(uploadImage(file));
                } else {
                    updatedUser.setImagePath(imagePath);
                }
                updatedUser.setEmailAddress(email);
                UserDTO userInfo = userService.updateUser(updatedUser);
                if (userInfo != null) {
                    apiResponse.ok(userInfo);
                    return ResponseEntity.ok(apiResponse);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            }
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("requestBecomeCreator")
    public ResponseEntity<ApiResponse<User>> RequestBecomeCreator() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse<User> apiResponse = new ApiResponse<>();
        try {
            if (auth.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                String email = userDetails.getUsername();
                User userInfo = userService.findByEmail(email);
                userInfo.setStatus("REQUESTING");
                User user = _userRepository.save(userInfo);
                apiResponse.ok(user);
            }
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("user/profile/password/test")
    public ResponseEntity<ApiResponse<UserDTO>> checkUserPasswordTest(@RequestBody UserDTO userDTO) {
        ApiResponse<UserDTO> apiResponse = new ApiResponse<UserDTO>();
        apiResponse.ok(userDTO);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("user/profile/password")
    public ResponseEntity<ApiResponse<Boolean>> checkUserPassword(@RequestBody UserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse<Boolean> apiResponse = new ApiResponse<Boolean>();
        try {
            if (isUserAuthenticated(authentication)) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String email = userDetails.getUsername();
                boolean checkUserPassword = userService.checkUserPassword(email, userDTO.getPassword());
                apiResponse.ok(checkUserPassword);
                return ResponseEntity.ok(apiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("user/profile/password")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserPassword(@RequestBody UserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse<UserDTO> apiResponse = new ApiResponse<UserDTO>();
        try {
            if (isUserAuthenticated(authentication)) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String email = userDetails.getUsername();
                User updatedUserPassword = userService.updateUserPassword(email, userDTO.getPassword());
                apiResponse.ok(userMapper.toUserDTO(updatedUserPassword));
                return ResponseEntity.ok(apiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("creatorList")
    public ResponseEntity<ApiResponse<List<UserDTO>>> viewAllCreators() {
        ApiResponse<List<UserDTO>> apiResponse = new ApiResponse<List<UserDTO>>();
        try {
            Role role = Role.CREATOR;
            List<User> creatorList = userService.filterByRole(role);
            apiResponse.ok(userMapper.toList(creatorList));
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings("rawtypes")
    public String uploadImage(MultipartFile file) {
        Map data = cloudinaryService.upload(file);
        String url = data.get("url").toString();
        return url;
    }

    @GetMapping("/hello")
    public String testUser() {
        return "Hello";
    }
}
