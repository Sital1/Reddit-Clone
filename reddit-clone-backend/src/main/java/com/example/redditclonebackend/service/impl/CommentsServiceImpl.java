package com.example.redditclonebackend.service.impl;

import com.example.redditclonebackend.dto.CommentsDto;
import com.example.redditclonebackend.exceptions.PostNotFoundException;
import com.example.redditclonebackend.model.Comment;
import com.example.redditclonebackend.model.NotificationEmail;
import com.example.redditclonebackend.model.Post;
import com.example.redditclonebackend.model.User;
import com.example.redditclonebackend.repository.CommentRepository;
import com.example.redditclonebackend.repository.PostRepository;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.service.AuthService;
import com.example.redditclonebackend.service.CommentService;
import com.example.redditclonebackend.service.MailContentBuilder;
import com.example.redditclonebackend.service.MailService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Data
@Service
@AllArgsConstructor
public class CommentsServiceImpl implements CommentService {

    private final AuthService authService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    //TODO: Construct POST URL
    private static final String POST_URL = "";

    /**
     * Saves the comment by a particular user on a particular post and sends notification of comment to the original poster
     * @param commentsDto The CommentsDto object containing info about the comment.
     */
    @Override
    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post with post id: " + commentsDto.getPostId()
                        + " not found."));

        post.setCommentCount(post.getCommentCount()+1);

        Comment comment = modelMapper.map(commentsDto, Comment.class);

        comment.setCreatedDate(Instant.now());
        comment.setPost(post);
        comment.setUser(authService.getCurrentUser());

        commentRepository.save(comment);
        String message = mailContentBuilder.build(post.getUser().getUsername() + " commented on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());

    }

    /**
     * Returns all the comments belonging to a particular post.
     * @param id The id of the post.
     * @return A list containing CommentDto object.
     */
    @Override
    public List<CommentsDto> getCommentsForPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with post id: " + id + "not found"));
       return commentRepository.findAllByPost(post)
                .stream()
                .map(comment -> modelMapper.map(comment, CommentsDto.class))
                .collect(toList());
    }

    /**
     * Returns all the comments belonging to a particular user.
     * @param username The name of the user who made the comment.
     * @return A list containing CommentDto object.
     */
    @Override
    public List<CommentsDto> getCommentsForUser(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username: " +
                username + "not found"));

       return  commentRepository.findAllByUser(user)
                .stream()
                .map(comment -> modelMapper.map(comment, CommentsDto.class))
                .collect(toList());
    }

    /**
     * Helper function to send the notification email to the original poster about new comments.
     * @param message The message of the notification
     * @param user The user object.
     */
    private void sendCommentNotification(String message, User user) {

        mailService.sendEmail(new NotificationEmail(user.getUsername() + "Commented on your post",
                user.getEmail(), message));

    }

}
