package com.example.redditclonebackend.config;

import com.example.redditclonebackend.dto.PostResponseDto;
import com.example.redditclonebackend.dto.SubredditDto;
import com.example.redditclonebackend.model.Post;
import com.example.redditclonebackend.model.Subreddit;
import com.example.redditclonebackend.utils.SubredditToSubredditDtoConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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


        return modelMapper;
    }


}
