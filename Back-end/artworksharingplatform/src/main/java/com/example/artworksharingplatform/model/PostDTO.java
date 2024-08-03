package com.example.artworksharingplatform.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostDTO {
	private UUID postId;
	private String description;
	private String creatorName;
	private String emailAddress;
	private UUID creatorId;
	private int numberOfLikes;
	private List<ArtworkDTO> artList;
	private List<InteractionDTO> interactions;
	public Date publishDate;
}
