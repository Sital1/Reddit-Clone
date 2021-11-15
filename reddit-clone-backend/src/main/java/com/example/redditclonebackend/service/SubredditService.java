package com.example.redditclonebackend.service;

import com.example.redditclonebackend.dto.SubredditDto;

import java.util.List;

public interface SubredditService {

    SubredditDto save(SubredditDto subredditDto);

    List<SubredditDto> getAll();

    SubredditDto getSubreddit(Long id);
}
