package com.example.redditclonebackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubredditDto {

    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;


}
