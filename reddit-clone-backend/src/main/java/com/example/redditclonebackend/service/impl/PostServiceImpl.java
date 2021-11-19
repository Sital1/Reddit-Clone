package com.example.redditclonebackend.service.impl;

import com.example.redditclonebackend.dto.PostRequestDto;
import com.example.redditclonebackend.dto.PostResponseDto;
import com.example.redditclonebackend.exceptions.PostNotFoundException;
import com.example.redditclonebackend.exceptions.SubRedditNotFoundException;
import com.example.redditclonebackend.model.Post;
import com.example.redditclonebackend.model.Subreddit;
import com.example.redditclonebackend.model.User;
import com.example.redditclonebackend.repository.PostRepository;
import com.example.redditclonebackend.repository.SubredditRepository;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.service.AuthService;
import com.example.redditclonebackend.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostServiceImpl implements PostService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    private ModelMapper modelMapper;
    private final UserRepository userRepository;


    /**
     * Saves the post by initializing the time, subreddit name and the current user.
     * @param postRequestDto The post dto object
     */
    @Override
    public void save(PostRequestDto postRequestDto) {

        Subreddit subreddit = subredditRepository.findByName(postRequestDto.getSubreddit())
                .orElseThrow(() -> new SubRedditNotFoundException("Subreddit not found" + postRequestDto.getSubreddit()));

        Post post = modelMapper.map(postRequestDto, Post.class);
        post.setSubreddit(subreddit);
        post.setCreatedDate(Instant.now());
        post.setUser(authService.getCurrentUser());

        postRepository.save(post);

    }

    /**
     * Gets a post by it's id.
     * @param id The id of the post
     * @return The PostResponse dto.
     */
    @Override
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with" + id.toString() + "not found"));

        return modelMapper.map(post, PostResponseDto.class);

    }

    /**
     * Returns all the posts.
     * @return The list of PostResponse dto.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
       return postRepository.findAll()
                .stream()
                .map(post -> modelMapper.map(post, PostResponseDto.class))
                .collect(toList());
    }

    /**
     * Returns the list of posts belonging to a subreddit
     * @param subredditId The id of the subreddit
     * @return The list of PostResponse dto.
     */
    @Override
    public List<PostResponseDto> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubRedditNotFoundException("Subreddit with id - " + subredditId.toString() + "not found"));
      return  postRepository.findAllBySubreddit(subreddit)
              .stream()
              .map(post -> modelMapper.map(post, PostResponseDto.class))
              .collect(toList());
    }

    /**
     * Returns the list of posts of a certain user.
     * @param name The name of the user.
     * @return The List of PostResponseDto.
     */
    @Override
    public List<PostResponseDto> getPostsByUsername(String name) {

        User user = userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + name + " not found"));
        return postRepository.findAllByUser(user)
                .stream()
                .map(post -> modelMapper.map(post, PostResponseDto.class))
                .collect(toList());
    }
}