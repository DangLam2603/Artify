package com.example.artworksharingplatform.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.EWallet;
import com.example.artworksharingplatform.entity.Role;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.repository.EWalletRepository;
import com.example.artworksharingplatform.repository.UserRepository;
import com.example.artworksharingplatform.service.EWalletService;

@Service
public class EWalletServiceImpl implements EWalletService {

	@Autowired
	EWalletRepository eWalletRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public void createWallet(EWallet eWallet) {
		eWalletRepository.save(eWallet);

	}

	@Override
	public EWallet getWalletByUserId(UUID userId) {
		return eWalletRepository.findByUser_Id(userId);
	}

	@Override
	public void updateWallet(EWallet eWallet) {
		// TODO Auto-generated method stub
		eWalletRepository.save(eWallet);

	}

	@Override
	public boolean isEnoughMoney(UUID userId, float price) {
		EWallet eWallet = getWalletByUserId(userId);
		if (eWallet.getTotalAmount() >= price) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updateAdminWallet(float totalMoney) {
		Role role = Role.ADMIN;
		User admin = userRepository.findByRole(role);
		EWallet eWallet = getWalletByUserId(admin.getId());

		float newBalance = (float) (eWallet.getTotalAmount() + totalMoney * 0.037);
		eWallet.setTotalAmount(newBalance);

		eWalletRepository.save(eWallet);
	}

	@Override
	public void processingAdminWallet(float totalMoney) {
		Role role = Role.ADMIN;
		User admin = userRepository.findByRole(role);
		EWallet eWallet = getWalletByUserId(admin.getId());

		float newBalance = (float) (eWallet.getTotalAmount() + totalMoney);
		eWallet.setTotalAmount(newBalance);

		eWalletRepository.save(eWallet);
	}

	@Override
	public void updateCreatorWallet(UUID creatorId, float totalMoney) {
		EWallet eWallet = getWalletByUserId(creatorId);

		float newBalance = (float) (eWallet.getTotalAmount() + totalMoney * 0.963);
		eWallet.setTotalAmount(newBalance);

		eWalletRepository.save(eWallet);
	}

	@Override
	public void updateAudienceWallet(UUID audienceId, float totalMoney) {
		EWallet eWallet = getWalletByUserId(audienceId);
		float newBalance = (float) (eWallet.getTotalAmount() + totalMoney);
		eWallet.setTotalAmount(newBalance);
		eWalletRepository.save(eWallet);
	}

}
