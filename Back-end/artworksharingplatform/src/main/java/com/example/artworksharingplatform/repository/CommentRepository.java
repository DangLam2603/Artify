package com.example.artworksharingplatform.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.artworksharingplatform.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByInteraction_Id(UUID interactionId);

    List<Comment> findAllByInteraction_InteractionPost_Id(UUID postId);
}
