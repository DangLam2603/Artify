package com.example.artworksharingplatform.entity;

import java.sql.Timestamp;
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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "Transactions")
@Data
public class Transaction {

	@Id
	@UuidGenerator
	private UUID id;

	@Column(name = "TotalMoney")
	private float totalMoney;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TransactionDate")
	private Timestamp transactionDate;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne
	@JoinColumn(name = "money_input_id", referencedColumnName = "id")
	private MoneyInput moneyInput;

	@OneToMany(mappedBy = "transactions", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Order> orders;

	@OneToMany(mappedBy = "transactions", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<PreOrder> preOrders;

}
