package com.klasix12.devskills.repository;

import com.klasix12.devskills.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
