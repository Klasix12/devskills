package com.klasix12.devskills.controller;

import com.klasix12.devskills.annotation.DefaultApiResponses;
import com.klasix12.devskills.dto.ErrorResponse;
import com.klasix12.devskills.dto.NewTopicRequest;
import com.klasix12.devskills.dto.TopicDto;
import com.klasix12.devskills.service.TopicService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/topic")
@Tag(name = "Topics", description = "Topic management operations. It's something like tags.")
public class TopicController {

    private final TopicService topicService;

    @DefaultApiResponses(summary = "Get topics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = TopicDto.class))),
    })
    @GetMapping
    public ResponseEntity<List<TopicDto>> getTopics(@RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(topicService.getTopics(from, size));
    }

    @DefaultApiResponses(summary = "Get topic by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "Topic not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TopicDto> getTopic(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.getTopic(id));
    }

    @DefaultApiResponses(summary = "Create new topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = TopicDto.class)))
    })
    @PostMapping
    public ResponseEntity<TopicDto> createTopic(@Valid @RequestBody NewTopicRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(topicService.createTopic(req));
    }
}
