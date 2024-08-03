package com.example.artworksharingplatform.model;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class PreOrderRequest {
    private UUID creatorId;
    private String requirement;
}
