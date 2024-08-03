package com.example.artworksharingplatform.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.Comment;
import com.example.artworksharingplatform.entity.Interaction;
import com.example.artworksharingplatform.entity.Post;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.repository.CommentRepository;
import com.example.artworksharingplatform.repository.InteractionRepository;
import com.example.artworksharingplatform.repository.PostRepository;
import com.example.artworksharingplatform.repository.UserRepository;
import com.example.artworksharingplatform.service.CommentService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    InteractionRepository interactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @SuppressWarnings("null")
    @Override
    public Comment getCommentById(UUID commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment comment = optionalComment.orElse(null);
        return comment;
    }

    @SuppressWarnings("null")
    @Override
    public void deleteComment(UUID commentId) {
        commentRepository.deleteById(commentId);
    }

    @SuppressWarnings("null")
    @Override
    public Comment addComment(Comment comment, UUID postId, UUID userId) {
        Interaction interaction = interactionRepository.findByInteractionPostIdAndInteractionAudienceId(postId, userId);

        if (interaction == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found"));

            Interaction newInteraction = new Interaction();
            newInteraction.setIsLiked(false);
            newInteraction.setInteractionAudience(user);
            newInteraction.setInteractionPost(post);
            interactionRepository.save(newInteraction);

            comment.setInteraction(newInteraction);
        } else {
            comment.setInteraction(interaction);
        }

        return commentRepository.save(comment);
    }

    @SuppressWarnings("null")
    @Override
    public Comment ediComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsListByPostId(UUID postId) {
        return commentRepository.findAllByInteraction_InteractionPost_Id(postId);
    }
}
