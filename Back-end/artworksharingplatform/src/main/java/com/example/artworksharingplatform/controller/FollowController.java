package com.example.artworksharingplatform.controller;

import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.mapper.UserMapper;
import com.example.artworksharingplatform.model.ApiResponse;
import com.example.artworksharingplatform.model.UserDTO;
import com.example.artworksharingplatform.service.FollowingService;
import com.example.artworksharingplatform.service.UserService;
import com.example.artworksharingplatform.service.impl.FollowingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/auth")
public class FollowController {

    @Autowired
    UserService userService;
    @Autowired
    FollowingServiceImpl _followService;
    @Autowired
    UserMapper _userMapper;

    @PostMapping("follow")
    // @PreAuthorize("hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<String>> following(@RequestParam("creatorId") UUID creatorId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = new ApiResponse();
        if (isUserAuthenticated(authentication)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User audience = userService.findByEmail(email);
            User creator = userService.getUserById(creatorId);
            String result = _followService.Following(audience, creator);
            apiResponse.ok(result);
            return ResponseEntity.ok(apiResponse);
        } else {
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("number_of_follow")
    public ResponseEntity<ApiResponse<Integer>> NumberOfFollower(@RequestParam UUID CreatorId) throws Exception {
        ApiResponse<Integer> apiResponse = new ApiResponse<>();
        try {
            User creator = userService.getUserById(CreatorId);
            Integer numberOfFollower = _followService.NumOfFollowing(creator);
            apiResponse.ok(numberOfFollower);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isUserAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails;
    }

    @GetMapping("is_Following")
    @PreAuthorize("hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<String>> IsFollowing(@RequestParam UUID creatorId) throws Exception {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mess = "";
        try {
            if (isUserAuthenticated(authentication)) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String email = userDetails.getUsername();
                User audience = userService.findByEmail(email);
                User creator = userService.getUserById(creatorId);
                Boolean result = _followService.IsFollow(audience, creator);
                if (result) {
                    mess = "You have followed this creator";
                }else {
                    mess= "You did not follow this creator";
                }
                apiResponse.ok(mess);
                return ResponseEntity.ok(apiResponse);
            } else {
                return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("get_all_follower")
    @PreAuthorize("hasRole('ROLE_AUDIENCE')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllFollower(@RequestParam UUID creatorId) throws Exception{
        ApiResponse<List<UserDTO>> apiResponse = new ApiResponse<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mess = "";
        try {
            if (isUserAuthenticated(authentication)) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String email = userDetails.getUsername();
                User audience = userService.findByEmail(email);
                User creator = userService.getUserById(creatorId);
                List<User> ListUser = _followService.GetAllFollower(audience, creator);

                if (ListUser == null){
                    apiResponse.error("List is null");
                    return ResponseEntity.ok(apiResponse);
                }
                List<UserDTO> listUserDto = _userMapper.toList(ListUser);

                apiResponse.ok(listUserDto);
                return ResponseEntity.ok(apiResponse);
            } else {
                return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
