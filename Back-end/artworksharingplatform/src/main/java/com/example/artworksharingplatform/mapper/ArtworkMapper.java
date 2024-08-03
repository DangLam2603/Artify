package com.example.artworksharingplatform.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.artworksharingplatform.entity.Artworks;
import com.example.artworksharingplatform.model.ArtworkDTO;

@Mapper(componentModel = "spring")
public interface ArtworkMapper {

    @Mapping(source = "artwork.id", target = "artId")
    @Mapping(source = "artwork.artName", target = "artName")
    @Mapping(source = "artwork.posts.id", target = "postId")
    @Mapping(expression = "java(calculateOrderCount(artwork))", target = "orderCount")
    // @Mapping(source)
    ArtworkDTO toArtworkDTO(Artworks artwork);

    List<ArtworkDTO> toArtworkDTOs(List<Artworks> artworks);

    default int calculateOrderCount(Artworks artwork) {
        if (artwork.getOrders() != null) {
            return artwork.getOrders().size();
        }
        return 0;
    }
}
