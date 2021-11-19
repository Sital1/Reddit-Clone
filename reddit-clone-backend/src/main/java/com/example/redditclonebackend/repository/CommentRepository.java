package com.example.redditclonebackend.repository;

import com.example.redditclonebackend.model.Comment;
import com.example.redditclonebackend.model.Post;
import com.example.redditclonebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByUser(User user);
}
