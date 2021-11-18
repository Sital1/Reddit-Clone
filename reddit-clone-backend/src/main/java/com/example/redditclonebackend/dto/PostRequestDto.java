package com.example.redditclonebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    private Long postId;
    private String subreddit;
    private String postName;
    private String url;
    private String description;
}
