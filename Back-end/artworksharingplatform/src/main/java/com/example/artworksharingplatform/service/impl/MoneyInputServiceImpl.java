package com.example.artworksharingplatform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.MoneyInput;
import com.example.artworksharingplatform.repository.InputMoneyRepository;
import com.example.artworksharingplatform.service.MoneyInputService;

@Service
public class MoneyInputServiceImpl implements MoneyInputService{

	@Autowired
	InputMoneyRepository _repo;
	@Override
	public MoneyInput addMoneyInput(MoneyInput input) {
		return _repo.save(input);
	}
	
}
