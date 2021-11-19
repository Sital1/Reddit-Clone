package com.example.redditclonebackend.repository;

import com.example.redditclonebackend.model.Post;
import com.example.redditclonebackend.model.User;
import com.example.redditclonebackend.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User user);
}
