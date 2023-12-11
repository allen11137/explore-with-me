package ru.practicum.event.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserDto;

@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventBriefDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String eventDate;
    private UserDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}