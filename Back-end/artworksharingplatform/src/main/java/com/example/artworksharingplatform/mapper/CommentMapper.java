package com.example.artworksharingplatform.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.artworksharingplatform.entity.Comment;
import com.example.artworksharingplatform.model.CommentDTO;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "c.id", target = "commentId")
    @Mapping(source = "c.comment", target = "comment")
    @Mapping(source = "c.interaction.interactionAudience.id", target = "userId")
    @Mapping(source = "c.interaction.interactionAudience.name", target = "userName")
    CommentDTO toCommentDTO(Comment c);

    List<CommentDTO> toCommentDTOList(List<Comment> commentsList);
}
