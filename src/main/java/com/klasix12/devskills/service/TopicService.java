package com.klasix12.devskills.service;

import com.klasix12.devskills.dto.NewTopicRequest;
import com.klasix12.devskills.dto.TopicDto;

import java.util.List;

public interface TopicService {
    List<TopicDto> getTopics(Integer from, Integer size);
    TopicDto getTopic(Long id);
    TopicDto createTopic(NewTopicRequest req);
}
