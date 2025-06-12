package com.klasix12.devskills.mapper;

import com.klasix12.devskills.dto.TopicDto;
import com.klasix12.devskills.model.Topic;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicMapper {

    public static List<TopicDto> toDto(Page<Topic> topics) {
        return topics.stream()
                .map(TopicMapper::toDto)
                .toList();
    }

    public static TopicDto toDto(Topic topic) {
        return new TopicDto(topic.getId(), topic.getTitle());
    }
}
