package com.example.artworksharingplatform.controller;

import com.example.artworksharingplatform.entity.Transaction;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.mapper.TransactionMapper;
import com.example.artworksharingplatform.model.ApiResponse;
import com.example.artworksharingplatform.model.TransactionDTO;
import com.example.artworksharingplatform.service.TransactionService;
import com.example.artworksharingplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("api/auth")
public class TransactionController {
    @Autowired
    TransactionService _transactionService;
    @Autowired
    UserService _userService;
    @Autowired
    TransactionMapper _mapper;

    @GetMapping("filter_By_Date")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> filterByDate(@RequestParam("date") Date date) {
        ApiResponse<List<TransactionDTO>> apiResponse = new ApiResponse<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String email = userDetails.getUsername();
                User user = _userService.findByEmail(email);
                var result = _transactionService.filterByDate(date, user);
                List<TransactionDTO> listSort = _mapper.toList(result);
                apiResponse.ok(listSort);
            }
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e.getMessage());
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/viewList")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> viewTransactionsList() {
        ApiResponse<List<TransactionDTO>> apiResponse = new ApiResponse<List<TransactionDTO>>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = _userService.findByEmail(email);

        try {
            List<Transaction> transactions = _transactionService.getTransactionsByUserId(user.getId());
            List<TransactionDTO> transactionDTOs = _mapper.toList(transactions);

            apiResponse.ok(transactionDTOs);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/viewAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> viewAllTransactions() {
        ApiResponse<List<TransactionDTO>> apiResponse = new ApiResponse<List<TransactionDTO>>();

        try {
            List<Transaction> transactions = _transactionService.getAllTransactions();
            List<TransactionDTO> transactionDTOs = _mapper.toList(transactions);

            apiResponse.ok(transactionDTOs);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
