package com.example.artworksharingplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.entity.Post;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.mapper.ArtworkMapper;
import com.example.artworksharingplatform.mapper.PostMapper;
import com.example.artworksharingplatform.model.ApiResponse;
import com.example.artworksharingplatform.model.ArtworkDTO;
import com.example.artworksharingplatform.model.PostDTO;
import com.example.artworksharingplatform.model.UserDTO;
import com.example.artworksharingplatform.service.ArtworkService;
import com.example.artworksharingplatform.service.CloudinaryService;
import com.example.artworksharingplatform.service.PostService;
import com.example.artworksharingplatform.service.UserService;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("api/auth")
public class PostController {

	@Autowired
	PostService postService;

	@Autowired
	PostMapper postMapper;

	@Autowired
	ArtworkService artworkService;

	@Autowired
	ArtworkMapper artworkMapper;

	@Autowired
	UserService userService;

	@Autowired
    private CloudinaryService cloudinaryService;

	@GetMapping("audience/viewAllPosts")
	@PreAuthorize("hasRole('ROLE_AUDIENCE') or hasRole('ROLE_CREATOR')")
	public ResponseEntity<ApiResponse> viewAllPosts() {
		ApiResponse apiResponse = new ApiResponse();
		try {
			List<Post> posts = postService.getAllPosts();
			List<PostDTO> postDTOs = postMapper.toList(posts);
			apiResponse.ok(postDTOs);
			return ResponseEntity.ok(apiResponse);
		} catch (Exception e) {
			apiResponse.error(e);
			return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("audience/viewAllArt")
	public ResponseEntity<ApiResponse> viewArts() {
		ApiResponse apiResponse = new ApiResponse();
		List<Artworks> artworks = new ArrayList<Artworks>();
		
		try{
			artworks = artworkService.getAllArtworks();
			List<ArtworkDTO> result = artworkMapper.toArtworkDTOs(artworks);
			apiResponse.ok(result);
			return ResponseEntity.ok(apiResponse);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseEntity.ok(apiResponse);
		}
	}

	@PostMapping("creator/addPost")
    @PreAuthorize("hasRole('ROLE_CREATOR')")
	public ResponseEntity<ApiResponse> addArtwork(@RequestPart("image") List<MultipartFile> files,
	@RequestPart("post") PostDTO postDTO){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		ApiResponse apiResponse = new ApiResponse();
		try{
			if (isUserAuthenticated(authentication)) {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				String email = userDetails.getUsername();
				UserDTO userInfo = userService.findByEmailAddress(email);
				User creator = userService.getUserById(userInfo.getUserId());
				Post savedPost = postService.addPost(postDTO,creator);
				List<ArtworkDTO> artsDTO = postDTO.getArtList();
				List<Artworks> artworks = postService.convertArtList(artsDTO, savedPost);
				for (int i = 0; i < files.size(); i++) {
					MultipartFile file = files.get(i);
					Artworks artwork = artworks.get(i);
					Map<String, Object> data = cloudinaryService.upload(file);
					String url = data.get("url").toString();
					artwork.setImagePath(url);
					artwork.setStatus("Active");
					postService.addArtwork(artwork);
				}
				PostDTO result = postService.getPostById(savedPost.getId());
				apiResponse.ok(result);
				return ResponseEntity.ok(apiResponse);
			}else{
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
			}

		}catch (Exception e){
			apiResponse.error(e.getMessage());
			return ResponseEntity.ok(apiResponse);
		}  
	}

	private boolean isUserAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails;
    }

	@GetMapping("getPostById")
	public ResponseEntity<ApiResponse> getPostById(@RequestParam("postId") String postId) {
		ApiResponse apiResponse = new ApiResponse();
		try{
			UUID convertedPostId = UUID.fromString(postId);
			PostDTO result = postService.getPostById(convertedPostId);
			if(result == null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
			}
			apiResponse.ok(result);
			return ResponseEntity.ok(apiResponse);
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}
	}

	@GetMapping("getArtById")
	public ResponseEntity<ApiResponse> getArtById(@RequestParam("artId") String artId){
		ApiResponse apiResponse = new ApiResponse();
		try{
			UUID convertedArtId = UUID.fromString(artId);
			ArtworkDTO art = postService.getArtByArtId(convertedArtId);
			if(art == null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
			}
			apiResponse.ok(art);
			return ResponseEntity.ok(apiResponse);
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}
	}

	@PutMapping("updatePost")
	public ResponseEntity<ApiResponse> updatePost(@RequestPart("image") List<MultipartFile> files,
	@RequestPart("post") PostDTO postDTO){
		ApiResponse apiResponse = new ApiResponse();
		try{
			Post updatedPost = postService.updatePost(postDTO);
			List<Artworks> resultArtworks = postService.convertArtList(postDTO.getArtList(), updatedPost);
			for (int i = 0; i < files.size(); i++) {
				MultipartFile file = files.get(i);
				if(file != null){
					Artworks artwork = resultArtworks.get(i);
					Map<String, Object> data = cloudinaryService.upload(file);
					String url = data.get("url").toString();
					artwork.setImagePath(url);
					postService.addArtwork(artwork);
				}
			}
			if(updatedPost == null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
			}
			PostDTO result = postService.getPostById(updatedPost.getId());
			apiResponse.ok(result);
			return ResponseEntity.ok(apiResponse);
		}catch(Exception e){
			apiResponse.error(e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}
	}

	@GetMapping("downloadArt")
	public ResponseEntity<ApiResponse> downloadArt(@RequestParam("artId") String artId){
		ApiResponse apiResponse = new ApiResponse();
		try{
			UUID convertedArtId = UUID.fromString(artId);
			ArtworkDTO art = postService.getArtByArtId(convertedArtId);
			if(art == null){
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
			}
			apiResponse.ok(art.getImagePath());
			return ResponseEntity.ok(apiResponse);
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}
	}

	@GetMapping("getPostByCreator")
	public ResponseEntity<ApiResponse> getPostByCreator(@RequestParam("creatorId") String creatorId){
		ApiResponse apiResponse = new ApiResponse(); 
		UUID convertedArtId = UUID.fromString(creatorId);
		List<PostDTO> result = postService.getPostsByCreatorId(convertedArtId);
		apiResponse.ok(result);
		return ResponseEntity.ok(apiResponse);
	}
	
	@DeleteMapping("deletePost")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CREATOR')")
	public ResponseEntity<ApiResponse> deletePost(@RequestParam("postId") UUID postId) {
		ApiResponse apiResponse = new ApiResponse(); 
		postService.deletePost(postId);
		apiResponse.ok("Delete Sucessfully");
		return ResponseEntity.ok(apiResponse);
	}
	
	@DeleteMapping("deleArtInPost")
	public ResponseEntity<ApiResponse> deleteArt(@RequestParam("artId") UUID artId) throws Exception{
		ApiResponse apiResponse = new ApiResponse(); 
		postService.deleteArtwork(artId);
		apiResponse.ok("Delete Sucessfully");
		return ResponseEntity.ok(apiResponse);
	}
	


}
