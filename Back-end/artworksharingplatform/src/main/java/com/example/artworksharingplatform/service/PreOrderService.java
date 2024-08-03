package com.example.artworksharingplatform.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.entity.PreOrder;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.model.PreOrderDTO;
import com.example.artworksharingplatform.model.ProcessingRequest;

@Service
public interface PreOrderService {
    void addPreOrder(PreOrder preOrderRequest) throws Exception;

    List<PreOrder> getCreatorPreOrderList(User preOrderCreator) throws Exception;

    List<PreOrder> getWaitingPreOrderList(User preOrderCustomer) throws Exception;

    List<PreOrder> getAcceptedPreOrderList(User preOrderCustomer, String status) throws Exception;

    List<PreOrder> getProcessingPreOrderList(User preOrderCreator, String status) throws Exception;

    int countByPreOrderCreator(User preOrderCreator);

    PreOrder updatePreOrderCreator(PreOrderDTO updatedPreOrderDTO, Artworks updatedArtwork) throws Exception;

    PreOrder processingPreOrderAudience(ProcessingRequest request) throws Exception;

    PreOrder completePreOrderAudience(PreOrderDTO preOrderDTO) throws Exception;

    PreOrder feedbackPreOrder(UUID preOrderId, String feedback) throws Exception;

    PreOrder deleteFeedback(UUID preOrderId) throws Exception;

    PreOrder ratePreOrder(UUID preOrderId, int rate) throws Exception;

    PreOrder deleteRating(UUID preOrderId) throws Exception;

    PreOrder canclePreOrderAudience(UUID preOrderId) throws Exception;
}
