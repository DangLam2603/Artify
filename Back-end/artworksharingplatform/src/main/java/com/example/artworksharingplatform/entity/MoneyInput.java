package com.example.artworksharingplatform.entity;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

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
@Table(name = "MoneyInput")
@Data
public class MoneyInput {

	@Id
	@UuidGenerator
    private UUID id;

	@Column(name = "MoneyAmount", nullable = false)
	private float money;

	@Column(name = "Description")
	private String description;

	@Column(name = "Status")
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="InputDate")
	private Timestamp inputDate;
	
	@ManyToOne
	@JoinColumn(name="UserID")
	private User userInputs;

	@OneToOne(mappedBy = "moneyInput")
    private Transaction transactions;

}
