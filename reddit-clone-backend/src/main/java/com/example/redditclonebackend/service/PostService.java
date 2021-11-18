package com.example.redditclonebackend.service;

import com.example.redditclonebackend.dto.PostRequestDto;
import com.example.redditclonebackend.dto.PostResponseDto;

import java.util.List;

public interface PostService {

    void save(PostRequestDto postRequestDto);
    PostResponseDto getPost(Long id);
    List<PostResponseDto> getAllPosts();
    List<PostResponseDto> getPostsBySubreddit(Long id);
    List<PostResponseDto> getPostsByUsername(String name);

}
