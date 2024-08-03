package com.example.artworksharingplatform.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.artworksharingplatform.entity.EWallet;

public interface EWalletRepository extends JpaRepository<EWallet,UUID> {
	EWallet findByUser_Id(UUID userId);
}
