package com.klasix12.devskills.service;

import com.klasix12.devskills.dto.NewTopicRequest;
import com.klasix12.devskills.dto.TopicDto;
import com.klasix12.devskills.exception.NotFoundException;
import com.klasix12.devskills.mapper.TopicMapper;
import com.klasix12.devskills.model.Topic;
import com.klasix12.devskills.repository.TopicRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public List<TopicDto> getTopics(Integer from, Integer size) {
        return TopicMapper.toDto(topicRepository.findAll(PageRequest.of(from, size)));
    }

    public TopicDto getTopic(Long id) {
        return TopicMapper.toDto(topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No topic found with id: " + id)));
    }

    public TopicDto createTopic(NewTopicRequest req) {
        return TopicMapper.toDto(topicRepository.save(new Topic(req.getTitle())));
    }
}
