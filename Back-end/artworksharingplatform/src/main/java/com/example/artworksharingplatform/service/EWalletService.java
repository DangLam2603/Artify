package com.example.artworksharingplatform.service;

import java.util.UUID;

import com.example.artworksharingplatform.entity.EWallet;

public interface EWalletService {
	public void createWallet(EWallet eWallet);

	public EWallet getWalletByUserId(UUID userId);

	public void updateWallet(EWallet eWallet);

	public boolean isEnoughMoney(UUID userId, float price);

	public void updateAdminWallet(float totalMoney);

	public void processingAdminWallet(float totalMoney);

	public void updateCreatorWallet(UUID creatorId, float totalMoney);

	public void updateAudienceWallet(UUID audienceId, float totalMoney);
}
