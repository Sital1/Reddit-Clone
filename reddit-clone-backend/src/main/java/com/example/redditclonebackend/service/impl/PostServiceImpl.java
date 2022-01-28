package com.example.redditclonebackend.service.impl;

import com.example.redditclonebackend.dto.PostRequestDto;
import com.example.redditclonebackend.dto.PostResponseDto;
import com.example.redditclonebackend.exceptions.PostNotFoundException;
import com.example.redditclonebackend.exceptions.SubRedditNotFoundException;
import com.example.redditclonebackend.model.*;
import com.example.redditclonebackend.repository.PostRepository;
import com.example.redditclonebackend.repository.SubredditRepository;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.repository.VoteRepository;
import com.example.redditclonebackend.service.AuthService;
import com.example.redditclonebackend.service.PostService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.example.redditclonebackend.model.VoteType.DOWNVOTE;
import static com.example.redditclonebackend.model.VoteType.UPVOTE;
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
    private final VoteRepository voteRepository;


    /**
     * Saves the post by initializing the time, subreddit name and the current user.
     * @param postRequestDto The post dto object
     */
    @Override
    public void save(PostRequestDto postRequestDto) {

        Subreddit subreddit = subredditRepository.findByName(postRequestDto.getSubreddit())
                .orElseThrow(() -> new SubRedditNotFoundException("Subreddit not found" + postRequestDto.getSubreddit()));

        List<Post> posts = subreddit.getPosts();


        Post post = modelMapper.map(postRequestDto, Post.class);
        post.setSubreddit(subreddit);
        posts.add(post);
        subreddit.setPosts(posts);
        post.setCreatedDate(Instant.now());
        post.setUser(authService.getCurrentUser());
        post.setCommentCount(0);
        post.setVoteCount(0);

       // subredditRepository.save(subreddit);
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

        return mapToPostResponseDto(post);

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
                .map(post -> mapToPostResponseDto(post))
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
              .map(post -> mapToPostResponseDto(post))
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
                .map(post -> mapToPostResponseDto(post))
                .collect(toList());
    }

    private PostResponseDto mapToPostResponseDto(Post post){
        return PostResponseDto.builder()
                .postId(post.getPostId())
                .subreddit(post.getSubreddit().getName())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .username(post.getUser().getUsername())
                .voteCount(post.getVoteCount())
                .commentCount(post.getCommentCount())
                .duration(TimeAgo.using(post.getCreatedDate().toEpochMilli()))
                .upVote(isPostUpVoted(post))
                .downVote(isPostDownVoted(post))
                .build();

    }

    private boolean isPostUpVoted(Post post){
        return checkVoteType(post, UPVOTE);
    }

    private boolean isPostDownVoted(Post post){
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType){
       if(authService.isLoggedIn()){
           Optional<Vote> voteForPostByUser = voteRepository.
                   findTopByPostAndUserOrderByVoteIdDesc(post,authService.getCurrentUser());

           return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
       }
       return false;
    }
}
