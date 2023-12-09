package ru.practicum.comments.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CommentsDto {
    private Long id;
    private String text;
    private Long event;
    private Long author;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
