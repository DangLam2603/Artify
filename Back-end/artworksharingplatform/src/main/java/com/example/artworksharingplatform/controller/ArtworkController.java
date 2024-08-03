package com.example.artworksharingplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.mapper.ArtworkMapper;
import com.example.artworksharingplatform.model.ApiResponse;
import com.example.artworksharingplatform.model.ArtworkDTO;
import com.example.artworksharingplatform.service.ArtworkService;
import com.example.artworksharingplatform.service.UserService;

@RestController
@RequestMapping("api/auth/artwork")
public class ArtworkController {

	@Autowired
	ArtworkService artworkService;

	@Autowired
	UserService userService;

	@Autowired
	ArtworkMapper artworkMapper;

	@GetMapping("/test")
	public String Test() {
		return "aaaa";
	}

	@GetMapping("/viewAll")
	public List<Artworks> getAllArts() {
		return artworkService.getAllArtworks();
	}

	@GetMapping("viewSoldArtworks")
	@PreAuthorize("hasRole('ROLE_CREATOR')")
	public ResponseEntity<ApiResponse<List<ArtworkDTO>>> viewSoldArtworks() {
		ApiResponse<List<ArtworkDTO>> apiResponse = new ApiResponse<List<ArtworkDTO>>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String email = userDetails.getUsername();
		User user = userService.findByEmail(email);

		try {
			List<Artworks> artworks = artworkService.getSoldArtworks(user.getId());
			List<ArtworkDTO> artworkDTOs = artworkMapper.toArtworkDTOs(artworks);

			apiResponse.ok(artworkDTOs);
			return ResponseEntity.ok(apiResponse);

		} catch (Exception e) {
			apiResponse.error(e);
			return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
		}
	}

}
