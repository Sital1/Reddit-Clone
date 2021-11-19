package com.example.redditclonebackend.service.impl;

import com.example.redditclonebackend.dto.VoteDto;
import com.example.redditclonebackend.exceptions.PostNotFoundException;
import com.example.redditclonebackend.exceptions.SpringRedditException;
import com.example.redditclonebackend.model.Post;
import com.example.redditclonebackend.model.Vote;
import com.example.redditclonebackend.model.VoteType;
import com.example.redditclonebackend.repository.PostRepository;
import com.example.redditclonebackend.repository.VoteRepository;
import com.example.redditclonebackend.service.AuthService;
import com.example.redditclonebackend.service.VoteService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
@AllArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    /**
     * Updates the vote count of a specific post. Only lets user upvote or downvote once consecutively. Saves the recorded vote in the post.
     * @param voteDto The voteDto object carrying vote info.
     */
    @Override
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not found with id: " + voteDto.getPostId()));

        // get the top Vote by user on a post
        Optional<Vote> optionalVoteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        // validate and test if the user can upvote or downvote once consecutively.
        if(optionalVoteByPostAndUser.isPresent() &&
                optionalVoteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
            throw new SpringRedditException("You have already " + voteDto.getVoteType() +"d for this post");
        }

        // increase or decrease according to the vote type
        if(VoteType.UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount()+1);
        }else{
            post.setVoteCount(post.getVoteCount()-1);
        }

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    /**
     * Creates a Vote object with the voteDto and post using the builder pattern.
     * @param voteDto The VoteDto object containing the vote information.
     * @param post The post object whose vote is changed
     * @return The Vote object.
     */
    private Vote mapToVote(VoteDto voteDto, Post post){
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }

}
