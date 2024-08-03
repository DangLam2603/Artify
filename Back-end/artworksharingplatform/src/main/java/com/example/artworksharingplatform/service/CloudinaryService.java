package com.example.artworksharingplatform.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.service.impl.CloudinaryServiceImpl;

@Service
public interface CloudinaryService {
    
    public List<Artworks> getAllArtworks();

	public Artworks geArtworksById(UUID artworkId);

	public List<Artworks> getSoldArtworks(UUID creatorId);
    public Map upload(MultipartFile file);
}
