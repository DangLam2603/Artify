package com.example.artworksharingplatform.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.artworksharingplatform.config.VnPayConfig;
import com.example.artworksharingplatform.entity.EWallet;
import com.example.artworksharingplatform.entity.MoneyInput;
import com.example.artworksharingplatform.entity.Transaction;
import com.example.artworksharingplatform.mapper.TransactionMapper;
import com.example.artworksharingplatform.model.ApiResponse;
import com.example.artworksharingplatform.model.TransactionDTO;
import com.example.artworksharingplatform.model.UserDTO;
import com.example.artworksharingplatform.service.impl.EWalletServiceImpl;
import com.example.artworksharingplatform.service.impl.UserServiceImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.artworksharingplatform.service.MoneyInputService;
import com.example.artworksharingplatform.service.TransactionService;
import com.example.artworksharingplatform.service.UserService;

@RestController
@RequestMapping("api/auth")

public class EWalletController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserService userService;

    @Autowired
    EWalletServiceImpl walletServiceImpl;

    @Autowired
    TransactionService transactionService;

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    MoneyInputService inputService;

    @PostMapping("/pay")
    public String getPay(@RequestParam("input_money") String inputMoney) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long money_amount = Integer.parseInt(inputMoney);
        long amount = money_amount * 100;
        String bankCode = "NCB";

        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }

    @GetMapping("/addEwallet")
    public ResponseEntity<ApiResponse> addMoneyToEwallet(@RequestParam("vnp_Amount") String amount,
            @RequestParam("vnp_ResponseCode") String responseCode) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = new ApiResponse();
        if (isUserAuthenticated(authentication)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername(); // getUserName này là email
            UserDTO userInfo = userService.findByEmailAddress(email);
            EWallet wallet = walletServiceImpl.getWalletByUserId(userInfo.getUserId());

            float newAmount = wallet.getTotalAmount() + (Float.parseFloat(amount) / 100);
            wallet.setTotalAmount(newAmount);
            walletServiceImpl.updateWallet(wallet);
            Timestamp date = new Timestamp(System.currentTimeMillis());
            // Date date = new Date();
            Transaction transaction = new Transaction();
            transaction.setUser(userService.getUser(userInfo.getUserId()));
            transaction.setTransactionDate(date);
            transaction.setTotalMoney(Float.parseFloat(amount) / 100);
            Transaction addedTrans = transactionService.addTransaction(transaction);
            MoneyInput input = new MoneyInput();
            input.setMoney(Float.parseFloat(amount));
            input.setTransactions(addedTrans);
            MoneyInput addedMoneyInput = inputService.addMoneyInput(input);
            TransactionDTO result = transactionMapper.toTransactionDTO(addedTrans);
            apiResponse.ok(result);
            return ResponseEntity.ok(apiResponse);
        } else {
            return ResponseEntity.ok(apiResponse);
        }
    }

    private boolean isUserAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails;
    }

    @GetMapping("/viewEwallet")
    // @Pre
    public ResponseEntity<ApiResponse> getWalletByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = new ApiResponse();
        if (isUserAuthenticated(authentication)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername(); // getUserName này là email
            UserDTO userInfo = userService.findByEmailAddress(email);
            EWallet wallet = walletServiceImpl.getWalletByUserId(userInfo.getUserId());
            if (wallet != null) {
                apiResponse.ok(wallet);
                return ResponseEntity.ok(apiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    @GetMapping("/viewTransactions")
    public ResponseEntity<ApiResponse> getTransactionsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = new ApiResponse();
        if (isUserAuthenticated(authentication)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername(); // getUserName này là email
            UserDTO userInfo = userService.findByEmailAddress(email);
            List<Transaction> transactions = transactionService.getTransactionsByUserId(userInfo.getUserId());
            transactionMapper.toList(transactions);
            if (transactions != null) {
                apiResponse.ok(transactionMapper.toList(transactions));
                return ResponseEntity.ok(apiResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    @GetMapping("/getTransaction")
    public ResponseEntity<ApiResponse> getTransactionById(@RequestParam("id") String transId) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            UUID id = UUID.fromString(transId);
            TransactionDTO result = transactionService.getTransactionById(id);
            apiResponse.ok(result);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

}
