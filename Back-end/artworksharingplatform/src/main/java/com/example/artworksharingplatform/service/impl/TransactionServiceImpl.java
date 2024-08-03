package com.example.artworksharingplatform.service.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Date;
import com.example.artworksharingplatform.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.mapper.TransactionMapper;
import com.example.artworksharingplatform.model.TransactionDTO;
import com.example.artworksharingplatform.repository.TransactionRepository;
import com.example.artworksharingplatform.repository.UserRepository;
import com.example.artworksharingplatform.service.TransactionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransactionServiceImpl implements TransactionService {

	float adminRate = 0.037f;
	float creatorRate = 0.963f;

	@Autowired
	TransactionRepository repo;

	@Autowired
	TransactionMapper transactionMapper;

	@Autowired
	UserRepository userRepository;

	@Override
	public List<Transaction> getTransactionsByUserId(UUID userId) {
		// TODO Auto-generated method stub
		return repo.findByUser_id(userId);
	}

	@Override
	public Transaction addTransaction(Transaction transaction) {
		return repo.save(transaction);
	}

	@Override
	public TransactionDTO getTransactionById(UUID id) {
		Transaction trans = repo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Transaction Not Found"));
		TransactionDTO result = transactionMapper.toTransactionDTO(trans);
		return result;
	}

	@Override
	public Transaction addTransactionOrderAdmin(Order order, float totalMoney) {
		Role role = Role.ADMIN;
		User admin = userRepository.findByRole(role);

		Transaction transaction = new Transaction();
		transaction.setTotalMoney(totalMoney * adminRate);
		transaction.setUser(admin);
		transaction.setTransactionDate(order.getOrderDate());
		return repo.save(transaction);
	}

	@Override
	public Transaction addTransactionOrderCreator(Order order, float totalMoney) {
		Transaction transaction = new Transaction();
		transaction.setTotalMoney(totalMoney * creatorRate);
		transaction.setUser(order.getArtwork().getPosts().getCreator());
		transaction.setTransactionDate(order.getOrderDate());
		return repo.save(transaction);
	}

	@Override
	public Transaction addTransactionOrderAudience(Order order, float totalMoney) {
		Transaction transaction = new Transaction();
		transaction.setTotalMoney(totalMoney);
		transaction.setUser(order.getAudience());
		transaction.setTransactionDate(order.getOrderDate());
		return repo.save(transaction);
	}

	@Override
	public Transaction addTransactionPreOrderAdmin(PreOrder order, float totalMoney) {
		Role role = Role.ADMIN;
		User admin = userRepository.findByRole(role);

		Transaction transaction = new Transaction();
		transaction.setTotalMoney(totalMoney);
		transaction.setUser(admin);
		transaction.setTransactionDate(Timestamp.valueOf(LocalDateTime.now()));
		return repo.save(transaction);
	}

	@Override
	public Transaction addTransactionPreOrderCreator(PreOrder order, float totalMoney) {
		Transaction transaction = new Transaction();
		transaction.setTotalMoney(totalMoney * creatorRate);
		transaction.setUser(order.getPreOrderCreator());
		transaction.setTransactionDate(order.getPreOrderDate());
		return repo.save(transaction);
	}

	@Override
	public Transaction addTransactionPreOrderAudience(PreOrder order, float totalMoney) {
		Transaction transaction = new Transaction();
		transaction.setTotalMoney(totalMoney);
		transaction.setUser(order.getPreOrderAudience());
		transaction.setTransactionDate(Timestamp.valueOf(LocalDateTime.now()));
		return repo.save(transaction);
	}

	@Override
	public List<Transaction> filterByDate(Date date, User user) throws Exception {
		try {
			// Retrieve all transactions from the repository
			List<Transaction> allTransactions = repo.findByUser_id(user.getId());
			LocalDate targetDate = date.toLocalDate();
			// Filter transactions by the given time
			List<Transaction> filteredTransactions = new ArrayList<>();
			for (Transaction transaction : allTransactions) {
				LocalDateTime transactionDateTime = transaction.getTransactionDate().toLocalDateTime();
				LocalDate transactionDate = transactionDateTime.toLocalDate();
				if (transactionDate.equals(targetDate)) {
					filteredTransactions.add(transaction);
				}
			}
			// Sort filtered transactions by Transaction_time in descending order
			Collections.sort(filteredTransactions, new Comparator<Transaction>() {
				@Override
				public int compare(Transaction t1, Transaction t2) {
					return t2.getTransactionDate().compareTo(t1.getTransactionDate());
				}
			});

			return filteredTransactions;

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public List<Transaction> getAllTransactions() {
		return repo.findAll();
	}
}
