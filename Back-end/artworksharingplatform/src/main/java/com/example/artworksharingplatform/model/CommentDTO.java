package com.example.artworksharingplatform.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private UUID commentId;
    private String comment;
    private UUID userId;
    private String userName;
}
