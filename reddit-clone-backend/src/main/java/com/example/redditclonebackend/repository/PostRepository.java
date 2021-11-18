package com.example.redditclonebackend.repository;

import com.example.redditclonebackend.model.Post;
import com.example.redditclonebackend.model.Subreddit;
import com.example.redditclonebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findAllByUser(User user);
}
