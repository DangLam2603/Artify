package com.example.artworksharingplatform.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.entity.PreOrder;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.model.PreOrderDTO;
import com.example.artworksharingplatform.model.ProcessingRequest;
import com.example.artworksharingplatform.repository.ArtworkRepository;
import com.example.artworksharingplatform.repository.PreOrderRepository;
import com.example.artworksharingplatform.repository.TransactionRepository;
import com.example.artworksharingplatform.repository.UserRepository;
import com.example.artworksharingplatform.service.EWalletService;
import com.example.artworksharingplatform.service.PreOrderService;
import com.example.artworksharingplatform.service.TransactionService;
import com.example.artworksharingplatform.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PreOrderServiceImpl implements PreOrderService {
    @Autowired
    PreOrderRepository _preOrderRepo;
    @Autowired
    UserService _userService;
    @Autowired
    TransactionRepository _transactionRepository;
    @Autowired
    TransactionService _transactionService;
    @Autowired
    EWalletService _eWalletService;
    @Autowired
    UserRepository _userRepository;
    @Autowired
    ArtworkRepository _artworkRepository;

    @Override
    public void addPreOrder(PreOrder preOrderRequest) throws Exception {
        try {
            _preOrderRepo.save(preOrderRequest);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<PreOrder> getCreatorPreOrderList(User preOrderCreator) throws Exception {
        try {
            List<PreOrder> preOrderList = _preOrderRepo.findByPreOrderCreator(preOrderCreator);
            return preOrderList;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public int countByPreOrderCreator(User preOrderCreator) {
        return _preOrderRepo.countByPreOrderCreator(preOrderCreator);
    }

    @Override
    public PreOrder updatePreOrderCreator(PreOrderDTO updatedPreOrderDTO, Artworks updatedArtwork) throws Exception {
        try {
            PreOrder preOrderToChange = _preOrderRepo.findById(updatedPreOrderDTO.getPreOrderId())
                    .orElseThrow(() -> new EntityNotFoundException("PreOrder not found"));
            preOrderToChange.setCreatorNote(updatedPreOrderDTO.getCreatorNote());
            preOrderToChange.setPrice(updatedPreOrderDTO.getPrice());
            preOrderToChange.setStatus(updatedPreOrderDTO.getStatus().toUpperCase());
            if (updatedArtwork != null) {
                preOrderToChange.setPreOrderArtwork(updatedArtwork);
            }
            _preOrderRepo.save(preOrderToChange);
            return preOrderToChange;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    @Override
    public PreOrder processingPreOrderAudience(ProcessingRequest request) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User userLogg = _userService.findByEmail(email);
        try {

            PreOrder preOrder = _preOrderRepo.findById(request.getPreOrderID())
                    .orElseThrow(() -> new EntityNotFoundException("PreOrder not found"));

            User user = _userRepository.findById(preOrder.getPreOrderAudience().getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            if (!user.equals(userLogg)) {
                throw new Exception("User can not be different");
            }
            if (request.getStatus() == "DENIEDPROCESSING") {
                preOrder.setStatus(request.getStatus());
                _preOrderRepo.save(preOrder);
            } else {
                // validation Price
                if (request.getPrice() < 0) {
                    throw new Exception("Money can not be negative");
                }
                if (request.getPrice() < preOrder.getPrice()) {
                    throw new Exception("Not enough money to buy this art");
                }
                // set status to Processing
                preOrder.setStatus("PROCESSING");
                _preOrderRepo.save(preOrder);
                // transaction
                preOrder.setTransactions(
                        _transactionService.addTransactionPreOrderAudience(preOrder, -request.getPrice()));
                var adminTransaction = _transactionService.addTransactionPreOrderAdmin(preOrder, request.getPrice());
                _eWalletService.updateAudienceWallet(user.getId(), -request.getPrice());
                _eWalletService.processingAdminWallet(adminTransaction.getTotalMoney());
            }

            return preOrder;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<PreOrder> getWaitingPreOrderList(User preOrderCustomer) throws Exception {
        try {
            List<PreOrder> preOrderList = _preOrderRepo.findByPreOrderAudience(preOrderCustomer);
            return preOrderList;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public PreOrder completePreOrderAudience(PreOrderDTO preOrderDTO) throws Exception {
        try {
            PreOrder preOrder = _preOrderRepo.findById(preOrderDTO.getPreOrderId())
                    .orElseThrow(() -> new EntityNotFoundException("PreOrder not found"));
            preOrder.setAudienceFeedback(preOrderDTO.getAudienceFeedback());
            preOrder.setAudienceRating(preOrderDTO.getAudienceRating());
            preOrder.setStatus("COMPLETED");
            _preOrderRepo.save(preOrder);
            // Add Transaction
            var adminTransaction = _transactionService.addTransactionPreOrderAdmin(preOrder,
                    (preOrder.getPrice() / 0.037f));
            var creatorTransaction = _transactionService.addTransactionPreOrderCreator(preOrder,
                    preOrder.getPrice());
            // Update E-wallet
            _eWalletService.updateCreatorWallet(preOrder.getPreOrderCreator().getId(),
                    creatorTransaction.getTotalMoney());
            _eWalletService.updateAdminWallet(adminTransaction.getTotalMoney());
            return preOrder;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public PreOrder feedbackPreOrder(UUID preOrderId, String feedback) throws Exception {
        try {
            PreOrder preOrder = _preOrderRepo.findById(preOrderId)
                    .orElseThrow(() -> new EntityNotFoundException("PreOrder not found"));

            preOrder.setAudienceFeedback(feedback);
            return _preOrderRepo.save(preOrder);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public PreOrder deleteFeedback(UUID preOrderId) throws Exception {
        try {
            PreOrder preOrder = _preOrderRepo.findById(preOrderId)
                    .orElseThrow(() -> new EntityNotFoundException("PreOrder not found"));

            preOrder.setAudienceFeedback(null);
            return _preOrderRepo.save(preOrder);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public PreOrder canclePreOrderAudience(UUID preOrderId) throws Exception {
        try {
            PreOrder preOrder = _preOrderRepo.findById(preOrderId)
                    .orElseThrow(() -> new EntityNotFoundException("PreOrder not found"));
            preOrder.setStatus("CANCEL");
            _preOrderRepo.save(preOrder);
            return preOrder;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public PreOrder ratePreOrder(UUID preOrderId, int rate) throws Exception {
        try {
            PreOrder preOrder = _preOrderRepo.findById(preOrderId)
                    .orElseThrow(() -> new EntityNotFoundException("PreOrder not found"));

            if (rate > 5)
                rate = 5;
            else if (rate < 0)
                rate = 0;

            preOrder.setAudienceRating(rate);
            return _preOrderRepo.save(preOrder);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public PreOrder deleteRating(UUID preOrderId) throws Exception {
        try {
            PreOrder preOrder = _preOrderRepo.findById(preOrderId)
                    .orElseThrow(() -> new EntityNotFoundException("PreOrder not found"));

            preOrder.setAudienceRating(0);
            return _preOrderRepo.save(preOrder);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<PreOrder> getAcceptedPreOrderList(User preOrderCustomer, String status) throws Exception {
        try {
            List<PreOrder> preOrders = _preOrderRepo.findByPreOrderAudienceAndStatus(preOrderCustomer, status);
            return preOrders;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<PreOrder> getProcessingPreOrderList(User preOrderCreator, String status) throws Exception {
        try {
            List<PreOrder> preOrders = _preOrderRepo.findByPreOrderCreatorAndStatus(preOrderCreator, status);
            return preOrders;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
