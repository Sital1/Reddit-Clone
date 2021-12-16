package com.example.redditclonebackend.utils;

import com.example.redditclonebackend.model.Post;
import org.modelmapper.AbstractConverter;

import java.util.List;

/**
 * A model mapper converter class that converts a postList to the size of the postList on the target.
 */
public class SubredditToSubredditDtoConverter extends AbstractConverter<List<Post>, Integer> {
    @Override
    public Integer convert(List<Post> postList) {
        if(postList != null) {
            System.out.println("aasd");
            return postList.size();
        } else {
            return 0;
        }
    }
}
