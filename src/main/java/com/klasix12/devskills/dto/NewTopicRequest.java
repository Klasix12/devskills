package com.klasix12.devskills.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewTopicRequest {
    @NotBlank
    private String title;
}
