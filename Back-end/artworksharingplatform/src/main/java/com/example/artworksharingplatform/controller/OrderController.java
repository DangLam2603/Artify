package com.example.artworksharingplatform.controller;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.entity.Order;
import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.mapper.OrderMapper;
import com.example.artworksharingplatform.model.ApiResponse;
import com.example.artworksharingplatform.model.OrderDTO;
import com.example.artworksharingplatform.service.ArtworkService;
import com.example.artworksharingplatform.service.EWalletService;
import com.example.artworksharingplatform.service.OrderService;
import com.example.artworksharingplatform.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/auth/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    EWalletService eWalletService;

    @Autowired
    UserService userService;

    @Autowired
    ArtworkService artworkService;

    @Autowired
    OrderMapper orderMapper;

    @PostMapping("add")
    @PreAuthorize("hasRole('ROLE_AUDIENCE') or hasRole('ROLE_CREATOR')")
    public ResponseEntity<ApiResponse<OrderDTO>> downloadArtwork(@RequestBody OrderDTO orderDTO) {
        ApiResponse<OrderDTO> apiResponse = new ApiResponse<OrderDTO>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);
        
        try {
            Artworks artworks = artworkService.geArtworksById(orderDTO.getArtwork().getArtId());
            Order order = new Order();
            order.setArtwork(artworks);
            order.setAudience(user);
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setTotalPrice(orderDTO.getTotalPrice());
            Order o = orderService.addOrder(order);
            OrderDTO oDto = orderMapper.toOrderDTO(o);

            apiResponse.ok(oDto);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("viewList")
    @PreAuthorize("hasRole('ROLE_AUDIENCE') or hasRole('ROLE_CREATOR')")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> viewOrdersList() {
        ApiResponse<List<OrderDTO>> apiResponse = new ApiResponse<List<OrderDTO>>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);

        try {
            List<Order> orders = orderService.getOrdersByUserId(user.getId());
            List<OrderDTO> orderDTOs = orderMapper.toOrderDTOsList(orders);

            apiResponse.ok(orderDTOs);
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
