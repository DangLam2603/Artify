package com.example.artworksharingplatform.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.artworksharingplatform.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
	List<Transaction> findByUser_id(UUID userId);
}
