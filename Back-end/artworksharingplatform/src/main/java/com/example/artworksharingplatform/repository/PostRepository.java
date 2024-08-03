package com.example.artworksharingplatform.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.artworksharingplatform.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
	List<Post> findByCreator_Id( UUID creatorId);
}
