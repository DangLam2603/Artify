package com.example.artworksharingplatform.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.artworksharingplatform.entity.PreOrder;
import com.example.artworksharingplatform.entity.User;

public interface PreOrderRepository extends JpaRepository<PreOrder, UUID> {
    List<PreOrder> findByPreOrderCreator(User preOrderCreator);
    List<PreOrder> findByPreOrderAudience(User preOrderCustomer);

    List<PreOrder> findByPreOrderAudienceAndStatus(User preOrderCustomer,String status);
    List<PreOrder> findByPreOrderCreatorAndStatus(User preOrderCreator,String status);

    int countByPreOrderCreator(User preOrderCreator);

    Optional<PreOrder> findById(UUID id);
}