package com.klasix12.devskills.service.impl;

import com.klasix12.devskills.dto.NewTopicRequest;
import com.klasix12.devskills.dto.TopicDto;
import com.klasix12.devskills.exception.NotFoundException;
import com.klasix12.devskills.mapper.TopicMapper;
import com.klasix12.devskills.model.Topic;
import com.klasix12.devskills.repository.TopicRepository;
import com.klasix12.devskills.service.TopicService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    public List<TopicDto> getTopics(Integer from, Integer size) {
        log.info("Get topics. From: {}, Size: {}", from, size);
        return TopicMapper.toDto(topicRepository.findAll(PageRequest.of(from, size)));
    }

    public TopicDto getTopic(Long id) {
        log.info("Get topic with id: {}", id);
        return TopicMapper.toDto(topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No topic found with id: " + id)));
    }

    public TopicDto createTopic(NewTopicRequest req) {
        log.info("Create new topic: {}", req);
        return TopicMapper.toDto(topicRepository.save(new Topic(req.getTitle())));
    }
}
