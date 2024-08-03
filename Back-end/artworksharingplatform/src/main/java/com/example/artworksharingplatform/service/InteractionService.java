package com.example.artworksharingplatform.service;

import java.util.List;
import java.util.UUID;

import com.example.artworksharingplatform.entity.Interaction;

public interface InteractionService {
    List<Interaction> getInteractionsByPostId(UUID postId);

    Interaction likePost(UUID postId, UUID userId);

    Interaction getInteractionByPostIdAndUserId(UUID postId, UUID userId) throws Exception;
}
