package com.example.artworksharingplatform.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.artworksharingplatform.entity.Interaction;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, UUID> {
    List<Interaction> findByInteractionPost_Id(UUID postId);

    Interaction findByInteractionPostIdAndInteractionAudienceId(UUID postId, UUID userId);

    int countByIsLikedTrueAndInteractionPost_Id(UUID postId);
}
