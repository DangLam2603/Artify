package com.example.artworksharingplatform.service.impl;

import com.example.artworksharingplatform.entity.Role;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.repository.UserRepository;
import com.example.artworksharingplatform.service.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowingServiceImpl implements FollowingService {
    @Autowired
    UserRepository _userRepository;

    @Override
    public String Following(User audience, User creator) throws Exception {
        String mess = "";
        try {
            if (!creator.getRole().equals(Role.CREATOR)) {
                mess = "this is not creator";
                return mess;
            }
            List<User> listOfFollow = audience.getAudience();

            // Check if the creator is already followed
            boolean alreadyFollowing = listOfFollow.stream()
                    .anyMatch(user -> user.getId() == creator.getId());

            if (alreadyFollowing) {
                listOfFollow.removeIf(user -> user.getId() == creator.getId());
                mess = "unFollow Creator successfully";
                _userRepository.save(audience);
                return mess;
            } else {
                // Add the creator to the list of followed creators
                listOfFollow.add(creator);
                _userRepository.save(audience);
                mess = "You are now following the creator successfully.";
            }
        } catch (Exception e) {
            throw new Exception("An error occurred while following the creator: " + e.getMessage());
        }
        return mess;
    }

    @Override
    public Integer NumOfFollowing(User creator) throws Exception {
        try {
            List<User> listOfFollow = creator.getCreator();
            return listOfFollow.size();

        } catch (Exception e) {
            throw new Exception("An error occurred while following the creator: " + e.getMessage());
        }
    }

    @Override
    public Boolean IsFollow(User audience, User creator) throws Exception {
        boolean check = false;
        try {
            List<User> listOfFollow = audience.getAudience();
            // Check if the creator is already followed
            boolean alreadyFollowing = listOfFollow.stream()
                    .anyMatch(user -> user.getId() == creator.getId());
            if (alreadyFollowing) {
                check = true;
            }
            return check;

        } catch (Exception e) {
            throw new Exception("An error occurred while following the creator: " + e.getMessage());

        }
    }
    @Override
    public List<User> GetAllFollower(User audience, User creator) throws Exception {
        try {
            List<User> listOfFollow = creator.getCreator();
            return listOfFollow;
        } catch (Exception e) {
            throw new Exception("An error occurred while following the creator: " + e.getMessage());
        }
    }

}
