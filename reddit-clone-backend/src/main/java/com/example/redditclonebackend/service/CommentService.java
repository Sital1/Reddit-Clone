package com.example.redditclonebackend.service;

import com.example.redditclonebackend.dto.CommentsDto;

import java.util.List;

public interface CommentService {

    void save(CommentsDto commentsDto);

    List<CommentsDto> getCommentsForPost(Long id);

    List<CommentsDto> getCommentsForUser(String username);
}
