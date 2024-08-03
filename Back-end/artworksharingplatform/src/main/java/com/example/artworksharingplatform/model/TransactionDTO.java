package com.example.artworksharingplatform.model;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter
@Setter
@Data
public class TransactionDTO {
    private UUID userId;
    private float totalMoney;
    private Timestamp transactionDate;
    private String description;
	public UUID transactionId;
	public MoneyInputDTO input_money;
}