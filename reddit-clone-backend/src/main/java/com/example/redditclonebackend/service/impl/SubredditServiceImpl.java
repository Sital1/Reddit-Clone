package com.example.redditclonebackend.service.impl;

import com.example.redditclonebackend.dto.SubredditDto;
import com.example.redditclonebackend.exceptions.SpringRedditException;
import com.example.redditclonebackend.model.Subreddit;
import com.example.redditclonebackend.repository.SubredditRepository;
import com.example.redditclonebackend.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@AllArgsConstructor
@Slf4j
public class SubredditServiceImpl implements SubredditService {
    
    private final ModelMapper modelMapper;
    private final SubredditRepository subredditRepository;

    @Override
    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {

        Subreddit subredditModel = modelMapper.map(subredditDto, Subreddit.class);
        Subreddit save = subredditRepository.save(subredditModel);
        subredditDto.setId(save.getId());
        subredditDto.setName("/r/"+subredditDto.getName());

        return subredditDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {

        return subredditRepository.findAll()
                .stream()
                .map(subRedditModel -> modelMapper.map(subRedditModel, SubredditDto.class ))
                .collect(toList());

    }

    @Override
    public SubredditDto getSubreddit(Long id) {
        Subreddit subReddit = subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("No Sub Reddit found"));

        return modelMapper.map(subReddit, SubredditDto.class);

    }
}
