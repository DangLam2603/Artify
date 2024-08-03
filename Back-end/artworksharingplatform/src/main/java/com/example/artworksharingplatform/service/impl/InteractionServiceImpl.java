package com.example.artworksharingplatform.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.Interaction;
import com.example.artworksharingplatform.entity.Post;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.repository.InteractionRepository;
import com.example.artworksharingplatform.repository.PostRepository;
import com.example.artworksharingplatform.repository.UserRepository;
import com.example.artworksharingplatform.service.InteractionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InteractionServiceImpl implements InteractionService {

    @Autowired
    InteractionRepository interactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Override
    public List<Interaction> getInteractionsByPostId(UUID postId) {
        List<Interaction> interactionsList = interactionRepository.findByInteractionPost_Id(postId);
        return interactionsList;
    }

    @SuppressWarnings("null")
    @Override
    public Interaction likePost(UUID postId, UUID userId) {
        Interaction interaction = interactionRepository.findByInteractionPostIdAndInteractionAudienceId(postId, userId);

        if (interaction == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found"));

            Interaction newInteraction = new Interaction();
            newInteraction.setIsLiked(true);
            newInteraction.setInteractionAudience(user);
            newInteraction.setInteractionPost(post);
            interactionRepository.save(newInteraction);

            post.setNumberOfLikes(interactionRepository.countByIsLikedTrueAndInteractionPost_Id(postId));
            postRepository.save(post);

            return newInteraction;
        } else {
            interaction.setIsLiked(!interaction.getIsLiked());
            interactionRepository.save(interaction);

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found"));
            post.setNumberOfLikes(interactionRepository.countByIsLikedTrueAndInteractionPost_Id(postId));
            postRepository.save(post);

            return interaction;
        }
    }

    @Override
    public Interaction getInteractionByPostIdAndUserId(UUID postId, UUID userId) throws Exception {
        try {
            Interaction interaction = interactionRepository.findByInteractionPostIdAndInteractionAudienceId(postId,
                    userId);
            return interaction;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
