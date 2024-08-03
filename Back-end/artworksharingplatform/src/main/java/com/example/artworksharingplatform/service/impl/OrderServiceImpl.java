package com.example.artworksharingplatform.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.entity.Order;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.repository.ArtworkRepository;
import com.example.artworksharingplatform.repository.OrderRepository;
import com.example.artworksharingplatform.repository.TransactionRepository;
import com.example.artworksharingplatform.repository.UserRepository;
import com.example.artworksharingplatform.service.EWalletService;
import com.example.artworksharingplatform.service.OrderService;
import com.example.artworksharingplatform.service.TransactionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ArtworkRepository artworkRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    EWalletService eWalletService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    OrderRepository orderRepository;

    @Transactional
    @SuppressWarnings("null")
    @Override
    public Order addOrder(Order order) throws Exception {
        try {
            Artworks artwork = artworkRepository.findById(order.getArtwork().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Artwork not found"));

            if (artwork.getType().toLowerCase().equals("premium")
                    && artwork.getStatus().toLowerCase().equals("active")) {

                User user = userRepository.findById(order.getAudience().getId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));

                float totalMoney = order.getTotalPrice();

                if (eWalletService.isEnoughMoney(user.getId(), totalMoney)) {
                    // set transaction + admin, creator, - audience
                    order.setTransactions(transactionService.addTransactionOrderAudience(order, -totalMoney));
                    transactionService.addTransactionOrderAdmin(order, totalMoney);
                    transactionService.addTransactionOrderCreator(order, totalMoney);

                    // update admin, creator, audience wallet
                    eWalletService.updateAudienceWallet(user.getId(), -totalMoney);
                    eWalletService.updateCreatorWallet(order.getArtwork().getPosts().getCreator().getId(), totalMoney);
                    eWalletService.updateAdminWallet(totalMoney);

                    return orderRepository.save(order);
                } else {
                    throw new Exception("Not enough money in EWallet.");
                }

            } else {
                throw new Exception("Artwork is not for sale.");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public long countOrders() {
        return orderRepository.count();
    }

    @Override
    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepository.findByAudience_Id(userId);
    }

}
