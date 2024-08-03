package com.example.artworksharingplatform.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.artworksharingplatform.entity.Order;
import com.example.artworksharingplatform.model.OrderDTO;

@Mapper(componentModel = "spring", uses = ArtworkMapper.class)
public interface OrderMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.orderDate", target = "orderDate")
    @Mapping(source = "order.totalPrice", target = "totalPrice")
    @Mapping(source = "order.artwork", target = "artwork")
    @Mapping(source = "order.artwork.posts.creator.id", target = "creatorId")
    @Mapping(source = "order.artwork.posts.creator.name", target = "creatorName")
    @Mapping(source = "order.audience.id", target = "audienceId")
    OrderDTO toOrderDTO(Order order);

    List<OrderDTO> toOrderDTOsList(List<Order> ordersList);
}
