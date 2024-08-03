package com.example.artworksharingplatform.service;

import java.util.List;

import com.example.artworksharingplatform.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface FollowingService {
    public String Following(User audience, User creator) throws Exception;

    public Integer NumOfFollowing(User creator) throws Exception;

    public Boolean IsFollow(User audience, User creator) throws Exception;
    public List<User> GetAllFollower(User audience, User creator) throws Exception;
}
