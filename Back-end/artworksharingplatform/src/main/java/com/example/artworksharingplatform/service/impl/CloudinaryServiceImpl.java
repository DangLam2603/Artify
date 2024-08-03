package com.example.artworksharingplatform.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cloudinary.Cloudinary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.service.CloudinaryService;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
	@Autowired
    private Cloudinary cloudinary;

    @Override
    public Map upload(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public List<Artworks> getAllArtworks() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAllArtworks'");
	}

	@Override
	public Artworks geArtworksById(UUID artworkId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'geArtworksById'");
	}

	@Override
	public List<Artworks> getSoldArtworks(UUID creatorId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getSoldArtworks'");
	}
}
