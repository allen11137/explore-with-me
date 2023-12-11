package ru.practicum.comments.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class CommentsRequestDto {
    @NotNull
    private String text;
    private Long event;
    @NotNull
    private Long author;
}
