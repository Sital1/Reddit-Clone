package com.example.redditclonebackend.config;

import com.example.redditclonebackend.dto.CommentsDto;
import com.example.redditclonebackend.dto.PostResponseDto;
import com.example.redditclonebackend.dto.SubredditDto;
import com.example.redditclonebackend.model.Comment;
import com.example.redditclonebackend.model.Post;
import com.example.redditclonebackend.model.Subreddit;
import com.example.redditclonebackend.utils.SubredditToSubredditDtoConverter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@AllArgsConstructor
public class BeanConfig {


    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(Subreddit.class, SubredditDto.class)
                .addMappings(new PropertyMap<Subreddit, SubredditDto>() {
                    @Override
                    protected void configure() {
                        using(new SubredditToSubredditDtoConverter()).map(source.getPosts(),destination.getNumberOfPosts());
                    }
                });

        // map name of subreddit from subreddit object to PostResponse DTO's subreddit.
        modelMapper.typeMap(Post.class, PostResponseDto.class)
                .addMapping(Post -> Post.getSubreddit().getName(), PostResponseDto::setSubreddit);

        // map username from User object to PostResponse DTO's username.
        modelMapper.typeMap(Post.class, PostResponseDto.class)
                .addMapping(Post -> Post.getUser().getUsername(), PostResponseDto::setUsername);
//                        .addMappings(new PropertyMap<Post, PostResponseDto>() {
//                            @Override
//                            protected void configure() {
//                                using(new PostToPostResponseDtoConverter()).map(source.getCreatedDate(), destination.getDuration());
//                            }
//                        });
//                .addMapping(Post -> commentRepository.findAllByPost(Post).size(), PostResponseDto::setCommentCount)
                //.addMapping(Post -> TimeAgo.using(Post.getCreatedDate().toEpochMilli()), PostResponseDto::setDuration);

        // map post and username from Comment Model to CommentDto
        modelMapper.typeMap(Comment.class, CommentsDto.class)
                .addMapping(Comment -> Comment.getPost().getPostId(), CommentsDto::setPostId)
                .addMapping(Comment -> Comment.getUser().getUsername() , CommentsDto::setUsername);


        return modelMapper;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();

        // GET, POST, HEAD
        config.applyPermitDefaultValues();

        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.DELETE);

        configSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(configSource);
    }



}



