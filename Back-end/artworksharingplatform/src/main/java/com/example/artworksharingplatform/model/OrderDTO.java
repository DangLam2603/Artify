package com.example.artworksharingplatform.model;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Data;

@Data
public class OrderDTO {
    private UUID orderId;
    private Timestamp orderDate;
    private ArtworkDTO artwork;
    private float totalPrice;
    private UUID creatorId;
    private String creatorName;
    private UUID audienceId;
}
