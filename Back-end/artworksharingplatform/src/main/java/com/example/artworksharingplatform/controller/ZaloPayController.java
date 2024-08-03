package com.example.artworksharingplatform.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.artworksharingplatform.model.ZaloCallBackDTO;
import com.example.artworksharingplatform.model.ZaloOrderRequestDTO;
import com.example.artworksharingplatform.service.ZaloPayTest;

@RestController
@RequestMapping("api/auth")
public class ZaloPayController {
	
	@Autowired
	ZaloPayTest zaloPayTest;

	 @PostMapping("v1/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody ZaloOrderRequestDTO request) throws JSONException, IOException {
        Map<String, Object> resultOrder = zaloPayTest.createOrder(request);
        return new ResponseEntity<>(resultOrder, HttpStatus.OK);
    }

	 @PostMapping("v1/callback")
    public ResponseEntity<String> callback(@RequestBody ZaloCallBackDTO paymentDTO)
            throws JSONException, NoSuchAlgorithmException, InvalidKeyException {

        JSONObject result = new JSONObject();
        return new ResponseEntity<>(zaloPayTest
                .doCallBack(result, paymentDTO.getJsonString()).toString(), HttpStatus.OK);

    }
}
