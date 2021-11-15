package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.dto.SubredditDto;
import com.example.redditclonebackend.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subredditService.save(subredditDto));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubReddits(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subredditService.getAll());
    }


}
