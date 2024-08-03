package com.example.artworksharingplatform.service;

import java.util.List;
import java.util.UUID;

import com.example.artworksharingplatform.entity.Comment;

public interface CommentService {
    Comment getCommentById(UUID commentId);

    Comment addComment(Comment comment, UUID postId, UUID userId);

    Comment ediComment(Comment comment);

    void deleteComment(UUID commentId);

    List<Comment> getCommentsListByPostId(UUID postId);
}
