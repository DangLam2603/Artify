package com.example.artworksharingplatform.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.artworksharingplatform.entity.Interaction;
import com.example.artworksharingplatform.model.InteractionDTO;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface InteractionMapper {
    @Mapping(source = "interaction.interactionPost.id", target = "postId")
    @Mapping(source = "interaction.comments", target = "comments")
    @Mapping(source = "interaction.isLiked", target = "isLiked")
    @Mapping(source = "interaction.interactionAudience.id", target = "userId")
    @Mapping(source = "interaction.interactionAudience.name", target = "name")
    InteractionDTO toInteractionDTO(Interaction interaction);

    List<InteractionDTO> toInteractionDTOList(List<Interaction> interactionList);
}
