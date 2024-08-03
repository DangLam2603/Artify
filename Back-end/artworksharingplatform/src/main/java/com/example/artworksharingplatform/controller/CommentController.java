package com.example.artworksharingplatform.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.artworksharingplatform.entity.Comment;
import com.example.artworksharingplatform.mapper.CommentMapper;
import com.example.artworksharingplatform.model.ApiResponse;
import com.example.artworksharingplatform.model.CommentDTO;
import com.example.artworksharingplatform.model.UserDTO;
import com.example.artworksharingplatform.service.CommentService;
import com.example.artworksharingplatform.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/auth/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    CommentMapper commentMapper;

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_AUDIENCE') or hasRole('ROLE_CREATOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteComment(@RequestParam UUID commentId) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();

        try {
            commentService.deleteComment(commentId);

            apiResponse.ok("Delete comment successfully.");
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_AUDIENCE') or hasRole('ROLE_CREATOR')")
    public ResponseEntity<ApiResponse<CommentDTO>> addComment(@RequestBody CommentDTO commentDTO,
            @RequestParam UUID postId) {
        ApiResponse<CommentDTO> apiResponse = new ApiResponse<CommentDTO>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        UserDTO user = userService.findByEmailAddress(email);

        try {
            Comment comment = new Comment();
            comment.setComment(commentDTO.getComment());

            Comment c = commentService.addComment(comment, postId, user.getUserId());
            CommentDTO cDto = commentMapper.toCommentDTO(c);

            apiResponse.ok(cDto);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("edit")
    @PreAuthorize("hasRole('ROLE_AUDIENCE') or hasRole('ROLE_CREATOR')")
    public ResponseEntity<ApiResponse<CommentDTO>> editComment(@RequestParam UUID commentId,
            @RequestBody CommentDTO commentDTO) {
        ApiResponse<CommentDTO> apiResponse = new ApiResponse<CommentDTO>();

        try {
            Comment comment = commentService.getCommentById(commentId);
            comment.setComment(commentDTO.getComment());

            Comment c = commentService.ediComment(comment);
            CommentDTO cDto = commentMapper.toCommentDTO(c);

            apiResponse.ok(cDto);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("view")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getCommentsListByPostId(@RequestParam("postId") UUID postId) {
        ApiResponse<List<CommentDTO>> apiResponse = new ApiResponse<List<CommentDTO>>();

        try {
            List<Comment> cList = commentService.getCommentsListByPostId(postId);
            List<CommentDTO> cDtosList = commentMapper.toCommentDTOList(cList);

            apiResponse.ok(cDtosList);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            apiResponse.error(e);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
