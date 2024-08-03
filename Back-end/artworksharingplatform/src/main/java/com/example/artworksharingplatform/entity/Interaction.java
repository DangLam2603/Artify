package com.example.artworksharingplatform.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Interactions")
public class Interaction {

	@Id
	@UuidGenerator
	private UUID id;

	@Column(name = "IsLiked")
	private Boolean isLiked;

	@ManyToOne
	@JoinColumn(name = "AudienceID")
	private User interactionAudience;

	@ManyToOne
	@JoinColumn(name = "PostID")
	private Post interactionPost;

	@OneToMany(mappedBy = "interaction", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Comment> comments;
}
