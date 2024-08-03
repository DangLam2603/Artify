package com.example.artworksharingplatform.service.impl;

import java.util.List;
import java.util.UUID;

import com.example.artworksharingplatform.entity.Artworks;

public interface ArtworkServiceImpl {
	public List<Artworks> getAllArtworks();

	public Artworks geArtworksById(UUID artworkId);

	public List<Artworks> getSoldArtworks(UUID creatorId);
}
