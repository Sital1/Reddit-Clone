package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.dto.PostRequestDto;
import com.example.redditclonebackend.dto.PostResponseDto;
import com.example.redditclonebackend.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void>createPost(@RequestBody PostRequestDto postRequestDto){
        postService.save(postRequestDto);

        return new ResponseEntity(CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id){

        return status(OK).body( postService.getPost(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<PostResponseDto>> getAllPosts(){
        return status(OK).body(postService.getAllPosts());
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponseDto>> getPostsBySubreddit(@PathVariable Long id){
        return status(OK).body(postService.getPostsBySubreddit(id));
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponseDto>> getPostsBySubreddit(@PathVariable String name){
        return status(OK).body(postService.getPostsByUsername(name));
    }




}
