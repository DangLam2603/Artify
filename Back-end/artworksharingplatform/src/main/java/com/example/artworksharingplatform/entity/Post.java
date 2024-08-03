package com.example.artworksharingplatform.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "Posts")
public class Post {

	@Id
	@UuidGenerator
	private UUID id;

	@Temporal(TemporalType.DATE)
	@Column(name = "PublishDate")
	private Date publishDate;

	@Column(name = "NumberOfLikes")
	private int numberOfLikes;

	@Column(name = "Description", columnDefinition = "nvarchar(max)")
	private String description;

	@OneToMany(mappedBy = "posts", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Artworks> artworks;

	@ManyToOne
	@JoinColumn(name = "CreatorID")
	private User creator;

	@OneToMany(mappedBy = "interactionPost", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Interaction> postsInteraction;

}
