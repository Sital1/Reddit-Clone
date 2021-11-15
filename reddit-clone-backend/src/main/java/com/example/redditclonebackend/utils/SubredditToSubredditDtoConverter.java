package com.example.redditclonebackend.utils;

import com.example.redditclonebackend.model.Post;
import org.modelmapper.AbstractConverter;

import java.util.List;

public class SubredditToSubredditDtoConverter extends AbstractConverter<List<Post>, Integer> {
    @Override
    public Integer convert(List<Post> postList) {
        if(postList != null) {
            return postList.size();
        } else {
            return 0;
        }
    }
}
