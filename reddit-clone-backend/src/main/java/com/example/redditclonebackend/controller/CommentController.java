package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.dto.CommentsDto;
import com.example.redditclonebackend.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments")
@Slf4j
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * Endpoint to Create a comment.
     * @return The response entity with status 201 created.
     */
    @PostMapping
    public ResponseEntity<Void> createComments(@RequestBody CommentsDto commentsDto){
        commentService.save(commentsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Gets all the comments of a specific post.
     * @param id The id of the post.
     * @return 200 OK status with the list of CommentsDto.
     */
    @GetMapping("by-post/{id}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long id){
      return ResponseEntity.status(OK).body(commentService.getCommentsForPost(id));
    }

    /**
     *  Gets all the comments of a specific user.
     * @param username The username of the user
     * @return 200 OK status with the list of CommentsDto.
     */
    @GetMapping("by-user/{username}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String username){
        return ResponseEntity.status(OK).body(commentService.getCommentsForUser(username));
    }

}
