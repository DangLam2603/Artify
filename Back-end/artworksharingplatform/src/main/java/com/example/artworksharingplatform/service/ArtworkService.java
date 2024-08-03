package com.example.artworksharingplatform.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.repository.ArtworkRepository;
import com.example.artworksharingplatform.service.impl.ArtworkServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ArtworkService implements ArtworkServiceImpl {

	@Autowired
	ArtworkRepository artworkRepository;

	@Override
	public List<Artworks> getAllArtworks() {
		// TODO Auto-generated method stub
		List<Artworks> list = artworkRepository.findAll();
		return list;
	}

	@SuppressWarnings("null")
	@Override
	public Artworks geArtworksById(UUID artworkId) {
		Artworks artwork = artworkRepository.findById(artworkId)
				.orElseThrow(() -> new EntityNotFoundException("Artwork not found"));
		return artwork;
	}

	@Override
	public List<Artworks> getSoldArtworks(UUID creatorId) {
		return artworkRepository.findByPosts_Creator_IdAndOrdersIsNotEmpty(creatorId);
	}

}
