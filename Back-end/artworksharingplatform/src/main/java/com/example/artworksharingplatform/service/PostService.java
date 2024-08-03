package com.example.artworksharingplatform.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.entity.Post;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.model.ArtworkDTO;
import com.example.artworksharingplatform.model.PostDTO;

@Service
public interface PostService {
    public List<Post> getAllPosts();
	public void addArtwork(Artworks artwork);
	public void deleteArtwork(UUID id) throws Exception;
	public Post addPost(PostDTO post,User creator);
	public Post addPostTest(PostDTO post);
	public List<Artworks> convertArtList(List<ArtworkDTO> artsDTO,Post post);
	public List<Artworks> convertArtUpdate(List<ArtworkDTO> artsDTO);
	public PostDTO getPostById(UUID id);
	public ArtworkDTO getArtByArtId(UUID artId);
	public Post updatePost(PostDTO postDTO);
	public List<PostDTO> getPostsByCreatorId(UUID creatorId);
	public void deletePost(UUID postId);
	// public void delete
} 