package com.example.artworksharingplatform.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.artworksharingplatform.entity.PreOrder;
import com.example.artworksharingplatform.model.PreOrderDTO;

@Mapper(componentModel = "spring")
public interface PreOrderMapper {
	@Mapping(source = "preOrder.id", target = "preOrderId")
	@Mapping(source = "preOrder.status", target = "status")
	@Mapping(source = "preOrder.price", target = "price")
	@Mapping(source = "preOrder.requirement", target = "requirement")
	@Mapping(source = "preOrder.audienceRating", target = "audienceRating")
	@Mapping(source = "preOrder.audienceFeedback", target = "audienceFeedback")
	@Mapping(source = "preOrder.creatorNote", target = "creatorNote")
	@Mapping(source = "preOrder.preOrderDate", target = "preOrderDate")
	@Mapping(source = "preOrder.preOrderCreator.id", target = "creatorId")
	@Mapping(source = "preOrder.preOrderCreator.name", target = "creatorName")
	@Mapping(source = "preOrder.preOrderAudience.id", target = "audienceId")
	@Mapping(source = "preOrder.preOrderArtwork.id", target = "artworkId")
	@Mapping(source = "preOrder.preOrderArtwork.imagePath", target = "artworkImagePath")
	PreOrderDTO toPreOrderDTO(PreOrder preOrder);

	List<PreOrderDTO> toList(List<PreOrder> preOrderList);
}
