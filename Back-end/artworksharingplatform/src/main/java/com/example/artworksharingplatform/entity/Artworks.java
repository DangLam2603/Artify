package com.example.artworksharingplatform.entity;

import java.util.Date;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data

public class Artworks {

	@Id
	@UuidGenerator
	private UUID id;

	@Column(name = "Type")
	private String type;

	@Column(name = "Price")
	private float price;

	@Column(name = "Name")
	private String artName;

	@Column(name = "ImagePath", nullable = false, columnDefinition = "nvarchar(max)")
	private String imagePath;

	@Column(name = "Status")
	private String status;

	@Temporal(TemporalType.DATE)
	@Column(name = "CreatedDate")
	private Date createdDate;

	@OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Order> orders;

	@ManyToOne
	@JoinColumn(name = "PostID")
	private Post posts;

	@OneToOne(mappedBy = "preOrderArtwork", cascade = CascadeType.ALL)
	private PreOrder preOrder;
}
