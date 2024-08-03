package com.example.artworksharingplatform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZaloOrderRequestDTO {
	private String appUser;
    private Long amount;
    private Long orderId;
}
