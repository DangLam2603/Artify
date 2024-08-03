package com.example.artworksharingplatform.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.artworksharingplatform.entity.Artworks;

public interface ArtworkRepository extends JpaRepository<Artworks, UUID> {
    List<Artworks> findByPosts_Creator_IdAndOrdersIsNotEmpty(UUID creatorId);
}
