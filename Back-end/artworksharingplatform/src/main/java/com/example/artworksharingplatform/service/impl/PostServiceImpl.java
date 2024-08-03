package com.example.artworksharingplatform.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.entity.Post;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.mapper.ArtworkMapper;
import com.example.artworksharingplatform.mapper.PostMapper;
import com.example.artworksharingplatform.model.ArtworkDTO;
import com.example.artworksharingplatform.model.PostDTO;
import com.example.artworksharingplatform.repository.ArtworkRepository;
import com.example.artworksharingplatform.repository.PostRepository;
import com.example.artworksharingplatform.service.PostService;
import com.example.artworksharingplatform.service.impl.PostServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostServiceImpl implements PostService{
	
    @Autowired
    PostRepository postRepository;

    @Autowired
    ArtworkRepository artworkRepository;

    @Autowired
    PostMapper postMapper;

    @Autowired
    ArtworkMapper artworkMapper;

    @Override
    public List<Post> getAllPosts() {
        // TODO Auto-generated method stub
        List<Post> postList = postRepository.findAll();
        return postList;
    }

    @Override
    public void addArtwork(Artworks artwork) {
        artworkRepository.save(artwork);
    }

    @Override
    public void deleteArtwork(UUID id) throws Exception {
        try {
            artworkRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Post addPost(PostDTO postDTO, User creator){
        // TODO Auto-generated method stub
       Post post = new Post();
       post.setDescription(postDTO.getDescription());
       post.setNumberOfLikes(postDTO.getNumberOfLikes());
       post.setCreator(creator);
       Date date = new Date();
       post.setPublishDate(date);
       Post result = postRepository.save(post);
       return result;
    }

    @Override
    public List<Artworks> convertArtList(List<ArtworkDTO> artsDTO,Post post) {
        List<Artworks> result = new ArrayList<Artworks>();
        for(ArtworkDTO artDTO : artsDTO) {
            Artworks art = new Artworks();
            art.setArtName(artDTO.getArtName());
            art.setType(artDTO.getType());
            art.setPrice(artDTO.getPrice());
            art.setImagePath(artDTO.getImagePath()); 
            art.setStatus(artDTO.getStatus());
            art.setId(artDTO.getArtId());
            art.setPosts(post);
            Date date = new Date();
            art.setCreatedDate(date);
            result.add(art);
        }
        return result;
    }

    @Override
    public PostDTO getPostById(UUID id) {
        // TODO Auto-generated method stub
       Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post Not Found"));
       return postMapper.toPostDTO(post);
    }

    @Override
    public ArtworkDTO getArtByArtId(UUID artId) {
        Artworks art = artworkRepository.findById(artId).orElseThrow(() -> new EntityNotFoundException("Art Not Found"));
        return artworkMapper.toArtworkDTO(art);
    }

    @Override
    public List<Artworks> convertArtUpdate(List<ArtworkDTO> artsDTO) {
        List<Artworks> result = new ArrayList<Artworks>();
        for(ArtworkDTO artDTO : artsDTO) {
            Artworks art = new Artworks();
            art.setArtName(artDTO.getArtName());
            art.setType(artDTO.getType());
            art.setPrice(artDTO.getPrice());
            art.setImagePath(artDTO.getImagePath()); 
            art.setStatus(artDTO.getStatus());
            art.setId(artDTO.getArtId());
            result.add(art);
        }
        return result;
    }

    @Override
    public Post updatePost(PostDTO postDTO) {
        Post post = postRepository.findById(postDTO.getPostId()).orElseThrow(() -> new EntityNotFoundException("Post Not Found"));
        post.setDescription(postDTO.getDescription());
        post.setNumberOfLikes(postDTO.getNumberOfLikes());
        Date date = new Date();
        post.setPublishDate(date);
        Post result = postRepository.save(post);
        return result;
    }

    @Override
    public Post addPostTest(PostDTO postDTO) {
        Post post = new Post();
        post.setDescription(postDTO.getDescription());
        post.setNumberOfLikes(postDTO.getNumberOfLikes());
        Date date = new Date();
        post.setPublishDate(date);
        Post result = postRepository.save(post);
        return result;
    }

    @Override
    public List<PostDTO> getPostsByCreatorId(UUID creatorId) {
        List<Post> posts =  postRepository.findByCreator_Id(creatorId);
        return postMapper.toList(posts);
    }

    @Override
    public void deletePost(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post Not Found"));
        postRepository.delete(post);
    }

}
