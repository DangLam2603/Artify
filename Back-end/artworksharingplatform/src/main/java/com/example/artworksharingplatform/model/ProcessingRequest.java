package com.example.artworksharingplatform.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProcessingRequest {
    private UUID preOrderID;
    private float price;
    String status;
}
