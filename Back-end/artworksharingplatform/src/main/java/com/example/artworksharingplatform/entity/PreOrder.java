package com.example.artworksharingplatform.entity;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "PreOrders")
public class PreOrder {
	@Id
	@UuidGenerator
	private UUID id;

	@Column(name = "Status")
	private String status;

	@Column(name = "Price")
	private float price;

	@Column(name = "Requirement", columnDefinition = "nvarchar(max)")
	private String requirement;

	@Column(name = "AudienceRating")
	private int audienceRating;

	@Column(name = "AudienceFeedback", columnDefinition = "nvarchar(max)")
	private String audienceFeedback;

	@Column(name = "CreatorNote", columnDefinition = "nvarchar(max)")
	private String creatorNote;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PreOrderDate")
	private Timestamp preOrderDate;

	@ManyToOne
	@JoinColumn(name = "AudienceID")
	private User preOrderAudience;

	@ManyToOne
	@JoinColumn(name = "CreatorID")
	private User preOrderCreator;

	@ManyToOne
	@JoinColumn(name = "TransactionId")
	private Transaction transactions;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ArtworkId", referencedColumnName = "id")
	private Artworks preOrderArtwork;
}
