package ru.practicum.comments.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class AuthorWithCommentsDto {
    private Long id;
    private String text;
    private Long event;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private User author;
}
