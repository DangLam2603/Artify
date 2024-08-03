package com.example.artworksharingplatform.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "comments")
public class Comment {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "Comment", columnDefinition = "nvarchar(max)")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "InteractionID")
    private Interaction interaction;
}
