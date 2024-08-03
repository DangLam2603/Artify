package com.example.artworksharingplatform.entity;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "Orders")
public class Order {

	@Id
	@UuidGenerator
	private UUID id;

	@Column(name = "TotalPrice")
	private float totalPrice;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OrderDate")
	private Timestamp orderDate;

	@ManyToOne
	@JoinColumn(name = "Audience")
	private User audience;

	@ManyToOne
	@JoinColumn(name = "ArtworkId")
	private Artworks artwork;

	@ManyToOne
	@JoinColumn(name = "TransactionId")
	private Transaction transactions;

}
