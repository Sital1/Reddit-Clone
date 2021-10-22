package com.example.redditclonebackend.repository;

import com.example.redditclonebackend.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit,Long> {
}
